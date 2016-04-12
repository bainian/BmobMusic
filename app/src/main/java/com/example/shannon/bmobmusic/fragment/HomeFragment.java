package com.example.shannon.bmobmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.Music;
import com.example.shannon.bmobmusic.service.PlayService;
import com.example.shannon.bmobmusic.ui.MainActivity;
import com.example.shannon.bmobmusic.utils.MediaUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class HomeFragment extends Fragment {



    private ListView fh_lv_music_info;
    private ArrayList<Music> al_music = new ArrayList<>();
    private int startY , endY , marginTop;
    private PlayService ps;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        MainActivity ma = (MainActivity) getActivity();
        final TableLayout tl = ma.getVs_tl_container();
        tl.measure(0,0);
        final int height = tl.getMeasuredHeight();





        //System.out.println("得到的高度：" + height);
        //View view = new View(ma);
        //AbsListView.LayoutParams la = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,);
        fh_lv_music_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:

                        startY = (int) event.getY();
                        //System.out.println("startY:" + startY);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        endY = (int) event.getY();
                        //System.out.println("startY , endY:" + startY + "," + endY);
                        int delta = endY-startY;
                        //System.out.println("差值：" + delta);
                        marginTop += delta;
                        if(marginTop>0){

                            marginTop = 0;

                        }else if(marginTop < -height){

                            marginTop = -height;
                        }



                        RelativeLayout.LayoutParams la = (RelativeLayout.LayoutParams) tl.getLayoutParams();
                        la.setMargins(0,marginTop,0,0);
                        tl.setLayoutParams(la);





                        break;

                    case MotionEvent.ACTION_UP:

                        break;

                }


                return false;
            }
        });


        //设置item的点击事件
        fh_lv_music_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                MainActivity ma = (MainActivity) getActivity();
                ps = ma.getPs();

                if(ps!=null && al_music!=null){
                    PlayService.al_music = al_music;
                    //System.out.println("开始播放");
                    ps.play(position);

                }



            }
        });




    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //初始化本地音乐信息
        al_music = MediaUtils.getMusicFromLocal(context);
        PlayService.al_music = al_music;


    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home , null);
        fh_lv_music_info = (ListView) view.findViewById(R.id.fh_lv_music_info);
        fh_lv_music_info.setAdapter(new MyMusicAdapter());

        return view;
    }

    private class MyMusicAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            if(al_music !=null){

                return al_music.size();
            }

            return 0;

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //调用该方法证明集合不为空
            ViewHolder vh;
            Music m = al_music.get(position);

            if(convertView == null){

                vh = new ViewHolder();
                convertView = View.inflate(getActivity(),R.layout.view_item_music,null);
                vh.vim_iv_image = (ImageView) convertView.findViewById(R.id.vim_iv_image);
                vh.vim_iv_more = (ImageView) convertView.findViewById(R.id.vim_iv_more);
                vh.vim_tv_title = (TextView) convertView.findViewById(R.id.vim_tv_title);
                vh.vim_tv_artist = (TextView) convertView.findViewById(R.id.vim_tv_artist);

                convertView.setTag(vh);


            }else{

                vh = (ViewHolder) convertView.getTag();

            }



            String title = m.getTitle();
            String artist = m.getArtist();


            String uri = Music.getImgUri(m.getId(),m.getId_album());

            ImageLoader.getInstance().displayImage(uri,vh.vim_iv_image, MyApplication.dio);

            //il.displayImage(getImgUri(m.getId(),m.getId_album()),vh.vim_iv_image);



            if(title == null){

                vh.vim_tv_title.setText("未知歌曲");

            }else{

                vh.vim_tv_title.setText(title);

            }

            if(artist == null){

                vh.vim_tv_artist.setText("未知歌手");

            }else{

                vh.vim_tv_artist.setText(artist);

            }


            return convertView;
        }

        class ViewHolder{

            ImageView vim_iv_image,vim_iv_more;
            TextView vim_tv_title,vim_tv_artist;


        }


    }





}
