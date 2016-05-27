package com.example.shannon.bmobmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.emokit.sdk.InitListener;
import com.emokit.sdk.senseface.ExpressionDetect;
import com.emokit.sdk.senseface.ExpressionListener;
import com.emokit.sdk.util.SDKAppInit;
import com.emokit.sdk.util.SDKConstant;
import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.EmojiResult;
import com.example.shannon.bmobmusic.bean.Music;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class FriendsFragment extends Fragment {




    private Button ff_btn_test;
    private ListView ff_lv_recommened;
    private ArrayList<Music> al_music = new ArrayList<>();
    private MyMusicAdapter ma = new MyMusicAdapter();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("friendsfragment创建");
        View view = inflater.inflate(R.layout.fragment_friends , null);
        ff_btn_test = (Button) view.findViewById(R.id.ff_btn_test);
        ff_lv_recommened = (ListView) view.findViewById(R.id.ff_lv_recommened);
        ma = new MyMusicAdapter();
        ff_lv_recommened.setAdapter(ma);

        //初始化情绪识别
        SDKAppInit.createInstance(getActivity());

        final ExpressionListener expressionListener = new ExpressionListener() {
            @Override
            public void beginDetect() {

                System.out.println("开始测试");

            }

            @Override
            public void endDetect(String s) {
                System.out.println("结束测试");
                System.out.println("测试结果：" + s);
                Gson gson = new Gson();
                EmojiResult emojiResult = gson.fromJson(s, EmojiResult.class);
                System.out.println(emojiResult.toString());

                BmobQuery<Music> collection = new BmobQuery<>();
                collection.addWhereEqualTo("type",0);
                collection.setLimit(10);
                collection.findObjects(getActivity(), new FindListener<Music>() {
                    @Override
                    public void onSuccess(List<Music> list) {
                        System.out.println("推荐的音乐：" + list.toString());
                        al_music.clear();
                        al_music.addAll(list);
                        ma.notifyDataSetChanged();


                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });




            }
        };

        InitListener initListener = new InitListener(){

            @Override
            public void onInit(int i) {
                SDKAppInit.registerforuid("BmobMusic","shannonc@163.com","w2133707703");
            }
        };


        final ExpressionDetect expressionDetect = ExpressionDetect.createRecognizer(getContext(),initListener);


        expressionDetect.setParameter(SDKConstant.FACING,
                SDKConstant.CAMERA_FACING_FRONT);



        ff_btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                expressionDetect.startRateListening(expressionListener);




            }
        });


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
