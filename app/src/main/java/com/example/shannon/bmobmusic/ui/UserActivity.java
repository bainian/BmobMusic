package com.example.shannon.bmobmusic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.Account;
import com.nostra13.universalimageloader.core.ImageLoader;


public class UserActivity extends BaseActivity {


    private Toolbar tl_custom;
    private ImageView au_iv_img;
    private TextView au_tv_username,au_tv_name,au_tv_sex,au_tv_title,au_tv_describle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initView();
        loadData();
        setListener();



    }

    @Override
    public void initView() {

        System.out.println("toolbar是否为空：" + tl_custom);
        tl_custom = (Toolbar) findViewById(R.id.tl_custom);
        au_iv_img = (ImageView) findViewById(R.id.au_iv_img);
        au_tv_username = (TextView) findViewById(R.id.au_tv_username);
        au_tv_name = (TextView) findViewById(R.id.au_tv_name);
        au_tv_sex = (TextView) findViewById(R.id.au_tv_sex);
        au_tv_title = (TextView) findViewById(R.id.au_tv_title);
        au_tv_describle = (TextView) findViewById(R.id.au_tv_describle);


    }

    @Override
    public void loadData() {

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



        Account account = MyApplication.account;
        if(account!=null){
            String username = account.getUsername();
            String name = account.getName();
            String title = account.getTitle();
            String url_img = account.getUrl_img();
            String describle = account.getDescrible();
            Integer sex = account.getSex();

            au_tv_username.setText(username);

            if(!TextUtils.isEmpty(url_img)){

                ImageLoader.getInstance().displayImage(url_img,au_iv_img);

            }


            if(!TextUtils.isEmpty(name)){

                au_tv_name.setText(name);

            }

            if(!TextUtils.isEmpty(title)){

                au_tv_title.setText(title);

            }

            if(!TextUtils.isEmpty(describle)){

                au_tv_describle.setText(describle);


            }

            switch(sex){

                case 0:
                    au_tv_sex.setText("男");
                    break;

                case 1:
                    au_tv_sex.setText("女");
                    break;

                default:
                    au_tv_sex.setText("未设置");
                    break;

            }









        }





    }

    @Override
    public void setListener() {

        au_iv_img.setOnClickListener(this);
        au_tv_username.setOnClickListener(this);
        au_tv_name.setOnClickListener(this);
        au_tv_sex.setOnClickListener(this);
        au_tv_title.setOnClickListener(this);
        au_tv_describle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){


            case R.id.au_iv_img:

                break;

            case R.id.au_tv_username:

                break;

            case R.id.au_tv_name:

                break;

            case R.id.au_tv_sex:

                break;

            case R.id.au_tv_title:

                break;

            case R.id.au_tv_describle:

                break;



        }


    }
}
