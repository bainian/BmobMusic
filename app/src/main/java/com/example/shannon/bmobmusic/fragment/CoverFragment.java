package com.example.shannon.bmobmusic.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.Music;
import com.example.shannon.bmobmusic.bean.PlayState;
import com.example.shannon.bmobmusic.service.PlayService;
import com.example.shannon.bmobmusic.ui.PlayActivity;
import com.example.shannon.bmobmusic.utils.AnimationUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by Shannon on 2016/2/8.
 */
public class CoverFragment extends Fragment {

    private ImageView fc_iv_cover;
    private PlayActivity pa;
    private AlphaAnimation aa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //初始化
        View view = inflater.inflate(R.layout.fragment_cover,null);
        aa = AnimationUtils.getAa();
        fc_iv_cover = (ImageView) view.findViewById(R.id.fc_iv_cover);
        fc_iv_cover.setAnimation(aa);
        Music m = PlayState.getCurrentMusic(PlayService.al_music , PlayService.playState.getCurrentPosition());
        if(m!=null){

            //初始化加载图片
            aa.start();
            setCover(m);


        }

        return view;

    }

    public void setCover(Bitmap bitmap){

        if(fc_iv_cover!=null){

            aa.start();
            fc_iv_cover.setImageBitmap(bitmap);
        }

    }

    public void setCover(Music m){



        if(fc_iv_cover!=null){

            String path = null;
            if(m.getPath_img()!=null){

                path = m.getPath_img();

            }else{

                path = Music.getImgUri(m.getId() , m.getId_album());
            }
            aa.start();
            ImageLoader.getInstance().displayImage(path,fc_iv_cover, MyApplication.dio);

        }

    }






}
