package com.example.imitationqqmusic.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setRootViewByBinding());
        setToolbar();
        initView();
    }

    abstract protected View setRootViewByBinding();
    abstract protected void initView();
    abstract protected void setToolbar();
}
