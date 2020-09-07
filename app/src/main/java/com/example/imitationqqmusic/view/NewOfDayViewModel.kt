package com.example.imitationqqmusic.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imitationqqmusic.adapter.AlbumAdapter
import com.example.imitationqqmusic.adapter.RecyclerSimpleAdapter
import com.example.imitationqqmusic.model.GetDataModel
import com.example.imitationqqmusic.model.GetDataModel.Companion.singleTon
import com.example.imitationqqmusic.model.GetDataModel.UpdateOffset
import com.example.imitationqqmusic.model.bean.Album
import com.example.imitationqqmusic.view.musicfragment.MusicViewModel

class NewOfDayViewModel : ViewModel() {

    init {

    }

    private var _list: MutableLiveData<ArrayList<Album>> = MutableLiveData()
    val list: LiveData<ArrayList<Album>> = _list

    private var _state: MutableLiveData<AlbumAdapter.State> = MutableLiveData()
    val state: LiveData<AlbumAdapter.State> = _state

    private var offset = 0
    private val count = 10
    var isLoading = false

    fun resetGetList(context: Context?) {
        offset = 0
        isLoading = false
        _state.postValue(AlbumAdapter.State.Loading)
        singleTon(context!!).getAlbumList(_list, _state, object : UpdateOffset {
            override fun update(size: Int) {
                offset += count
                isLoading = false
            }
        })
    }

    fun getList(context: Context){
        println("================isLoading: $isLoading")
        if (isLoading) return else isLoading = true
        GetDataModel.singleTon(context).getAlbumList(_list, count, offset, _state, object : GetDataModel.UpdateOffset{
            override fun update(size: Int) {
                offset += size
                isLoading = false
            }
        })
    }
}