package com.example.imitationqqmusic.view.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import java.util.Objects;

public class MainActivity extends BaseActivity{

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private Receiver receiver = new Receiver();
    private Connection connection = new Connection();

    @Override
    protected View setRootViewByBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    @SuppressWarnings("unchecked")
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

        binding.clFooter.pager2Footer.setUserInputEnabled(false);

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
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    int position = viewModel.getPosition();
                    System.out.println("==============================position: " + position);
                    System.out.println("==============================adapter.getItemCount(): " + (adapter.getItemCount()));
                    if (position == adapter.getItemCount() - 1) {
                        position = 1;
                        binding.clFooter.pager2Footer.setCurrentItem(position, false);
                    } else if (position == 0) {
                        position = adapter.getItemCount() - 2;
                        binding.clFooter.pager2Footer.setCurrentItem(position, false);
                    }
                    if (Connection.Companion.getPlayer() != null) {
                        viewModel.getPlayPath(adapter.getCurrentList().get(position-1), Connection.Companion.getPlayer());
                        viewModel.changePlayOrPause(Connection.Companion.getPlayer());
                        Connection.Companion.getPlayer().setCurrentData(adapter.getCurrentList(), position-1);
                    }
                }
            }
        });

        viewModel.currentSong.observe(this, new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(final HashMap<String, Object> map) {
                final ArrayList<SongItem> songItems = (ArrayList<SongItem>) map.get("list");
                adapter.submitList(songItems, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("==============================adapter.getItemCount: " + adapter.getItemCount());
                        binding.clFooter.pager2Footer.setUserInputEnabled(true);

                        int position = (Integer) map.get("position");
                        System.out.println("==============================position: " + position);

                        if (Connection.Companion.getPlayer() != null && songItems != null) {
                            Connection.Companion.getPlayer().setCurrentData(songItems, position);
                            viewModel.getPlayPath(songItems.get(position), Connection.Companion.getPlayer());
                        }
                        viewModel.changeCurrentSongPosition(position + 1);
                        viewModel.changePlayOrPause(Connection.Companion.getPlayer());
                        binding.clFooter.ivFooterPlayPause.setImageResource(R.drawable.footer_pause);
                    }
                });
            }
        });


        viewModel.playOrPause.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (Connection.Companion.getPlayer() == null) return;
                if (aBoolean) {
                    binding.clFooter.ivFooterPlayPause.setImageResource(R.drawable.footer_play);
                    Connection.Companion.getPlayer().pauseMusic();
                } else {
                    binding.clFooter.ivFooterPlayPause.setImageResource(R.drawable.footer_pause);
                    if (!Connection.Companion.getPlayer().isPlaying()) {
                        Connection.Companion.getPlayer().playMusic();
                    }
                }
            }
        });

        viewModel.currentSongPosition.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                System.out.println("==============================integer: " + integer);
                binding.clFooter.pager2Footer.setCurrentItem(integer, false);
                if (adapter.getItemCount()-2 == integer){
                    adapter.notifyItemChanged(integer);
                    adapter.notifyItemChanged(0);
                    adapter.notifyItemChanged(integer+1);
                }
            }
        });

        binding.clFooter.ivFooterPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.changePlayOrPause(Connection.Companion.getPlayer());
            }
        });

        viewModel.shouldTranslate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                System.out.println("==============================aBoolean:" + aBoolean);
                if (aBoolean) {
                    if (binding.navigation.getVisibility() != View.GONE) {
                        binding.navigation.setVisibility(View.GONE);
//                        setStatusWithConfig(0);
                    }
                } else {
                    if (binding.navigation.getVisibility() != View.VISIBLE) {
                        binding.navigation.setVisibility(View.VISIBLE);
//                        setStatusBar();
                    }
                }
            }
        });

        viewModel.playPosition.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.clFooter.musicProgress.setProgress(integer);
            }
        });

        viewModel.duration.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.clFooter.musicProgress.setMax(integer);
            }
        });

        binding.clFooter.ivCurrentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() == 0) return;  //todo
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("progress");
        intentFilter.addAction("set_max");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case "progress":
                    int position = intent.getIntExtra("progress", 0);
//                    System.out.println("==============================position: " + position);
                    viewModel.changePosition(position);
                    break;

                case "set_max":
                    int max = intent.getIntExtra("set_max", 0);
                    viewModel.setMaxProgress(max);
                    break;

                case "current_info":
                    int index = intent.getIntExtra("position", -1);
                    if (index == -1) return;
                    viewModel.changeCurrentSongPosition(index);
                    break;
            }
        }
    }
}
