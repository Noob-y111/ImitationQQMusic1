package com.example.imitationqqmusic.view.recommendedfragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.RecommendedFragmentBinding;
import com.example.imitationqqmusic.view.main.MainViewModel;

public class RecommendedFragment extends BaseFragment {

    private RecommendedViewModel mViewModel;
    private MainViewModel mainViewModel;
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
        super.setTransparentStatusBar(150, Color.WHITE);
        setTextViewSearchWidth(requireView());
        mainViewModel = MainViewModel.getInstance(requireActivity(), requireActivity().getApplication());
    }

    @Override
    protected void onCreateOptionMenuOnToolBar(Toolbar toolbar) {
        super.onCreateOptionMenuOnToolBar(toolbar);
        toolbar.inflateMenu(R.menu.default_black_more);
    }

    @NonNull
    @Override
    protected String getTitle() {
        return "推荐";
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.setShouldTranslate(false);
    }
}
