package com.example.imitationqqmusic.view.current_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imitationqqmusic.model.bean.SongItem

class CurrentListViewModel: ViewModel() {

    private var _list = MutableLiveData<ArrayList<SongItem>>()
    val list: LiveData<ArrayList<SongItem>> = _list

    private var _lastList = MutableLiveData<ArrayList<SongItem>>()
    val lastList: LiveData<ArrayList<SongItem>> = _list

    init {

    }
}