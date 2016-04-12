package com.example.shannon.bmobmusic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.shannon.bmobmusic.R;

public class FeedbackActivity extends BaseActivity {

    private Toolbar tl_custom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
        loadData();
        setListener();
    }

    @Override
    public void initView() {
        tl_custom = (Toolbar) findViewById(R.id.tl_custom);
        setSupportActionBar(tl_custom);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        //tl_custom.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);

        tl_custom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
