package com.example.imitationqqmusic.view.singer

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imitationqqmusic.model.GetDataModel
import com.example.imitationqqmusic.model.bean.Singer

class SingerViewModel : ViewModel() {
    private var _singerList = MutableLiveData<ArrayList<Singer>>()
    val singerList: LiveData<ArrayList<Singer>> = _singerList

    var isFromUser = false

    fun updateSingerList(context: Context){
        GetDataModel.singleTon(context).getSingerList(_singerList)
    }
}