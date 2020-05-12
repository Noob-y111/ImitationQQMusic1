package com.example.imitationqqmusic.view.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class MainViewModel extends AndroidViewModel {

    private static MainViewModel viewModel;

    private MutableLiveData<Boolean> _shouldTranslate = new MutableLiveData<>();
    public LiveData<Boolean> shouldTranslate = _shouldTranslate;

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

    private int navigationHeight = 0;
    private int position;

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
