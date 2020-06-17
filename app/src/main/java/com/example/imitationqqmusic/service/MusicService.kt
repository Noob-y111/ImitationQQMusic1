package com.example.imitationqqmusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.imitationqqmusic.model.GetDataModel
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.view.main.MainViewModel
import java.util.*

class MusicService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private lateinit var mediaPlayer: MediaPlayer
    private val model = GetDataModel.singleTon(this)
    private val intentOfProgress = Intent("progress")
    private val intentOfMax = Intent("set_max")
    private val intentOfCurrentSong = Intent("current_info")
    private val intentOfIsPlaying = Intent("isPlaying")
    private val intentOfCheckedIndex = Intent("change_checked_position")


    private var currentSongList: List<SongItem>? = null
    private var currentSongItem: SongItem? = null

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnPreparedListener(this)
        println("=========================onCreate")
    }

    override fun onBind(intent: Intent): IBinder {
        return MyBinder()
    }

    override fun onDestroy() {
        println("=========================onDestroy")
        if (MainViewModel.getViewModel() == null) {
            println("=========================null")
        } else {
            println("=========================not null")
        }
        super.onDestroy()
    }

    private fun runTimerTask() {
        timer ?: kotlin.run {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    intentOfProgress.putExtra("progress", getPosition())
                    LocalBroadcastManager.getInstance(this@MusicService).sendBroadcast(intentOfProgress)
                }
            }
        }
        timer?.schedule(timerTask, 0, 100)
    }


    private fun cancelTimerTask() {
        timer?.cancel()
        timerTask?.cancel()
        timer = null
        timerTask = null
    }

    private fun sendMax() {
        intentOfMax.putExtra("set_max", getMax())
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentOfMax)
    }

    private fun sendIsPlaying() {
        intentOfIsPlaying.putExtra("isPlaying", isPlayingService())
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentOfIsPlaying)
    }

    fun startMusicInService(songItem: SongItem) {
        cancelTimerTask()
        mediaPlayer.reset()
        println("================songItem: $songItem")
        if (songItem.isFromInternet) {
            if (songItem.path == null) return
            mediaPlayer.setDataSource(songItem.path)
        } else {
            mediaPlayer.setDataSource(application, Uri.parse(GetDataModel.uri + songItem.songMid))
            songItem.path = GetDataModel.uri + songItem.songMid
        }
        sendCheckedIndex()
        mediaPlayer.prepareAsync()
    }

    fun pauseMusicService() {
        mediaPlayer.pause()
    }

    fun playMusicService() {
        mediaPlayer.start()
    }

    fun nextMusicService(): Int {
        currentSongList?.let {
            currentSongList?.let {
                var position = it.indexOf(currentSongItem)
                position = (position + 1) % it.size
                currentSongItem = it[position]
                if (!currentSongItem!!.isFromInternet)
                    startMusicInService(currentSongItem!!)
                else{
                    if (currentSongItem!!.path == null){
                        Connection.player?.let { it1 -> model.getPlayPath(currentSongItem!!, it1) }
                    }else{
                        startMusicInService(currentSongItem!!)
                    }
                }
                sendCurrentInfo(position)
                return position
            }
        }
        return -1
    }

    fun lastMusicService(): Int {
        currentSongList?.let {
            currentSongList?.let {
                var position = it.indexOf(currentSongItem)
                if (position == 0) position = it.size
                position--
                currentSongItem = it[position]

                if (currentSongItem!!.isFromInternet)
                    startMusicInService(currentSongItem!!)
                else{
                    if (currentSongItem!!.path == null){
                        Connection.player?.let { it1 -> model.getPlayPath(currentSongItem!!, it1) }
                    }else{
                        startMusicInService(currentSongItem!!)
                    }
                }
                sendCurrentInfo(position)
                return position
            }
        }
        return -1
    }

    fun isPlayingService() = mediaPlayer.isPlaying

    private fun getMax() = mediaPlayer.duration

    fun getPosition() = mediaPlayer.currentPosition

    fun seekToService(msc: Int) {
        mediaPlayer.seekTo(msc)
    }

    fun sendCheckedIndex(){
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentOfCheckedIndex)
    }

    private fun sendCurrentInfo(position: Int) {
        intentOfCurrentSong.putExtra("position", position)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentOfCurrentSong)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        cancelTimerTask()
        mediaPlayer.reset()
        nextMusicService()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
        sendMax()
        sendIsPlaying()
        runTimerTask()
    }

    inner class MyBinder : Binder() {
        fun startMusic(songItem: SongItem) {
            startMusicInService(songItem)
        }

        fun next() = nextMusicService()

        fun last() = lastMusicService()

        fun pauseMusic() {
            pauseMusicService()
        }

        fun playMusic() {
            playMusicService()
        }

        fun isPlaying() = isPlayingService()

        fun setCurrentData(list: List<SongItem>?, position: Int) {
            currentSongItem = list?.get(position)
            currentSongList = list
        }

        fun seekTo(msc: Int) {
            seekToService(msc)
        }

        fun getCurrentSong() = currentSongItem
        fun getCurrentList() = currentSongList
    }
}
