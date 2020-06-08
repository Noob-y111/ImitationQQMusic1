package com.example.imitationqqmusic.model.bean

data class MusicFragmentData(
        val name: String,
        val smallImageUrl: String,
        val bigImageUrl: String,
        val type: Int,
        val songItem: ArrayList<SongItem>
)