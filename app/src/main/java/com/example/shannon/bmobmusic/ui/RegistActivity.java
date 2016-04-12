package com.example.shannon.bmobmusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.Account;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Shannon on 2016/3/18.
 */
public class RegistActivity extends BaseActivity{

    private Toolbar tl_custom;
    private EditText ar_et_username,ar_et_email,ar_et_password,ar_et_passwordc;
    private TextView ar_tv_signup;
    private Button ar_btn_regist;
    private RelativeLayout ar_rl_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
        loadData();
        setListener();
    }

    @Override
    public void initView() {
        ar_et_username = (EditText) findViewById(R.id.ar_et_username);
        ar_et_email = (EditText) findViewById(R.id.ar_et_email);
        ar_et_password = (EditText) findViewById(R.id.ar_et_password);
        ar_et_passwordc = (EditText) findViewById(R.id.ar_et_passwordc);

        ar_tv_signup = (TextView) findViewById(R.id.ar_tv_signup);

        ar_btn_regist = (Button) findViewById(R.id.ar_btn_regist);

        ar_rl_container = (RelativeLayout) findViewById(R.id.ar_rl_container);

        tl_custom = (Toolbar) findViewById(R.id.tl_custom);

    }

    @Override
    public void loadData() {

        setSupportActionBar(tl_custom);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        tl_custom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void setListener() {

        ar_tv_signup.setOnClickListener(this);
        ar_btn_regist.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {


        switch(v.getId()){

            case R.id.ar_tv_signup:

                break;

            case R.id.ar_btn_regist:


                regist();


                break;



        }

    }

    private void regist() {


        String username = ar_et_username.getText().toString().trim();
        String email = ar_et_email.getText().toString().trim();
        String password = ar_et_password.getText().toString().trim();
        String passwordc = ar_et_passwordc.getText().toString().trim();

        boolean unIsEmpty = TextUtils.isEmpty(username);
        boolean emailIsEmpty = TextUtils.isEmpty(email);
        boolean pasIsEmpty = TextUtils.isEmpty(password);
        boolean pascIsEmpty = TextUtils.isEmpty(passwordc);

        if(unIsEmpty || emailIsEmpty || pasIsEmpty || pascIsEmpty){

            if(TextUtils.isEmpty(username)){

                ar_et_username.setHintTextColor(getResources().getColor(R.color.colorAccent));
                ar_et_username.setHint("*请输入帐号");
            }


            if(TextUtils.isEmpty(email)){

                ar_et_email.setHintTextColor(getResources().getColor(R.color.colorAccent));
                ar_et_email.setHint("*请输入邮箱");

            }


            if(TextUtils.isEmpty(password)){

                ar_et_password.setHintTextColor(getResources().getColor(R.color.colorAccent));
                ar_et_password.setHint("*请输入密码");

            }

            if(TextUtils.isEmpty(passwordc)){

                ar_et_passwordc.setHintTextColor(getResources().getColor(R.color.colorAccent));
                ar_et_passwordc.setHint("*请输入确认密码");

            }


        }else{


            if(password.equals(passwordc)){


                if(MyApplication.account == null){

                    MyApplication.account = new Account.Builder().build();
                }

                final Account account = MyApplication.account;
                account.setUsername(username);
                account.setPassword(password);
                account.setEmail(email);

                showProgressDialog();
                account.signUp(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("注册成功");
                        MyApplication.isComfirm = true;
                        Account.saveAccountToLocal(account);
                        startActivity(new Intent(RegistActivity.this , MainActivity.class));
                        finish();
                        dismissProgressDialog();

                        showToast("注册成功");

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        dismissProgressDialog();
                        System.out.println("注册失败：" + s);
                        showToast("注册失败：" + s);
                    }
                });




            }else{

                ar_et_passwordc.setHintTextColor(getResources().getColor(R.color.colorAccent));
                ar_et_passwordc.setHint("*两次输入的密码不相同");

            }




        }











    }
}
