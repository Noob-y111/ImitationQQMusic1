package com.example.imitationqqmusic.view.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager2.widget.ViewPager2;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.adapter.FooterPagerAdapter;
import com.example.imitationqqmusic.base.BaseActivity;
import com.example.imitationqqmusic.databinding.ActivityMainBinding;
import com.example.imitationqqmusic.model.bean.SongItem;
import com.example.imitationqqmusic.service.Connection;
import com.example.imitationqqmusic.service.MusicService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private Connection connection = new Connection();

    @Override
    protected View setRootViewByBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        System.out.println("==============================bindService(intent, connection, Context.BIND_AUTO_CREATE): " + bindService(intent, connection, Context.BIND_AUTO_CREATE));

        viewModel = MainViewModel.getInstance(this, getApplication());
//        final SongItem songItem = new SongItem();
//        songItem.setName("我爱你1");
//        songItem.setSinger("你是谁1");
//        SongItem songItem1 = new SongItem();
//        songItem1.setName("我爱你2");
//        songItem1.setSinger("你是谁2");
//        SongItem songItem2 = new SongItem();
//        songItem2.setName("我爱你3");
//        songItem2.setSinger("你是谁3");

        final FooterPagerAdapter adapter = new FooterPagerAdapter(new FooterPagerAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {

            }
        }, getSupportFragmentManager());

        binding.clFooter.pager2Footer.setAdapter(adapter);
        binding.clFooter.pager2Footer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewModel.setPosition(position);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                System.out.println("==============================onPageScrollStateChanged: " + "onPageScrollStateChanged");
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    int position = viewModel.getPosition();
                    if (position == adapter.getItemCount() - 1) {
                        binding.clFooter.pager2Footer.setCurrentItem(1, false);
                    } else if (position == 0) {
                        binding.clFooter.pager2Footer.setCurrentItem(adapter.getItemCount() - 2, false);
                    } else {
                        //TODO()
                    }
                }
            }
        });

        viewModel.currentSong.observe(this, new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> map) {
                ArrayList<SongItem> songItems = (ArrayList<SongItem>) map.get("list");
                adapter.submitList(songItems);
                int position = (Integer) map.get("position") + 1;
                System.out.println("==============================position: " + position);
                System.out.println("==============================adapter.getItemCount(): " + adapter.getItemCount());
                binding.clFooter.pager2Footer.setCurrentItem( position, false);
            }
        });

        viewModel.shouldTranslate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                System.out.println("==============================aBoolean:" + aBoolean);
                if (aBoolean){
                    if (binding.navigation.getVisibility() != View.GONE){
                        binding.navigation.setVisibility(View.GONE);
                        setStatusWithConfig(0);
                    }
                }else {
                    if (binding.navigation.getVisibility() != View.VISIBLE) {
                        binding.navigation.setVisibility(View.VISIBLE);
                        setStatusBar();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    protected int getNavControllerId() {
        return R.id.fragment;
    }

    @Override
    protected BottomNavigationView getBottomNavigationView() {
        return binding.navigation;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
