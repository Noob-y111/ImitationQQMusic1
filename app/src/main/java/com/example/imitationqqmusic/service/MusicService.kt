package com.example.imitationqqmusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.ViewModelProvider
import com.example.imitationqqmusic.model.GetDataModel
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.view.main.MainViewModel

class MusicService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private lateinit var mediaPlayer: MediaPlayer
    private val model = GetDataModel.singleTon(this)
    private lateinit var mainViewModel: MainViewModel


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
        if (MainViewModel.getViewModel() == null){
            println("=========================null")
        }else{
            println("=========================not null")
        }
        super.onDestroy()
    }

    fun startMusicInService(songItem: SongItem){
        mediaPlayer.reset()
        if (songItem.isFromInternet){

        }else{
            mediaPlayer.setDataSource(application, Uri.parse(GetDataModel.uri + songItem.songMid))
        }
        mediaPlayer.prepareAsync()
    }

    fun pauseMusic(){

    }

    fun playMusic(){

    }

    override fun onCompletion(mp: MediaPlayer?) {

    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
    }

    inner class MyBinder: Binder(){
        fun startMusic(songItem: SongItem){
            startMusicInService(songItem)
        }

        fun pauseMusic(){

        }

        fun playMusic(){

        }
    }
}
