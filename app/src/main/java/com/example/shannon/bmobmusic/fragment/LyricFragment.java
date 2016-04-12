package com.example.shannon.bmobmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.ui.PlayActivity;
import com.example.shannon.bmobmusic.view.LrcView;

/**
 * Created by Shannon on 2016/2/8.
 */
public class LyricFragment extends Fragment {


    private LrcView fl_lrc;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lyric,null);
        fl_lrc = (LrcView) view.findViewById(R.id.fl_lrc);
        PlayActivity mp = (PlayActivity)getActivity();
        mp.showLrc();



        return view;
    }


    public void setFl_lrc(String path){

        if(fl_lrc!=null){

            fl_lrc.setLrcPath(path);
        }

    }

    public void changeCurrent(long time){

        if(fl_lrc!=null){

            fl_lrc.changeCurrent(time);
        }

    }


















}
