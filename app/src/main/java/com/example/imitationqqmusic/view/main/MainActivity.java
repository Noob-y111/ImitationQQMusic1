package com.example.imitationqqmusic.view.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
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
                if (aBoolean){
                    binding.navigation.setVisibility(View.GONE);
                    setStatusWithConfig(0);
                }else {
                    binding.navigation.setVisibility(View.VISIBLE);
                    setStatusBar();
                }
//                final ConstraintLayout.LayoutParams oldLayoutParam
//                        = (ConstraintLayout.LayoutParams)binding.fragment.getLayoutParams();
//                if (aBoolean) {
//                    ((ConstraintLayout.LayoutParams) binding.clFooter.clFooter.getLayoutParams())
//                            .goneBottomMargin = binding.navigation.getHeight();
//                    binding.navigation.setVisibility(View.GONE);
//
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(
//                            binding.clFooter.clFooter,
//                            "y",
//                            binding.clFooter.clFooter.getY(),
//                            binding.clFooter.clFooter.getY() + binding.navigation.getHeight());
//                    animator.start();
//                } else {
//                    ((ConstraintLayout.LayoutParams) binding.clFooter.clFooter.getLayoutParams())
//                            .goneBottomMargin = 0;
//                    binding.navigation.setVisibility(View.GONE);
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(
//                            binding.clFooter.clFooter,
//                            "y",
//                            binding.clFooter.clFooter.getY(),
//                            binding.clFooter.clFooter.getY() - binding.navigation.getHeight());
//                    animator.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            binding.navigation.setVisibility(View.INVISIBLE);
//                            binding.fragment.setLayoutParams(oldLayoutParam);
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            binding.navigation.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
//                    animator.start();
//                }
            }
        });
    }

    @Override
    protected int getNavControllerId() {
        return R.id.fragment;
    }

    @Override
    protected BottomNavigationView getBottomNavigationView() {
        return binding.navigation;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        Navigation.findNavController(binding.fragment).navigateUp();
//        return super.onSupportNavigateUp();
//    }

}
