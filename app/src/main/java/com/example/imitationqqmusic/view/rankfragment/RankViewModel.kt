package com.example.imitationqqmusic.view.rankfragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imitationqqmusic.model.GetDataModel
import com.example.imitationqqmusic.model.bean.MusicFragmentData

class RankViewModel : ViewModel() {

    private var _list = MutableLiveData<ArrayList<MusicFragmentData>>()
    val list: LiveData<ArrayList<MusicFragmentData>> = _list

    private var _type = MutableLiveData<Int>()
    val type: LiveData<Int> = _type

    var lastType = -1
    var isFromUser = false

    fun changeType(type: Int){
        _type.postValue(type)
    }

    fun getData(context: Context){
        when(type.value){
            1 -> {
                GetDataModel.singleTon(context).getPageOfMusicFragment(GetDataModel.ApiKind.baidu, _list)
            }

            2 -> {
                GetDataModel.singleTon(context).getPageOfMusicFragment(GetDataModel.ApiKind.QQ, _list)
            }
        }
    }
}
