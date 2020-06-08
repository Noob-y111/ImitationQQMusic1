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
import kotlin.collections.ArrayList

class MusicService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private lateinit var mediaPlayer: MediaPlayer
    private val model = GetDataModel.singleTon(this)
    private val intentOfProgress = Intent("progress")
    private val intentOfMax = Intent("set_max")
    private val intentOfCurrentSong = Intent("current_info")


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

    fun startMusicInService(songItem: SongItem) {
        cancelTimerTask()
        mediaPlayer.reset()
        if (songItem.isFromInternet) {
            if (songItem.path == null ) return
            mediaPlayer.setDataSource(songItem.path)
        } else {
            mediaPlayer.setDataSource(application, Uri.parse(GetDataModel.uri + songItem.songMid))
            songItem.path = GetDataModel.uri + songItem.songMid
        }
        mediaPlayer.prepareAsync()
    }

    fun pauseMusicService() {
        mediaPlayer.pause()
    }

    fun playMusicService() {
        mediaPlayer.start()
    }

    fun nextMusicService(): Int{
        currentSongList ?.let {
            currentSongList?.let {
                var position = it.indexOf(currentSongItem)
                position = (position + 1) % it.size
                currentSongItem = it[position]
                startMusicInService(currentSongItem!!)
                sendCurrentInfo(position)
                return position
            }
        }
        return -1
    }

    fun lastMusicService(): Int{
        currentSongList ?.let {
            currentSongList?.let {
                var position = it.indexOf(currentSongItem)
                if (position == 0) position = it.size
                position --
                currentSongItem = it[position]
                startMusicInService(currentSongItem!!)
                sendCurrentInfo(position)
                return position
            }
        }
        return  -1
    }

    fun isPlayingService() = mediaPlayer.isPlaying

    private fun getMax() = mediaPlayer.duration

    fun getPosition() = mediaPlayer.currentPosition

    private fun sendCurrentInfo(position: Int){
        intentOfCurrentSong.putExtra("position", position)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentOfCurrentSong)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        cancelTimerTask()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
        sendMax()
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

        fun setCurrentData(list: List<SongItem>?, position: Int){
            currentSongItem = list?.get(position)
            currentSongList = list
        }
    }
}
