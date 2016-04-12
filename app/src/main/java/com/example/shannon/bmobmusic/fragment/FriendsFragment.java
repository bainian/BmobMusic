package com.example.shannon.bmobmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.Music;
import com.example.shannon.bmobmusic.download.DownloadInfo;
import com.example.shannon.bmobmusic.download.DownloadManager;
import com.example.shannon.bmobmusic.download.DownloadState;
import com.example.shannon.bmobmusic.download.DownloadViewHolder;
import com.example.shannon.bmobmusic.service.PlayService;
import com.example.shannon.bmobmusic.ui.MainActivity;
import com.example.shannon.bmobmusic.utils.MediaUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;


public class FriendsFragment extends Fragment {






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("friendsfragment创建");
        View view = inflater.inflate(R.layout.fragment_friends , null);


        return view;
    }









}
