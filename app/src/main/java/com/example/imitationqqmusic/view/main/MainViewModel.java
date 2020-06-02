package com.example.imitationqqmusic.view.main;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.imitationqqmusic.model.bean.SongItem;

import java.util.ArrayList;
import java.util.HashMap;

public class MainViewModel extends AndroidViewModel {

    private static MainViewModel viewModel;

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

    private MutableLiveData<HashMap<String, Object>> _currentSong = new MutableLiveData<>();
    public LiveData<HashMap<String, Object>> currentSong = _currentSong;

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
}
