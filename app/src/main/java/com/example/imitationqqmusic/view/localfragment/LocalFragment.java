package com.example.imitationqqmusic.view.localfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.LocalFragmentBinding;
import com.example.imitationqqmusic.view.TestFragment;
import com.example.imitationqqmusic.view.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class LocalFragment extends BaseFragment {

    private LocalViewModel viewModel;
    private MainViewModel mainViewModel;
    private LocalFragmentBinding binding;
    NavController navController;

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
    protected int getToolBarId() {
        return R.id.toolbar;
    }

    @Override
    protected void initView() {
        mainViewModel = MainViewModel
                .getInstance(requireActivity(), requireActivity().getApplication());
        navController = Navigation.findNavController(requireActivity(), R.id.fragment);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        TestFragment.Adapter adapter = new TestFragment.Adapter(TestFragment.Adapter.itemCallback);
        adapter.setListener(new TestFragment.Adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                navController.navigate(R.id.action_localFragment_to_testFragment);
                mainViewModel.setShouldTranslate(true);
            }
        });
        binding.recyclerview.setAdapter(adapter);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        adapter.submitList(list);
    }

    @Override
    protected void onCreateOptionMenuOnToolBar(Toolbar toolbar) {
        super.onCreateOptionMenuOnToolBar(toolbar);
        toolbar.inflateMenu(R.menu.default_black_more);
    }

    @Override
    protected void onToolBarOptionMenuSelected(MenuItem menuItem) {
        super.onToolBarOptionMenuSelected(menuItem);
        Toast.makeText(requireContext(), "点击了", Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.action_localFragment_to_testFragment);
        mainViewModel.setShouldTranslate(true);
    }

    @Override
    protected void toMusicDetail(@NonNull Bundle bundle) {

    }

    @NonNull
    @Override
    protected String getTitle() {
        return "我的";
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainViewModel != null){
            mainViewModel.setShouldTranslate(false);
        }
    }
}
