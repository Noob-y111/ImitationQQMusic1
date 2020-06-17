package com.example.imitationqqmusic.view.musicfragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.adapter.RecyclerSimpleAdapter;
import com.example.imitationqqmusic.adapter.SlideshowPagerAdapter;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.custom.AppbarStateChangedListener;
import com.example.imitationqqmusic.databinding.MusicFragmentBinding;
import com.example.imitationqqmusic.model.bean.Album;
import com.example.imitationqqmusic.model.tools.DpPxUtils;
import com.example.imitationqqmusic.model.tools.ScreenUtils;
import com.example.imitationqqmusic.view.main.MainViewModel;
import com.google.android.material.appbar.AppBarLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MusicFragment extends BaseFragment {

    private MusicViewModel mViewModel;
    private MainViewModel mainViewModel;
    private MusicFragmentBinding binding;
    private Timer timer;
    private TimerTask timerTask;
    private NavController navController;

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

    private void setCheckCircle(int checkIndex) {
        for (int i = 0; i < binding.circleParent.getChildCount(); i++) {
            if (i != checkIndex) {
                ((CheckBox) binding.circleParent.getChildAt(i)).setChecked(false);
            } else {
                ((CheckBox) binding.circleParent.getChildAt(i)).setChecked(true);
            }
        }
    }

    @Override
    protected void initView() {
        super.setTransparentStatusBar(150, Color.WHITE);
        setTextViewSearchWidth(requireView());
        navController = Navigation.findNavController(requireActivity(), R.id.fragment);

        binding.srlMusic.setColorSchemeResources(R.color.colorPrimary);
        mViewModel = new ViewModelProvider(requireActivity()).get(MusicViewModel.class);
        mainViewModel = MainViewModel.getInstance(requireActivity(), requireActivity().getApplication());

        final SlideshowPagerAdapter adapter = new SlideshowPagerAdapter();
        binding.vp2Image.setAdapter(adapter);

        mViewModel.slideshowList.observe(getViewLifecycleOwner(), new Observer<ArrayList<Object>>() {
            @Override
            public void onChanged(ArrayList<Object> objects) {
                if (objects.size() > 1) {
                    for (int i = 0; i < objects.size(); i++) {
                        CheckBox checkBox = new CheckBox(requireContext());
                        checkBox.setEnabled(false);
                        checkBox.setButtonDrawable(null);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        layoutParams.height = DpPxUtils.Companion.dp2Px(requireContext(), 5);
                        layoutParams.weight = DpPxUtils.Companion.dp2Px(requireContext(), 5);
                        layoutParams.weight = 1;
                        layoutParams.leftMargin = DpPxUtils.Companion.dp2Px(requireContext(), 3);
                        layoutParams.rightMargin = DpPxUtils.Companion.dp2Px(requireContext(), 3);
                        checkBox.setBackground(getResources().getDrawable(R.drawable.circle, null));
                        binding.circleParent.addView(checkBox, layoutParams);
                    }
                    ((CheckBox) binding.circleParent.getChildAt(0)).setChecked(true);
                }
                adapter.submitList(objects, new Runnable() {
                    @Override
                    public void run() {
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
                        timer.schedule(timerTask, 4000, 4000);
                    }
                });
            }
        });

        if (mViewModel.slideshowList.getValue() == null) {
            mViewModel.getSlideshowList();
        }

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
                    if (mViewModel.getVp2Index() == adapter.getItemCount() - 1) {
                        setCheckCircle(0);
                    } else if (mViewModel.getVp2Index() == 0) {
                        setCheckCircle(adapter.getItemCount() - 2);
                    } else {
                        setCheckCircle(mViewModel.getVp2Index() - 1);
                    }
                }
            }
        });

        binding.recyclerMusicFragment.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        final RecyclerSimpleAdapter simpleAdapter = new RecyclerSimpleAdapter(ScreenUtils.Companion.getWidth(requireActivity()),
                new RecyclerSimpleAdapter.OnItemClick() {
                    @Override
                    public void onClick(@NotNull Album album) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", album.getTitle());
                        bundle.putString("imagePath", album.getImage());
                        bundle.putInt("apiKind", 4);
                        bundle.putInt("type", Integer.parseInt(album.getAlbumId()));
                        Navigation.findNavController(requireActivity(), R.id.fragment)
                                .navigate(R.id.action_musicFragment_to_musicListFragment, bundle);
                        mainViewModel.setShouldTranslate(true);
                    }
                });
        simpleAdapter.setListener(new RecyclerSimpleAdapter.OnErrorClickCallBack() {
            @Override
            public void onErrorClick() {
                mViewModel.loadMore(requireContext(), simpleAdapter.getState());
            }
        });
        binding.recyclerMusicFragment.setAdapter(simpleAdapter);

        mViewModel.list.observe(getViewLifecycleOwner(), new Observer<ArrayList<Album>>() {
            @Override
            public void onChanged(ArrayList<Album> albums) {
                simpleAdapter.submitList(albums, new Runnable() {
                    @Override
                    public void run() {
                        binding.srlMusic.setRefreshing(false);
                    }
                });
            }
        });

        binding.srlMusic.setProgressViewOffset(true, 0, (int) (ScreenUtils.Companion.getHeight(requireActivity()) * 0.2));
        binding.srlMusic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.resetGetList(requireContext());
            }
        });

        if (mViewModel.list.getValue() == null) {
            binding.srlMusic.setRefreshing(true);
            mViewModel.resetGetList(requireContext());
        }

        binding.card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_musicFragment_to_singerFragment);
                mainViewModel.setShouldTranslate(true);
            }
        });

        binding.card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("title", "排行榜");
                navController.navigate(R.id.action_musicFragment_to_rankFragment, bundle);
                mainViewModel.setShouldTranslate(true);
            }
        });

        binding.card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                bundle.putString("title", "歌单");
                navController.navigate(R.id.action_musicFragment_to_rankFragment, bundle);
                mainViewModel.setShouldTranslate(true);
            }
        });

        mViewModel.position.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.vp2Image.setCurrentItem(integer, true);
            }
        });

        mViewModel.footerState.observe(getViewLifecycleOwner(), new Observer<RecyclerSimpleAdapter.State>() {
            @Override
            public void onChanged(RecyclerSimpleAdapter.State state) {
                simpleAdapter.setState(state);
                simpleAdapter.notifyItemChanged(simpleAdapter.getItemCount() - 1);
                if (state == RecyclerSimpleAdapter.State.Error){
                    binding.srlMusic.setRefreshing(false);
                    mViewModel.setLoading(false);
                }
            }
        });

//        binding.appbar.addOnOffsetChangedListener(new AppbarStateChangedListener() {
//            @Override
//            public void onStateChanged(@Nullable AppBarLayout appbar, @NotNull State state) {
//                if (state == State.EXPANDED) {
//                    System.out.println("==============================state change mViewModel.isRefreshEnable(): " + (mViewModel.isRefreshEnable()));
//                    mViewModel.setRefreshEnable(true);
//                } else {
//                    mViewModel.setRefreshEnable(false);
//                }
//            }
//        });

        binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY){
                    if (binding.srlMusic.isRefreshing()) {
                        mViewModel.changeFooterState(RecyclerSimpleAdapter.State.Gone);
                        return;
                    }
                    if (scrollY <= v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() && scrollY >= v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() -20){
                        mViewModel.loadMore(requireContext(), simpleAdapter.getState());
                    }
                }

                if (scrollY == 0){
                    binding.srlMusic.setEnabled(true);
                }else {
                    if (binding.srlMusic.isRefreshing()) return;
                    binding.srlMusic.setEnabled(false);
                }
            }
        });

    }

    @Override
    protected void toMusicList(@NonNull Bundle bundle) {

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
