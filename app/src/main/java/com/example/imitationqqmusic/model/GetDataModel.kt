package com.example.imitationqqmusic.model

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.bean.SongItem
import java.lang.Exception

class GetDataModel(private val context: Context) {

    companion object {
        private var model: GetDataModel? = null

        fun singleTon(context: Context): GetDataModel {
            synchronized(this) {
                if (model == null) {
                    model = GetDataModel(context)
                }
                return model as GetDataModel
            }
        }
    }

    @SuppressLint("Recycle")
    fun getLocalMusic(list: MutableLiveData<ArrayList<SongItem>>, albumPath: MutableLiveData<Any>) {
        Thread(Runnable {
            val cursor: Cursor? = context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
            try {
                cursor?.let {
                    if (it.count > 0) {
                        val songList = ArrayList<SongItem>()
                        while (it.moveToNext()) {
                            val song = SongItem()
                            song.name = it.getString(it.getColumnIndex(MediaStore.Audio.Media.TITLE))
                            song.singer = it.getString(it.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                            song.songMid = it.getString(it.getColumnIndex(MediaStore.Audio.Media._ID))
                            song.albumId = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                            songList.add(song)
                        }
                        list.postValue(songList)
//                        albumPath.postValue(R.drawable.default_image)
                        getAlbumPath(songList[0], albumPath)
                    }
                }
            } catch (e: Exception) {

            } finally {
                cursor?.close()
            }
        }).start()
    }

    @SuppressLint("Recycle")
    fun getAlbumPath(song: SongItem, vararg albumPath: MutableLiveData<Any>) {
        Thread(Runnable {
            try {
                when (song.isFromInternet) {
                    false -> {
                        val uri = "content://media/external/audio/albums"
                        val selector = arrayOf("album_art")
                        val albumCursor = context.contentResolver.query(
                                Uri.parse("$uri/${song.albumId}"),
                                selector,
                                null, null, null
                        )
                        albumCursor?.run {
                            println("=========================run")
                            println("================count: $count")
                            println("================columnCount: $columnCount")

                            var path: Any? = null

                            if (count > 0 && columnCount > 0) {
                                moveToNext()
                                println("================getString(): ${getString(0)}")
                                path = getString(0)
                            }

                            if (path == null) {
                                path = R.drawable.default_image
                            }

                            if (albumPath.isEmpty()) {
                                song.albumPath = path
                            } else {
                                albumPath[0].postValue(path)
                            }
                            println("================path: $path")
                        }
                        println("================song: $song")
                        albumCursor?.close()
                    }

                    true -> {

                    }
                }
            } catch (e: Exception) {

            }
        }).start()
    }

}