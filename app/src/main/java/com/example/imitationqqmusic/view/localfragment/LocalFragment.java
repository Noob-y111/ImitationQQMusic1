package com.example.imitationqqmusic.view.localfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.View;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.LocalFragmentBinding;
import com.example.imitationqqmusic.view.main.MainViewModel;

public class LocalFragment extends BaseFragment {

    private LocalViewModel viewModel;
    private MainViewModel mainViewModel;
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
        binding.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_localFragment_to_testFragment);
                mainViewModel = MainViewModel
                        .getInstance(requireActivity(), requireActivity().getApplication());
                mainViewModel.setShouldTranslate(true);
            }
        });
    }

    @Override
    protected void toMusicDetail(@NonNull Bundle bundle) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainViewModel != null){
            mainViewModel.setShouldTranslate(false);
        }
    }
}
