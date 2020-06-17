package com.example.imitationqqmusic.view.listfragment

import android.app.Application
import android.app.Service
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.custom.AppbarStateChangedListener
import com.example.imitationqqmusic.model.GetDataModel
import com.example.imitationqqmusic.model.bean.SongItem

class MusicListViewModel(application: Application) : AndroidViewModel(application) {

    private val model = GetDataModel.singleTon(application)

    private var _list: MutableLiveData<ArrayList<SongItem>> = MutableLiveData<ArrayList<SongItem>>()
    val list: LiveData<ArrayList<SongItem>> = _list

    private var _state = MutableLiveData<AppbarStateChangedListener.State>()
    val state: LiveData<AppbarStateChangedListener.State> = _state

    private var _floatingDisplay = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val floatingDisplay: LiveData<Boolean> = _floatingDisplay

    fun changeDisplayEnable(){
        _floatingDisplay.postValue(true)
//        Handler().postDelayed(Runnable {
//            if (_floatingDisplay.value == false) return@Runnable
//            _floatingDisplay.postValue(false)
//        }, 2000)
    }

    var titleColor: Int? = -1
    var alpha: Int = 0

    private var _firstAlbumPath = MutableLiveData<Any>()
    val firstAlbumPath: LiveData<Any> = _firstAlbumPath

    private var _kind = MutableLiveData<String>()
    val kind: LiveData<String> = _kind

    fun changeKind(kind: String){
        _kind.postValue(kind)
    }

    private fun changeFirstAlbumPath(path: Any){
        _firstAlbumPath.postValue(path)
    }

    private fun getSongList(){
        model.getLocalMusic(_list, _firstAlbumPath)
    }

    fun setState(state: AppbarStateChangedListener.State){
        _state.postValue(state)
    }

    fun getData(type: Int, url: Any?, apiKind: Int?){
        if (type == -1){
            getSongList()
        }else{
            when(apiKind){
                1 -> {
                    model.getRankList(type, _list)
                    if (url == null){
                        changeFirstAlbumPath(R.drawable.default_image)
                    }else{
                        changeFirstAlbumPath(url)
                    }
                }

                2 -> {
                    model.getSongList(type, _list)
                    if (url == null){
                        changeFirstAlbumPath(R.drawable.default_image)
                    }else{
                        changeFirstAlbumPath(url)
                    }
                }

                3 -> {
                    model.getListFromSinger(type, _list)
                    if (url == null){
                        changeFirstAlbumPath(R.drawable.default_image)
                    }else{
                        changeFirstAlbumPath(url)
                    }
                }

                4 -> {
                    model.getListByAlbumId(type, _list)
                    if (url == null){
                        changeFirstAlbumPath(R.drawable.default_image)
                    }else{
                        changeFirstAlbumPath(url)
                    }
                }
            }
        }
    }

//    fun getPlayPath(songItem: SongItem, player: MusicService.MyBinder){
//        with(songItem){
//            if (isFromInternet){
//                if (this.path != null){
//                    Connection.player?.startMusic(this)
//                }else{
//                    model.getPlayPath(songItem, player)
//                }
//            }else{
//                Connection.player?.startMusic(this)
//            }
//        }
//    }
}
