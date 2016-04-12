package com.example.shannon.bmobmusic.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.shannon.bmobmusic.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        loadData();
        setListener();
        startActivity(new Intent(this , MainActivity.class));
        finish();
    }

    @Override
    public void initView() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
