package com.example.imitationqqmusic.model

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.bean.MusicFragmentData
import com.example.imitationqqmusic.model.bean.Singer
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.service.Connection
import com.example.imitationqqmusic.service.MusicService
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.math.sin

class GetDataModel(private val context: Context) {

    //content://media/external/audio/media/ 本地音乐uri前缀

    companion object {
        private var model: GetDataModel? = null
        const val uri = "content://media/external/audio/media/"
        val type = intArrayOf(1, 2, 21, 22, 23, 24, 25)
        val topId: IntArray = intArrayOf(25, 35)

        fun singleTon(context: Context): GetDataModel {
            synchronized(this) {
                if (model == null) {
                    model = GetDataModel(context)
                }
                return model as GetDataModel
            }
        }
    }

    enum class ApiKind {
        baidu, QQ
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
                            if (song.singer == "<unknown>") {
                                song.singer = "未知艺术家"
                            }
                            song.isFromInternet = false
                            song.songMid = it.getString(it.getColumnIndex(MediaStore.Audio.Media._ID))
                            song.albumId = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                            getAlbumPath(song)
                            songList.add(song)
                        }
                        if (songList.size == 0) {
                            albumPath.postValue(R.drawable.default_image)
                        } else {
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

    fun getRankList(type: Int, list: MutableLiveData<ArrayList<SongItem>>) {
        val url = "http://tingapi.ting.baidu.com/v1/restserver/ting?size=0&type=${type}&callback=cb_list&_t=1468380543284&format=json&method=baidu.ting.billboard.billList"
        val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                Response.Listener {
                    Thread(Runnable {
                        val data = JSONObject(it.substring(12, it.length - 2))
                        val songList = data.getJSONArray("song_list")
                        val size = songList.length()
                        val songs = ArrayList<SongItem>()
                        for (i in 0 until size) {
                            val song = SongItem()
                            val oneSong = songList.getJSONObject(i)
                            song.name = oneSong.getString("title")
                            song.singer = oneSong.getString("artist_name")
                            song.songMid = oneSong.getString("song_id")
                            song.albumPath = oneSong.getString("pic_huge")
                            song.isFromInternet = true
                            song.kind = ApiKind.baidu
                            songs.add(song)
                        }
                        list.postValue(songs)
                    }).start()
                },
                Response.ErrorListener {

                }
        )
        VolleyInstance.getInstance(context).requestQueue.add(stringRequest)
    }

