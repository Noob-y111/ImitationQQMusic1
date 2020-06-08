package com.example.imitationqqmusic.view.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.imitationqqmusic.model.GetDataModel;
import com.example.imitationqqmusic.model.bean.MusicFragmentData;
import com.example.imitationqqmusic.model.bean.SongItem;
import com.example.imitationqqmusic.service.Connection;
import com.example.imitationqqmusic.service.MusicService;

import java.util.ArrayList;
import java.util.HashMap;

public class MainViewModel extends AndroidViewModel {

    private static MainViewModel viewModel;
    private GetDataModel getDataModel = new GetDataModel(getApplication());

    private MutableLiveData<Boolean> _shouldTranslate = new MutableLiveData<>();
    LiveData<Boolean> shouldTranslate = _shouldTranslate;

    public static synchronized MainViewModel getInstance(ViewModelStoreOwner owner, Application application) {
        if (viewModel != null) {
            return viewModel;
        } else {
            viewModel = new ViewModelProvider(owner,
                    new ViewModelProvider.AndroidViewModelFactory(application))
                    .get(MainViewModel.class);
            return viewModel;
        }
    }

    public static MainViewModel getViewModel(){
        return viewModel;
    }

    private int color;
    private boolean isFirstEnterCallBack = true;

    private int navigationHeight = 0;
    private int position;

    private MutableLiveData<Integer> _playPosition = new MutableLiveData<>();
    public LiveData<Integer> playPosition = _playPosition;

    private MutableLiveData<Integer> _duration = new MutableLiveData<>();
    public LiveData<Integer> duration = _duration;

    private MutableLiveData<HashMap<String, Object>> _currentSong = new MutableLiveData<>();
    public LiveData<HashMap<String, Object>> currentSong = _currentSong;

    private MutableLiveData<Boolean> _playOrPause = new MutableLiveData<>();
    public LiveData<Boolean> playOrPause = _playOrPause;

    private MutableLiveData<Integer> _currentSongPosition = new MutableLiveData<>();
    public LiveData<Integer> currentSongPosition = _currentSongPosition;

    public void changeCurrentSongPosition(int position){
        System.out.println("==============================: " + "changeCurrentSongPosition");
        _currentSongPosition.postValue(position);
    }

    public void changePlayOrPause(@Nullable MusicService.MyBinder binder){
        if (binder == null) return;
        _playOrPause.postValue(binder.isPlaying());
    }

    public void changeCurrentSong(HashMap<String, Object> map){
        _currentSong.postValue(map);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
            this.color = color;
    }

    public boolean isFirstEnterCallBack() {
        return isFirstEnterCallBack;
    }

    public void setFirstEnterCallBack(boolean firstEnterCallBack) {
        isFirstEnterCallBack = firstEnterCallBack;
    }

    public int getNavigationHeight() {
        return navigationHeight;
    }

    public void setNavigationHeight(int navigationHeight) {
        this.navigationHeight = navigationHeight;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setShouldTranslate(Boolean b) {
        this._shouldTranslate.setValue(b);
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void changePosition(Integer position){
        _playPosition.postValue(position);
    }

    public void setMaxProgress(Integer duration){
        _duration.postValue(duration);
    }

    public void getAlbumPath(final ArrayList<SongItem> list){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (SongItem songItem: list){
                    if (songItem.getAlbumPath() != null) return;
                    if (songItem.isFromInternet()){

                    }else {
                        getDataModel.getAlbumPath(songItem);
                    }
                }
            }
        }).start();
    }

    public void getPlayPath(SongItem songItem, MusicService.MyBinder player){
        if (Connection.Companion.getPlayer() == null) return;
        if (songItem.isFromInternet()){
            if (songItem.getPath() != null){
                Connection.Companion.getPlayer().startMusic(songItem);
            }else {
                getDataModel.getPlayPath(songItem, player);
            }
        }else {
            Connection.Companion.getPlayer().startMusic(songItem);
        }
    }
}
