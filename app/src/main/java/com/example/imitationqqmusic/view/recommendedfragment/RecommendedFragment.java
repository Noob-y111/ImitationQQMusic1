package com.example.imitationqqmusic.view.recommendedfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.RecommendedFragmentBinding;

public class RecommendedFragment extends BaseFragment {

    private RecommendedViewModel mViewModel;
    private RecommendedFragmentBinding binding;

    public static RecommendedFragment newInstance() {
        return new RecommendedFragment();
    }


    @NonNull
    @Override
    protected View setRootView() {
        binding = RecommendedFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected int getToolBarId() {
        return R.id.toolbar;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void toMusicDetail(@NonNull Bundle bundle) {

    }

    @Override
    protected void onCreateOptionMenuOnToolBar(Toolbar toolbar) {
        super.onCreateOptionMenuOnToolBar(toolbar);
        toolbar.inflateMenu(R.menu.test);
    }

    @NonNull
    @Override
    protected String getTitle() {
        return "推荐";
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
