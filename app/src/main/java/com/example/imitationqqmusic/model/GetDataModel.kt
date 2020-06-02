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

    //content://media/external/audio/media/ 本地音乐uri前缀

    companion object {
        private var model: GetDataModel? = null
        const val uri = "content://media/external/audio/media/"

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
                            if (song.singer == "<unknown>"){
                                song.singer = "未知艺术家"
                            }
                            song.isFromInternet = false
                            song.songMid = it.getString(it.getColumnIndex(MediaStore.Audio.Media._ID))
                            song.albumId = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                            getAlbumPath(song)
                            songList.add(song)
                        }
                        if (songList.size == 0){
                            albumPath.postValue(R.drawable.default_image)
                        }else{
                            getAlbumPath(songList.random(), albumPath)
                        }
                        Thread.sleep(300)
                        list.postValue(songList)
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
                            var path: Any? = null

                            if (count > 0 && columnCount > 0) {
                                moveToNext()
                                path = getString(0)
                            }

                            if (path == null) {
                                path = R.drawable.default_image
                            }

                            song.albumPath = path
                            if (albumPath.isNotEmpty()) {
                                albumPath[0].postValue(path)
                            }
                        }
                        albumCursor?.close()
                    }

                    true -> {

                    }
                }
            } catch (e: Exception) {

            }
        }).start()
    }

    fun getPlayPath(song: SongItem){
        Thread(Runnable {

        }).start()
    }

}