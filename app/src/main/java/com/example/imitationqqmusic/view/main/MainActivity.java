package com.example.imitationqqmusic.view.main;

import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;
import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.adapter.FooterPagerAdapter;
import com.example.imitationqqmusic.base.BaseActivity;
import com.example.imitationqqmusic.databinding.ActivityMainBinding;
import com.example.imitationqqmusic.model.SongItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected View setRootViewByBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        viewModel = MainViewModel.getInstance(this, getApplication());
        SongItem songItem = new SongItem();
        songItem.setName("我爱你1");
        songItem.setSinger("你是谁1");
        SongItem songItem1 = new SongItem();
        songItem1.setName("我爱你2");
        songItem1.setSinger("你是谁2");
        SongItem songItem2 = new SongItem();
        songItem2.setName("我爱你3");
        songItem2.setSinger("你是谁3");
        final FooterPagerAdapter adapter = new FooterPagerAdapter();

        List<SongItem> list = new ArrayList<>();
        list.add(songItem);
        list.add(songItem1);
        list.add(songItem2);
        binding.clFooter.pager2Footer.setAdapter(adapter);
        adapter.submitList(list);
        binding.clFooter.pager2Footer.setCurrentItem(1, false);
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

        viewModel.shouldTranslate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                System.out.println("==============================aBoolean:" + aBoolean);
                if (aBoolean) {
                    binding.navigation.setVisibility(View.GONE);
                } else {
                    binding.navigation.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    public boolean onSupportNavigateUp() {
        Navigation.findNavController(binding.fragment).navigateUp();
        return super.onSupportNavigateUp();
    }
}
