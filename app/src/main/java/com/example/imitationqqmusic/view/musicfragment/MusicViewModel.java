package com.example.imitationqqmusic.view.musicfragment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imitationqqmusic.model.GetDataModel;
import com.example.imitationqqmusic.model.bean.MusicFragmentData;

import java.util.ArrayList;
import java.util.HashMap;

public class MusicViewModel extends ViewModel {

    private MutableLiveData<Integer> _position = new MutableLiveData<>();
    LiveData<Integer> position = _position;

    private int state;
    private int vp2Index;
    private boolean isTouch;

    private MutableLiveData<Integer> _lastIndex = new MutableLiveData<>();
    LiveData<Integer> lastIndex = _lastIndex;

    private MutableLiveData<ArrayList<Object>> _slideshowList = new MutableLiveData<>();
    LiveData<ArrayList<Object>> slideshowList = _slideshowList;

    private MutableLiveData<ArrayList<MusicFragmentData>> _list = new MutableLiveData<>();
    LiveData<ArrayList<MusicFragmentData>> list = _list;

    void getList(Context context){
        GetDataModel.Companion.singleTon(context).getPageOfMusicFragment(GetDataModel.ApiKind.baidu, _list);
    }

    public void getSlideshowList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("==============================:hear ");
                if (slideshowList.getValue() == null) {
                    ArrayList<Object> list = new ArrayList<>();
                    list.add("http://hiphotos.qianqian.com/ting/pic/item/c83d70cf3bc79f3d98ca8e36b8a1cd11728b2988.jpg");
                    list.add("http://hiphotos.qianqian.com/ting/pic/item/8d5494eef01f3a291bf6bec89b25bc315c607cfd.jpg");
                    list.add("http://hiphotos.qianqian.com/ting/pic/item/f703738da97739121a5aed67fa198618367ae2bc.jpg");
                    _slideshowList.postValue(list);
                }
            }
        }).start();
    }

    public void getLastIndex(final int count) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (position.getValue() != null) {
                    int index = position.getValue();
                    if (index == count - 1) {
                        _lastIndex.postValue(1);
                    } else if (index == 0) {
                        _lastIndex.postValue(count - 2);
                    } else {
                        _lastIndex.postValue(index);
                    }
                } else {
                    _lastIndex.postValue(1);
                    set_position(1);
                }
            }
        }).start();
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }

    public int getVp2Index() {
        return vp2Index;
    }

    public void setVp2Index(int vp2Index) {
        this.vp2Index = vp2Index;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void set_position(Integer position) {
        _position.postValue(position);
    }
}
