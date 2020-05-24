package com.example.imitationqqmusic.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.imitationqqmusic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

abstract public class BaseFragment extends Fragment {

    protected Toolbar toolbar;

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
    protected void toMusicDetail(@NonNull Bundle bundle){

    }
    abstract protected int getToolBarId();
    @NonNull
    abstract protected String getTitle();

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
