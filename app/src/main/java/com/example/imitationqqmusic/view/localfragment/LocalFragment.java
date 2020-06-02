package com.example.imitationqqmusic.view.localfragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.adapter.MusicListAdapter;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.LocalFragmentBinding;
import com.example.imitationqqmusic.model.bean.SongItem;
import com.example.imitationqqmusic.view.main.MainViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class LocalFragment extends BaseFragment {

    private LocalViewModel viewModel;
    private MainViewModel mainViewModel;
    private LocalFragmentBinding binding;
    private NavController navController;

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
        setTextViewSearchWidth(requireView());
        mainViewModel = MainViewModel
                .getInstance(requireActivity(), requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity()).get(LocalViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.fragment);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        final MusicListAdapter adapter = new MusicListAdapter(new MusicListAdapter.OnListItemClickListener() {
            @Override
            public void onListItemClick(int position) {

            }
        });
        binding.recyclerview.setAdapter(adapter);

        viewModel.list.observe(getViewLifecycleOwner(), new Observer<ArrayList<SongItem>>() {
            @Override
            public void onChanged(ArrayList<SongItem> SongItems) {
                adapter.submitList(SongItems);
            }
        });

//        viewModel.getList();

        binding.ivLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("kind", "本地音乐");
                toMusicDetail(bundle);
            }
        });

        binding.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        super.setTransparentStatusBar(150, Color.WHITE);
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
    }

    @Override
    protected void toMusicDetail(@NonNull Bundle bundle) {
        navController.navigate(R.id.action_localFragment_to_musicListFragment, bundle);
        mainViewModel.setShouldTranslate(true);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        System.out.println("==============================onPause");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        System.out.println("==============================onStop");
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        System.out.println("==============================onDestroyView: ");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        System.out.println("==============================onDestroy: ");
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        System.out.println("==============================onStart: ");
//    }


    @NonNull
    @Override
    protected String getTitle() {
        return "我的";
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.setShouldTranslate(false);

//        if (mainViewModel != null){
//            mainViewModel.setShouldTranslate(false);
//        }
    }
}
