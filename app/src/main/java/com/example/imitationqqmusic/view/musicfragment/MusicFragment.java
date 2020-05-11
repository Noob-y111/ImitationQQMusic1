package com.example.imitationqqmusic.view.musicfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.View;

import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.MusicFragmentBinding;

public class MusicFragment extends BaseFragment {

    private MusicViewModel mViewModel;
    private MusicFragmentBinding binding;

    public static MusicFragment newInstance() {
        return new MusicFragment();
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
}
