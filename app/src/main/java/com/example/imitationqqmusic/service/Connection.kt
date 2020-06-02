package com.example.imitationqqmusic.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class Connection: ServiceConnection {
    companion object {
        var player: MusicService.MyBinder? = null
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        if (player != null) {
            player = null
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        println("=========================onServiceConnected")
        if (service == null) {
            println("=========================service is null")
        } else {
            println("=========================service is not null")
        }
        player = service as MusicService.MyBinder?
        if (player == null) {
            println("=========================player is null")
        } else {
            println("=========================player is not null")
        }
    }
}