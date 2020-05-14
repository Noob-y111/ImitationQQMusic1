package com.example.imitationqqmusic.view.musicfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.MusicFragmentBinding;

public class MusicFragment extends BaseFragment {

    private MusicViewModel mViewModel;
    private MusicFragmentBinding binding;

    public static MusicFragment newInstance() {
        return new MusicFragment();
    }

    @Override
    protected void onCreateOptionMenuOnToolBar(Toolbar toolbar) {
        super.onCreateOptionMenuOnToolBar(toolbar);
        toolbar.inflateMenu(R.menu.test);
    }

    @Override
    protected int getToolBarId() {
        return R.id.toolbar;
    }

    @NonNull
    @Override
    protected View setRootView() {
        binding = MusicFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void toMusicDetail(@NonNull Bundle bundle) {

    }

    @NonNull
    @Override
    protected String getTitle() {
        return "音乐馆";
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
