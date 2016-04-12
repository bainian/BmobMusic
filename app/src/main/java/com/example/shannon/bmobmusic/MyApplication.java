package com.example.shannon.bmobmusic;

import android.app.Application;

import com.example.shannon.bmobmusic.bean.Account;
import com.example.shannon.bmobmusic.bean.Emoji;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.xutils.x;
import cn.bmob.v3.Bmob;

/**
 * Created by Shannon on 2016/3/20.
 */
public class MyApplication extends Application{

    public static Account account;
    public static boolean isComfirm = false;
    public static Emoji emoji = new Emoji();

    public static final DisplayImageOptions dio = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.missing_album_artwork_generic_proxy)
            .showImageOnFail(R.mipmap.missing_album_artwork_generic_proxy)
            .showImageOnLoading(R.mipmap.missing_album_artwork_generic_proxy).build();;
    //public static ArrayList<Music> al_music;
    //public static PlayState playState = new PlayState();



    @Override
    public void onCreate() {
        super.onCreate();

        init();



    }

    private void init() {

        Bmob.initialize(getApplicationContext(),getBmobKeyFromC());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .build();

        ImageLoader.getInstance().init(config);


        //初始化xutils
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);





    }

    private native String getBmobKeyFromC();

    static{

        System.loadLibrary("util");

    }


}
