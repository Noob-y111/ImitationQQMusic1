package com.example.imitationqqmusic.view.listfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imitationqqmusic.custom.AppbarStateChangedListener
import com.example.imitationqqmusic.model.GetDataModel
import com.example.imitationqqmusic.model.bean.SongItem

class MusicListViewModel(application: Application) : AndroidViewModel(application) {

    private val model = GetDataModel.singleTon(application)

    private var _list: MutableLiveData<ArrayList<SongItem>> = MutableLiveData<ArrayList<SongItem>>().also{
        it.value = ArrayList()
    }
    val list: LiveData<ArrayList<SongItem>> = _list

    private var _state = MutableLiveData<AppbarStateChangedListener.State>()
    val state: LiveData<AppbarStateChangedListener.State> = _state

    var titleColor: Int? = -1
    var alpha: Int = 0

    private var _firstAlbumPath = MutableLiveData<Any>()
    val firstAlbumPath: LiveData<Any> = _firstAlbumPath

    fun changeFirstAlbumPath(path: Any){
        _firstAlbumPath.postValue(path)
    }

    fun getSongList(){
        model.getLocalMusic(_list, _firstAlbumPath)
    }

    fun setState(state: AppbarStateChangedListener.State){
        _state.postValue(state)
    }
}