    fun getSongList(type: Int, list: MutableLiveData<ArrayList<SongItem>>){
        val url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8¬ice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=$type&_=1519963122923"
        val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                Response.Listener {
                    Thread(Runnable {
                        val data = JSONObject(it)
                        val songList = data.getJSONArray("songlist")
                        val songs = ArrayList<SongItem>()
                        val size = songList.length()
                        for(j in 0 until size){
                            val song = SongItem()
                            val songInfo = songList.getJSONObject(j)
                            val songData = songInfo.getJSONObject("data")

                            val singerList = songData.getJSONArray("singer")
                            val length = singerList.length()
                            song.singer = ""
                            for (index in 0 until length){
                                val singer = singerList.getJSONObject(index)
                                song.singer += singer.getString("name")
                                if (index != length - 1) {
                                    song.singer += "/"
                                }
                            }

                            song.albumId = songData.getLong("albumid")
                            song.albumPath = "http://imgcache.qq.com/music/photo/album_300/" +
                                    (song.albumId % 100) + "/300_albumpic_" +
                                    song.albumId + "_0.jpg"
                            song.songMid = songData.getString("songmid")
                            song.name = songData.getString("songname")
                            song.isFromInternet = true
                            song.kind = ApiKind.QQ
                            songs.add(song)
                        }
                        list.postValue(songs)
                    }).start()
                },
                Response.ErrorListener {

                }
        )
        VolleyInstance.getInstance(context).requestQueue.add(stringRequest)
    }

    fun getPageOfMusicFragment(kind: ApiKind, listOfMusic: MutableLiveData<ArrayList<MusicFragmentData>>) {
        when (kind) {
            ApiKind.baidu -> {
                val list = ArrayList<MusicFragmentData>()
                for (i in type.indices) {
                    val url = "http://tingapi.ting.baidu.com/v1/restserver/ting?size=3&type=${type[i]}&callback=cb_list&_t=1468380543284&format=json&method=baidu.ting.billboard.billList"
                    val stringRequest = StringRequest(
                            Request.Method.GET,
                            url,
                            Response.Listener {
                                Thread(Runnable {
//                                    println("================it: ${it.substring(12, it.length-2)}")
                                    val data = JSONObject(it.substring(12, it.length - 2))
                                    val billboard = data.getJSONObject("billboard")
                                    val name = billboard.getString("name")
                                    val urlType = billboard.getInt("billboard_type")
                                    val smallImage = billboard.getString("pic_s192")
                                    val bigImage = billboard.getString("pic_s444")
                                    val songs = ArrayList<SongItem>()
                                    val songList = data.getJSONArray("song_list")

                                    for (index in 0 until songList.length()) {
                                        val song = SongItem()
                                        song.name = songList.getJSONObject(index).getString("title")
                                        song.singer = songList.getJSONObject(index).getString("artist_name")
                                        songs.add(song)
                                    }

                                    synchronized(list) {
                                        val item = MusicFragmentData(name, smallImage, bigImage, urlType, songs)
                                        list.add(item)
                                        if (list.size == type.size) {
                                            listOfMusic.postValue(list)
                                        }
                                    }
                                }).start()
                            },
                            Response.ErrorListener {

                            }
                    )
                    VolleyInstance.getInstance(context).requestQueue.add(stringRequest)
                }
            }

            ApiKind.QQ -> {
                val list = ArrayList<MusicFragmentData>()
                for (i in topId[0]..topId[1]) {
                    val url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8¬ice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=$i&_=1519963122923"
                    val stringRequest = StringRequest(
                            Request.Method.GET,
                            url,
                            Response.Listener {
                                Thread(Runnable {
                                    val data = JSONObject(it)
                                    val topInfo = data.getJSONObject("topinfo")
                                    val name = topInfo.getString("ListName")
                                    val smallUrl = topInfo.getString("pic_v12")
                                    val songList = data.getJSONArray("songlist")
                                    val songs = ArrayList<SongItem>()
                                    for(j in 0..2){
                                        val song = SongItem()
                                        val songInfo = songList.getJSONObject(j)
                                        val songData = songInfo.getJSONObject("data")
                                        val singerList = songData.getJSONArray("singer")
                                        val size = singerList.length()
                                        song.singer = ""
                                        for (index in 0 until size){
                                            val singer = singerList.getJSONObject(index)
                                            song.singer += singer.getString("name")
                                            if (index != size - 1) {
                                                song.singer += "/"
                                            }
                                        }
                                        song.name = songData.getString("songname")
                                        songs.add(song)
                                    }

                                    synchronized(list){
                                        val item = MusicFragmentData(name, smallUrl, smallUrl, i, songs)
                                        list.add(item)
                                        if (list.size == (topId[1] - topId[0])) {
                                            listOfMusic.postValue(list)
                                        }
                                    }
                                }).start()
                            },
                            Response.ErrorListener {

                            }
                    )
                    VolleyInstance.getInstance(context).requestQueue.add(stringRequest)
                }
            }
        }
    }

    fun getPlayPath(song: SongItem, player: MusicService.MyBinder) {
        Thread(Runnable {
            when(song.kind){
                ApiKind.QQ -> {
                    val token =
                            "https://u.y.qq.com/cgi-bin/musicu.fcg?format=json&data=%7B%22req_0%22%3A%7B%22module%22%" +
                                    "3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%2" +
                                    "2%3A%22358840384%22%2C%22songmid%22%3A%5B%22${song.songMid}%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221443481947%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A%2218585073516%22%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D"
                    val stringRequest = StringRequest(
                            Request.Method.GET,
                            token,
                            Response.Listener {
                                val vKeyJson = JSONObject(it)
                                val reg = vKeyJson.getJSONObject("req_0")
                                val data = reg.getJSONObject("data")
//                            val sip = data.getJSONArray("sip")
                                val midUrlInfo = data.getJSONArray("midurlinfo")
                                val vKeyData = midUrlInfo.getJSONObject(0)
                                val strVKey = vKeyData.getString("vkey")
                                val pUrl = vKeyData.getString("purl")

                                val playPath: String? = if (strVKey == "") {
                                    null
                                } else {
//                                "https://ws.stream.qqmusic.qq.com/$pUrl"
                                    "https://isure.stream.qqmusic.qq.com//$pUrl"
                                }

                                song.path = playPath
                                player.startMusic(song)
                            },
                            Response.ErrorListener {

                            }
                    )
                    VolleyInstance.getInstance(context).requestQueue.add(stringRequest)
                }

                else -> {
                    val url = "http://music.taihe.com/data/music/fmlink?rate=320&songIds=${song.songMid}&type=&_t=1468380564513&format=json"
                    val stringRequest = StringRequest(
                            Request.Method.GET,
                            url,
                            Response.Listener {
                                Thread(Runnable {
                                    val data = JSONObject(it).getJSONObject("data")
                                    val songList = data.getJSONArray("songList")
                                    song.path = songList.getJSONObject(0).getString("showLink")
                                    player.startMusic(song)
                                }).start()
                            },
                            Response.ErrorListener {

                            }
                    )
                    VolleyInstance.getInstance(context).requestQueue.add(stringRequest)
                }
            }
        }).start()
    }

    fun getListFromSinger(id: Int, list: MutableLiveData<ArrayList<SongItem>>){
        val url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.artist.getSongList&format=json=1&tinguid=$id&offset=2&limits=100"
        val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                Response.Listener {
                    Thread(Runnable {
                        val data = JSONObject(it)
                        val songList = data.getJSONArray("songlist")
                        val size = songList.length()
                        val songs = ArrayList<SongItem>()
                        for (i in 0 until size) {
                            val song = SongItem()
                            val oneSong = songList.getJSONObject(i)
                            song.name = oneSong.getString("title")
                            song.singer = oneSong.getString("author")
                            song.songMid = oneSong.getString("song_id")
                            song.albumPath = oneSong.getString("pic_huge")
                            song.isFromInternet = true
                            song.kind = ApiKind.baidu
                            songs.add(song)
                        }
                        list.postValue(songs)
                    }).start()
                },
                Response.ErrorListener {

                }
        )
        VolleyInstance.getInstance(context).requestQueue.add(stringRequest)
    }

    fun getSingerList(list: MutableLiveData<ArrayList<Singer>>){
        val url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.artist.get72HotArtist&format=json=1&offset=0&limit=100"
        val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                Response.Listener {
                    Thread(Runnable {
                        val data = JSONObject(it)
                        val artists = data.getJSONArray("artist")
                        val size = artists.length()

                        val singerList = ArrayList<Singer>()
                        for (i in 0 until size){
                            val singer = artists.getJSONObject(i)
                            val name = singer.getString("name")
                            val id = singer.getString("ting_uid")
                            val image = singer.getString("avatar_big")
                            val artist = Singer(name, image, id)
                            singerList.add(artist)
                        }
                        list.postValue(singerList)
                    }).start()
                },
                Response.ErrorListener {

                }
        )
        VolleyInstance.getInstance(context).requestQueue.add(stringRequest)
    }

}