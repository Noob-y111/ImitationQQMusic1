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

    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setRootViewByBinding());
        toolbar = findViewById(R.id.toolbar);
        setToolbar();
        initNavigationAndWindow(getNavControllerId(), getBottomNavigationView());
        initView();
    }

    abstract protected View setRootViewByBinding();
    abstract protected void initView();
    abstract protected void setToolbar();
    abstract protected int getNavControllerId();
    abstract protected BottomNavigationView getBottomNavigationView();

    protected void initNavigationAndWindow(int navControllerId, BottomNavigationView navigation){
        View view = getWindow().getDecorView();
        int config;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            config = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            config = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBackground(getDrawable(R.color.colorPrimary));
        }

        view.setSystemUiVisibility(config);
        view.setFitsSystemWindows(true);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        NavController navController = ((NavHostFragment) Objects
                .requireNonNull(getSupportFragmentManager()
                        .findFragmentById(navControllerId))).getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(navigation.getMenu())
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, navController);
    }
}
