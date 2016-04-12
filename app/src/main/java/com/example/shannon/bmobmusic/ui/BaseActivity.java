package com.example.shannon.bmobmusic.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Shannon on 2016/3/18.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{


    public abstract void initView();
    public abstract void loadData();
    public abstract void setListener();
    private static final String BA_LOG = "BASE_ACTIVITY";
    private ProgressDialog pd;

    private Toast toast;



    public void showProgressDialog(){


        if(pd == null){

            pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setMessage("正在加载");

        }


        pd.show();


    }


    public void dismissProgressDialog(){

        if(pd != null){

            pd.dismiss();
        }

    }


    public void showToast(String content) {

        if(toast == null){

            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        }

        if(content!=null){
            toast.setText(content);
            toast.show();

        }





    }




    /**
     *
     *
     * @param tag  该tag可以为空
     * @param content
     */
    public void log(String tag , String content){

        if(tag == null){


            Log.i(BA_LOG, content);

        }else{

            Log.i(tag, content);
        }




    }


}
