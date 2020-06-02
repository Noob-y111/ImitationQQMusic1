package com.example.imitationqqmusic.view.detail

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.imitationqqmusic.model.bean.SongItem

class DetailModel : ViewModel() {
    companion object{
        private var viewModel: DetailModel? = null

        fun newInstances(owner: ViewModelStoreOwner): DetailModel{
            synchronized(this){
                return if (viewModel != null){
                    viewModel as DetailModel
                }else{
                    viewModel = ViewModelProvider(owner).get(DetailModel::class.java)
                    viewModel as DetailModel
                }
            }
        }
    }

    private var _currentSong = MutableLiveData<SongItem>()
    val currentSong: LiveData<SongItem> = _currentSong

    private var _albumBitmap = MutableLiveData<Bitmap>()
    val albumBitmap: LiveData<Bitmap> = _albumBitmap

    fun setBitmap(bitmap: Bitmap){
        _albumBitmap.postValue(bitmap)
    }

    fun changeSong(songItem: SongItem){
        _currentSong.postValue(songItem)
    }
}