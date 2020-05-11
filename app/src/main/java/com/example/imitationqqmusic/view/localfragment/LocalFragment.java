package com.example.imitationqqmusic.view.localfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.View;

import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.LocalFragmentBinding;

public class LocalFragment extends BaseFragment {

    private LocalViewModel viewModel;
    private LocalFragmentBinding binding;

    public static LocalFragment newInstance() {
        return new LocalFragment();
    }

    @NonNull
    @Override
    protected View setRootView() {
        binding = LocalFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void toMusicDetail(@NonNull Bundle bundle) {

    }
}
