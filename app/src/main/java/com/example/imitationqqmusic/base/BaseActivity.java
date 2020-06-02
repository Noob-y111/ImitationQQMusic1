package com.example.imitationqqmusic.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.imitationqqmusic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setRootViewByBinding());
        initNavigationAndWindow(getNavControllerId(), getBottomNavigationView());
        initView();
    }

    abstract protected View setRootViewByBinding();
    abstract protected void initView();
    abstract protected int getNavControllerId();
    abstract protected BottomNavigationView getBottomNavigationView();

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
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(localConfig);
        view.setFitsSystemWindows(true);
    }

    protected void initNavigationAndWindow(int navControllerId, BottomNavigationView navigation){
        setStatusBar();
        NavController navController = ((NavHostFragment) Objects
                .requireNonNull(getSupportFragmentManager()
                        .findFragmentById(navControllerId))).getNavController();
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
//                .Builder(navigation.getMenu())
//                .build();

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, navController);
    }

    protected void setTransparentStatusBar(int alpha, int color){
        getWindow().setStatusBarColor(
                Color.argb(alpha,
                        Color.red(color),
                        Color.green(color),
                        Color.blue(color))
        );
    }

    final public int getStatusBarHeight(){
        int resourceId = this
                .getApplicationContext()
                .getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        int height = 0;
        if (resourceId > 0){
            height = this.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }else {
            height = 63;
        }
        return height;
    }
}
