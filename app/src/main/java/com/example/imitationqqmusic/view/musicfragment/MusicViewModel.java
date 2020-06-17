package com.example.imitationqqmusic.view.musicfragment;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imitationqqmusic.adapter.RecyclerSimpleAdapter;
import com.example.imitationqqmusic.model.GetDataModel;
import com.example.imitationqqmusic.model.bean.Album;

import java.util.ArrayList;

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

    private MutableLiveData<ArrayList<Album>> _list = new MutableLiveData<>();
    LiveData<ArrayList<Album>> list = _list;

    private MutableLiveData<RecyclerSimpleAdapter.State> _footerState = new MutableLiveData<>();
    LiveData<RecyclerSimpleAdapter.State> footerState = _footerState;

    public void changeFooterState(RecyclerSimpleAdapter.State state){
        _footerState.postValue(state);
    }

//    private boolean refreshEnable = false;
//
//    public boolean isRefreshEnable() {
//        return refreshEnable;
//    }
//
//    public void setRefreshEnable(boolean refreshEnable) {
//        this.refreshEnable = refreshEnable;
//    }

    private int offset = 0;
    private static final int count = 10;

    private int recyclerState;

    public int getRecyclerState() {
        return recyclerState;
    }

    public void setRecyclerState(int recyclerState) {
        this.recyclerState = recyclerState;
    }

    public void resetGetList(final Context context){
        offset = 0;
        isLoading = false;
        _footerState.postValue(RecyclerSimpleAdapter.State.Loading);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                GetDataModel.Companion.singleTon(context).getAlbumList(_list, _footerState, new GetDataModel.UpdateOffset() {
//                    @Override
//                    public void update(int size) {
//                        offset += count;
//                    }
//                });
//            }
//        }, 5000);
        GetDataModel.Companion.singleTon(context).getAlbumList(_list, _footerState, new GetDataModel.UpdateOffset() {
            @Override
            public void update(int size) {
                offset += count;
            }
        });
    }

    public void getList(Context context, final int count){
        GetDataModel.Companion.singleTon(context).getAlbumList(_list, count, offset, _footerState, new GetDataModel.UpdateOffset() {
            @Override
            public void update(int size) {
                offset += size;
                isLoading = false;
            }
        });
    }

    private boolean isLoading = false;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void loadMore(Context context, RecyclerSimpleAdapter.State state){
        if (state == RecyclerSimpleAdapter.State.Complete) return;
        if (isLoading) return;
        isLoading = true;
        _footerState.postValue(RecyclerSimpleAdapter.State.Loading);
        getList(context, count);
    }

    public void getSlideshowList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("==============================:hear ");
                if (slideshowList.getValue() == null) {
                    ArrayList<Object> list = new ArrayList<>();
                    list.add("http://img1.imgtn.bdimg.com/it/u=4028199665,1325366622&fm=26&gp=0.jpg");
                    list.add("http://pic1.win4000.com/wallpaper/5/5603c2b6acfe9.jpg");
                    list.add("http://pic1.win4000.com/wallpaper/9/5603c261b44cc.jpg");
                    list.add("http://pic1.win4000.com/wallpaper/e/5603c11b17529.jpg");
                    list.add("https://img.bizhiku.net/uploads/2018/0823/bwhj2ouxn0h.jpg?x-oss-process=style/w_870-h_870");
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
