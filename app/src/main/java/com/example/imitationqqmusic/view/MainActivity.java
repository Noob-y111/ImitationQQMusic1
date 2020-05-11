package com.example.imitationqqmusic.view;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.base.BaseActivity;
import com.example.imitationqqmusic.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected View setRootViewByBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        initNavigation();
    }

    @Override
    protected void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void initNavigation() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        view.setFitsSystemWindows(true);
        NavController navController = ((NavHostFragment) Objects
                .requireNonNull(getSupportFragmentManager()
                        .findFragmentById(R.id.fragment))).getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(binding.navigation.getMenu())
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navigation, navController);
    }
}
