package com.example.shannon.bmobmusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.Account;
import com.example.shannon.bmobmusic.utils.SpUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends BaseActivity {


    private Button al_btn_login;
    private EditText al_et_username,al_et_password;
    private TextView al_tv_forgetp,al_tv_regist;
    private Toolbar tl_custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        loadData();
        setListener();

    }

    @Override
    public void initView() {

        al_btn_login = (Button) findViewById(R.id.al_btn_login);
        al_et_username = (EditText) findViewById(R.id.al_et_username);
        al_et_password = (EditText) findViewById(R.id.al_et_password);
        al_tv_forgetp = (TextView) findViewById(R.id.al_tv_forgetp);
        al_tv_regist = (TextView) findViewById(R.id.al_tv_regist);
        tl_custom = (Toolbar) findViewById(R.id.tl_custom);

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

    }

    @Override
    public void setListener() {

        al_btn_login.setOnClickListener(this);
        al_tv_forgetp.setOnClickListener(this);
        al_tv_regist.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.al_btn_login:
                //登录操作
                final String username = al_et_username.getText().toString().trim();
                final String password = al_et_password.getText().toString().trim();

                boolean isUnEmpty = TextUtils.isEmpty(username);
                boolean isPwEmpty = TextUtils.isEmpty(password);

                if(isUnEmpty || isPwEmpty){

                    if(isUnEmpty){

                        al_et_username.setHintTextColor(getResources().getColor(R.color.colorAccent));
                        al_et_username.setHint("*请输入帐号");

                    }

                    if(isPwEmpty){

                        al_et_password.setHintTextColor(getResources().getColor(R.color.colorAccent));
                        al_et_password.setHint("*请输入密码");

                    }


                }else{


                    showProgressDialog();

                    BmobUser.loginByAccount(LoginActivity.this, username, password, new LogInListener<Account>() {


                        @Override
                        public void done(Account account, BmobException e) {


                            if(account!=null){


                                SpUtils.saveStringContent(LoginActivity.this , Account.SP_USERNAME,username);
                                SpUtils.saveStringContent(LoginActivity.this , Account.SP_PASSWORD,password);
                                MyApplication.account = account;
                                MyApplication.isComfirm = true;
                                Account.saveAccountToLocal(account);
                                finish();
                                dismissProgressDialog();
                                showToast("登录成功");
                            }else{

                                showToast("帐号或密码错误");

                            }

                        }
                    });




                }




                break;

            case R.id.al_tv_forgetp:

                break;

            case R.id.al_tv_regist:
                startActivity(new Intent(this , RegistActivity.class));
                finish();
                break;


        }

    }
}
