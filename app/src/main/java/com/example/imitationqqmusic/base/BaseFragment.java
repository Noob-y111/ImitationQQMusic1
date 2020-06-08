package com.example.imitationqqmusic.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.model.tools.ScreenUtils;

abstract public class BaseFragment extends Fragment {

    protected Toolbar toolbar;
    protected TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(getToolBarId());
        initToolBar();
        setToolbar();
        onCreateOptionMenuOnToolBar(toolbar);
        setToolbarListener(toolbar);
        initView();
    }


    @NonNull
    abstract protected View setRootView();
    abstract protected void initView();
    protected void toMusicList(@NonNull Bundle bundle){

    }
    abstract protected int getToolBarId();
    @NonNull
    abstract protected String getTitle();

    protected void setStatusBar(){
        setStatusWithConfig(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    protected void setStatusWithConfig(int config){
        int localConfig = localConfig = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            localConfig = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    config;
        }
        View view = requireActivity().getWindow().getDecorView();
        view.setSystemUiVisibility(localConfig);
        view.setFitsSystemWindows(true);
    }

    final protected int getStatusBarHeight(){
        int resourceId = requireActivity()
                .getApplicationContext()
                .getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        int height = 0;
        if (resourceId > 0){
            height = requireActivity().getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }else {
            height = 63;
        }
        return height;
    }

    protected void setTextViewSearchWidth(View view){
        textView = view.findViewById(R.id.tv_search);
        if (textView == null) return;
        textView.getLayoutParams().width = (int) (ScreenUtils.Companion.getWidth(requireActivity()) * 0.4);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setToolbar(){
        toolbar.setTitle(getTitle());
    }

    private void initToolBar(){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBackground(requireActivity().getDrawable(R.color.colorPrimary));
        }else {

        }
    }

    protected  void onCreateOptionMenuOnToolBar(Toolbar toolbar){
        if (toolbar == null) return;
    }


    private void setToolbarListener(Toolbar toolbar){
        if (toolbar == null){
            return;
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onToolBarOptionMenuSelected(item);
                return true;
            }
        });
    }

    protected void onToolBarOptionMenuSelected(MenuItem menuItem){

    }

    protected void setTransparentStatusBar(int alpha, int color){
        requireActivity().getWindow().setStatusBarColor(
                Color.argb(alpha,
                        Color.red(color),
                        Color.green(color),
                        Color.blue(color))
        );
    }
}
