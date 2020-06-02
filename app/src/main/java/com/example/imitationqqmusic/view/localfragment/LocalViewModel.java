package com.example.imitationqqmusic.view.localfragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imitationqqmusic.model.bean.SongItem;

import java.util.ArrayList;

public class LocalViewModel extends ViewModel {
    private MutableLiveData<ArrayList<SongItem>> _list = new MutableLiveData<>();
    LiveData<ArrayList<SongItem>> list = _list;

    public void getList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<SongItem> list = new ArrayList<>();
                for (int i = 0; i <20; i++) {
                    SongItem songItem = new SongItem();
                    songItem.setName(i+"");
                    songItem.setSinger(i+"");
                    list.add(songItem);
                }
                _list.postValue(list);
            }
        }).start();
    }
}
