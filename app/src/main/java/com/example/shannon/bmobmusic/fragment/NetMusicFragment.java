package com.example.shannon.bmobmusic.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.Music;
import com.example.shannon.bmobmusic.bean.MusicCache;
import com.example.shannon.bmobmusic.service.PlayService;
import com.example.shannon.bmobmusic.ui.MainActivity;
import com.example.shannon.bmobmusic.utils.MediaUtils;
import com.example.shannon.bmobmusic.utils.NetUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class NetMusicFragment extends Fragment implements View.OnClickListener{



    private ListView fnm_lv_music_info;
    private TextView fnm_tv_type,fnm_tv_date;
    private ArrayList<Music> al_music = new ArrayList<>();
    private PlayService ps;
    private int startY , endY , marginTop;
    private PopupWindow pw;
    private MyMusicAdapter ma = new MyMusicAdapter();



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity ma = (MainActivity) getActivity();
        final TableLayout tl = ma.getVs_tl_container();
        tl.measure(0,0);
        final int height = tl.getMeasuredHeight();

        fnm_lv_music_info.setOnTouchListener(new View.OnTouchListener() {
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






    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_net_music , null);
        fnm_lv_music_info = (ListView) view.findViewById(R.id.fnm_lv_music_info);
        fnm_lv_music_info.setAdapter(ma);
        fnm_tv_type = (TextView) view.findViewById(R.id.fnm_tv_type);
        fnm_tv_type.setTag(MediaUtils.TYPE_BSB);
        fnm_tv_date = (TextView) view.findViewById(R.id.fnm_tv_date);

        fnm_tv_date.setText(MediaUtils.timeFormat(System.currentTimeMillis()) + "更新");
        loadData(MediaUtils.TYPE_BSB);


        fnm_tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });

        fnm_lv_music_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainActivity ma = (MainActivity) getActivity();
                ps = ma.getPs();

                if(ps!=null && al_music!=null){

                    PlayService.al_music = al_music;
                    ps.play(position);

                }


            }
        });



        return view;
    }

    private void loadData(String type) {

        System.out.println("当前网络是否可用：" + NetUtils.checkNet(getContext()));

        if(type!=null){

            //首先从本地加载榜单
            MusicCache mc = MediaUtils.getNetMusicFromLocal(getContext() , type);
            if(mc !=null){

                String cdate = MediaUtils.timeFormat(System.currentTimeMillis());

                if(mc.getDateSaved().equals(cdate)){

                    //如果是最新的
                    System.out.println("榜单为最新不需要从网络获取");
                    al_music.clear();
                    al_music.addAll(MediaUtils.jsonToList(mc.getJsonMusic()));
                    ma.notifyDataSetChanged();

                    return;

                }


            }


            //如果网络可用则执行网络更新操作
            if(NetUtils.checkNet(getContext())){

                new AsyncTask<String , Integer , MusicCache>(){


                    @Override
                    protected MusicCache doInBackground(String... params) {


                        return MediaUtils.getNetMusicFromNet(params[0]);

                    }


                    @Override
                    protected void onPostExecute(MusicCache musicCache) {
                        super.onPostExecute(musicCache);

                        if(musicCache != null){
                            //网络音乐加载成功
                            //System.out.println("榜单信息：" + musicCache);
                            Toast.makeText(getContext(),"歌单加载完成",Toast.LENGTH_SHORT).show();
                            al_music.clear();
                            al_music.addAll(MediaUtils.jsonToList(musicCache.getJsonMusic()));
                            ma.notifyDataSetChanged();
                            //缓存
                            MediaUtils.saveNetMusicToLocal(getContext(),musicCache);
                        }else{

                            Toast.makeText(getContext(),"歌单加载失败",Toast.LENGTH_SHORT).show();
                        }



                    }
                }.execute(type);


            }



        }









    }


    private void showPopupWindow(View v){

        TextView tv_bsb,tv_xgb,tv_rgb;
        View view = getActivity().getLayoutInflater().inflate(R.layout.view_type,null);
        tv_bsb = (TextView) view.findViewById(R.id.vt_tv_bsb);
        tv_xgb = (TextView) view.findViewById(R.id.vt_tv_xgb);
        tv_rgb = (TextView) view.findViewById(R.id.vt_tv_rgb);
        tv_bsb.setOnClickListener(this);
        tv_xgb.setOnClickListener(this);
        tv_rgb.setOnClickListener(this);

        pw = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        pw.setTouchable(true);
        pw.setBackgroundDrawable(getResources().getDrawable(R.color.bg_lv));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        pw.showAsDropDown(v);




    }

    @Override
    public void onClick(View v) {

        String tag = (String) fnm_tv_type.getTag();
        switch (v.getId()){

            case R.id.vt_tv_bsb:

                //如果和当前的类型不同
                if(!tag.equals(MediaUtils.TYPE_BSB)){

                    fnm_tv_type.setText("飙升榜");
                    fnm_tv_type.setTag(MediaUtils.TYPE_BSB);
                    loadData(MediaUtils.TYPE_BSB);


                }



                break;

            case R.id.vt_tv_xgb:

                if(!tag.equals(MediaUtils.TYPE_XGB)){

                    fnm_tv_type.setText("新歌榜");
                    fnm_tv_type.setTag(MediaUtils.TYPE_XGB);
                    loadData(MediaUtils.TYPE_XGB);

                }



                break;

            case R.id.vt_tv_rgb:

                if(!tag.equals(MediaUtils.TYPE_RGB)){

                    fnm_tv_type.setText("热歌榜");
                    fnm_tv_type.setTag(MediaUtils.TYPE_RGB);
                    loadData(MediaUtils.TYPE_RGB);

                }

                break;


        }

        pw.dismiss();

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


            /*
            String uri = Music.getImgUri(m.getId(),m.getId_album());
            ImageLoader.getInstance().displayImage(uri,vh.vim_iv_image);
            //il.displayImage(getImgUri(m.getId(),m.getId_album()),vh.vim_iv_image);*/



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
