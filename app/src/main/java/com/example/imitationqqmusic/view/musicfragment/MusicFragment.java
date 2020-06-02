package com.example.imitationqqmusic.view.musicfragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.adapter.SlideshowPagerAdapter;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.custom.AppbarStateChangedListener;
import com.example.imitationqqmusic.databinding.MusicFragmentBinding;
import com.example.imitationqqmusic.view.main.MainViewModel;
import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicFragment extends BaseFragment {

    private MusicViewModel mViewModel;
    private MainViewModel mainViewModel;
    private MusicFragmentBinding binding;
    private Timer timer;
    private TimerTask timerTask;

    public static MusicFragment newInstance() {
        return new MusicFragment();
    }

    @Override
    protected void onCreateOptionMenuOnToolBar(Toolbar toolbar) {
        super.onCreateOptionMenuOnToolBar(toolbar);
        toolbar.inflateMenu(R.menu.default_black_more);
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
        super.setTransparentStatusBar(150, Color.WHITE);
        setTextViewSearchWidth(requireView());

        binding.appbar.addOnOffsetChangedListener(new AppbarStateChangedListener() {
            @Override
            public void onStateChanged(@Nullable AppBarLayout appbar, @NotNull State state) {
                if (state == State.EXPANDED){
                    binding.srlMusic.setEnabled(true);
                }else {
                    if (binding.srlMusic.isRefreshing()){
                        return;
                    }
                    binding.srlMusic.setEnabled(false);
                }
            }
        });

        binding.srlMusic.setColorSchemeResources(R.color.colorPrimary);
        mViewModel = new ViewModelProvider(requireActivity()).get(MusicViewModel.class);
        mainViewModel = MainViewModel.getInstance(requireActivity(), requireActivity().getApplication());

        final SlideshowPagerAdapter adapter = new SlideshowPagerAdapter();
        binding.vp2Image.setAdapter(adapter);

        mViewModel.slideshowList.observe(getViewLifecycleOwner(), new Observer<ArrayList<Object>>() {
            @Override
            public void onChanged(ArrayList<Object> objects) {
                adapter.submitList(objects);
            }
        });
        mViewModel.getSlideshowList();

        mViewModel.getLastIndex(adapter.getItemCount());

        mViewModel.lastIndex.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.vp2Image.setCurrentItem(integer, false);
            }
        });

        binding.vp2Image.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                mViewModel.setVp2Index(position);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
//                System.out.println("==============================state: " + state);

                mViewModel.setState(state);
                if (mViewModel.position.getValue() != null && state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (mViewModel.position.getValue() == adapter.getItemCount() - 1) {
                        binding.vp2Image.setCurrentItem(1, false);
                    }
                }
            }
        });

        mViewModel.position.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.vp2Image.setCurrentItem(integer, true);
            }
        });

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mViewModel.position.getValue() != null) {
                    int index = (binding.vp2Image.getCurrentItem() + 1) % (adapter.getItemCount());
                    mViewModel.set_position(index);
                }
            }
        };
        timer.schedule(timerTask, 0, 3000);

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
        mainViewModel.setShouldTranslate(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        timerTask.cancel();
    }
}
