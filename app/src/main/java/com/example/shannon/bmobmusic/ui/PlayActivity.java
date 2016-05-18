package com.example.shannon.bmobmusic.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.adapter.MyFragmentPagerAdapter;
import com.example.shannon.bmobmusic.bean.Emoji;
import com.example.shannon.bmobmusic.bean.Music;
import com.example.shannon.bmobmusic.bean.MusicCollection;
import com.example.shannon.bmobmusic.bean.PlayState;
import com.example.shannon.bmobmusic.bean.UserAction;
import com.example.shannon.bmobmusic.download.DownloadManager;
import com.example.shannon.bmobmusic.fragment.CoverFragment;
import com.example.shannon.bmobmusic.fragment.LyricFragment;
import com.example.shannon.bmobmusic.receiver.MyReceiver;
import com.example.shannon.bmobmusic.service.PlayService;
import com.example.shannon.bmobmusic.utils.MediaUtils;
import com.example.shannon.bmobmusic.utils.NetUtils;

import org.xutils.ex.DbException;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class PlayActivity extends BaseActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{


    public static final String LRC_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/lrc/";
    public static final String DOWN_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+"/";
    public static final int PIC_DOWNLOAD= 0x3;
    public static final int LRC_DOWNLOAD = 0x4;
    private PlayService ps;
    private MyReceiver mr;
    private SeekBar amp_acs_sb;
    private TextView amp_tv_ctime,amp_tv_music_name,amp_tv_singer,amp_tv_atime;
    private ImageView amp_iv_back,amp_iv_album,amp_iv_random,amp_iv_pre,amp_iv_play,amp_iv_next,amp_iv_loop,amp_iv_download,amp_iv_emoj,amp_iv_like,amp_content,amp_iv_list;
    private ViewPager amp_vp_cover;

    private ArrayList<Fragment> al_fragment = new ArrayList<>();
    private CoverFragment cf;
    private LyricFragment lf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
        loadData();
        setListener();



    }


    public void initView() {

        amp_iv_back = (ImageView) findViewById(R.id.amp_iv_back);
        amp_iv_pre = (ImageView) findViewById(R.id.amp_iv_pre);
        amp_iv_play = (ImageView) findViewById(R.id.amp_iv_play);
        amp_iv_next = (ImageView) findViewById(R.id.amp_iv_next);

        amp_acs_sb = (SeekBar) findViewById(R.id.amp_acs_sb);

        amp_tv_music_name = (TextView) findViewById(R.id.amp_tv_music_name);
        amp_tv_singer = (TextView) findViewById(R.id.amp_tv_singer);
        amp_tv_ctime = (TextView) findViewById(R.id.amp_tv_ctime);
        amp_tv_atime = (TextView) findViewById(R.id.amp_tv_atime);


        amp_vp_cover = (ViewPager) findViewById(R.id.amp_vp_cover);

        //搜藏按钮的初始化
        amp_iv_like = (ImageView) findViewById(R.id.amp_iv_like);


        //初始化下载按钮
        amp_iv_download = (ImageView) findViewById(R.id.amp_iv_download);


        amp_content = (ImageView) findViewById(R.id.amp_content);

        amp_iv_list = (ImageView) findViewById(R.id.amp_iv_list);

        //播放模式按钮初始化
        amp_iv_random = (ImageView) findViewById(R.id.amp_iv_random);
        amp_iv_loop = (ImageView) findViewById(R.id.amp_iv_loop);


    }

    //刷新操作
    public void updateUI(){


        //获得最新的状态
        Music music = PlayState.getCurrentMusic(PlayService.al_music , PlayService.playState.getCurrentPosition());
        //如果歌曲信息不同就应该刷新歌曲信息
        String title = amp_tv_music_name.getText().toString().trim();
        String artist = amp_tv_singer.getText().toString().trim();
        //System.out.println("当前显示的信息：" + title + "**" + artist);
        //歌曲变换
        if(!title.equals(music.getTitle().trim()) || !artist.equals(music.getArtist().trim())){
            //刷新文本信息
            //System.out.println("歌曲的信息：" + music.getTitle().trim() + "**" + music.getArtist().trim());
            amp_tv_music_name.setText(music.getTitle());
            amp_tv_singer.setText(music.getArtist());

            //刷新图片信息
            if(cf!=null){

                //System.out.println("日志刷新图片");
                if(music.getPath_img()!=null){
                    //设置网络图片
                    //System.out.println("设置图片");
                    cf.setCover(music);

                }else{
                    //设置本地图片
                    //System.out.println("设置图片");
                    String uri = Music.getImgUri(music.getId() , music.getId_album());
                    cf.setCover(music);

                }


            }
            //刷新下载按钮
            setDownloadButton(music);

            //刷新搜藏按钮
            setCollectionView(music,PlayActivity.this);



        }


        int duration = amp_acs_sb.getMax();

        if(duration != PlayService.playState.getDuration()){
            //System.out.println("更新总时长");
            amp_acs_sb.setMax((int) PlayService.playState.getDuration());
            amp_tv_atime.setText(timeFormat(PlayService.playState.getDuration()));

        }



        //刷新控制按钮
        boolean isPlaying = PlayService.playState.isPlaying();
        //如果状态不一致

        if(isPlaying){

            //刷新播放的进度
            long progress = PlayService.playState.getProgress();
            amp_acs_sb.setProgress((int)progress );
            amp_tv_ctime.setText(timeFormat(progress));
            //System.out.println("刷新当前进度");
            lf.changeCurrent(progress);

            //状态不一致需刷新
            if(isPlaying != (boolean)amp_iv_play.getTag()){

                if(isPlaying){

                    amp_iv_play.setImageResource(R.mipmap.play_pause);

                }else{

                    amp_iv_play.setImageResource(R.mipmap.play_button);
                }
                //System.out.println("刷新播放按钮");

            }


        }


        int mode = PlayService.playState.getMode();

        if(mode == PlayService.MODE_ORDER){

            //如果当前的播放模式为顺序播放
            boolean mode_play = (boolean) amp_iv_loop.getTag();
            if(mode_play == false){

                //当前的模式为随机播放则需要更新状态
                amp_iv_random.setImageResource(R.mipmap.random);
                amp_iv_loop.setImageResource(R.mipmap.loop_red);

                amp_iv_random.setTag(false);
                amp_iv_loop.setTag(true);

            }

        }else{


            //如果当前的播放模式为顺序播放
            boolean mode_play = (boolean) amp_iv_random.getTag();
            if(mode_play == false){

                amp_iv_random.setImageResource(R.mipmap.random_red);
                amp_iv_loop.setImageResource(R.mipmap.loop);

                amp_iv_random.setTag(true);
                amp_iv_loop.setTag(false);

            }

        }




    }

    private String getLrcFromLocal(String artist , String title){

        String filename = artist + "-" +title + ".lrc";
        File file_lrc = new File(LRC_DIRECTORY);

        String[] lrcs = file_lrc.list();
        if(lrcs == null){

            return null;

        }else{

            for(String lrc:lrcs){

                if(lrc.equals(filename)){

                    return LRC_DIRECTORY + filename;

                }

            }

            return null;

        }






    }
    //显示歌词
    public void showLrc(){


        //获得当前歌曲的对象
        final Music mi = PlayService.al_music.get(PlayService.playState.getCurrentPosition());
        //判断当前对象是否为空
        if(mi!=null){



            String path_lrc = getLrcFromLocal(mi.getArtist(),mi.getTitle());

            if(path_lrc!=null){

                //如果本地存在歌词

                setLrc(path_lrc);


            }else{

                //如果本地不存在歌词则进行下载操作

                Runnable run_getlrc = new Runnable(){


                    @Override
                    public void run() {



                        String url = NetUtils.getLrcUrl(mi.getArtist(),mi.getTitle());

                        System.out.println("歌词的下载路径为：" + url);
                        //如果歌词存在
                        if(url!=null){

                            File file = new File(LRC_DIRECTORY);
                            //如果歌词的文件夹不存在则创建
                            if(!file.exists()){

                                file.mkdir();
                            }

                            String path = LRC_DIRECTORY + mi.getArtist().split("/")[0] + "-" + mi.getTitle().split("/")[0] +".lrc";


                            if(NetUtils.downFile(url,path)){

                                System.out.println("歌词下载完成");
                                Message message = new Message();
                                message.what = LRC_DOWNLOAD;
                                Bundle bundle = new Bundle();
                                bundle.putString("path",path);
                                message.setData(bundle);
                                handler.sendMessage(message);

                            }else{

                                System.out.println("歌词下载失败");
                            }
                        }



                    }
                };

                es.execute(run_getlrc);

            }




        }








    }

    private ExecutorService es = Executors.newSingleThreadExecutor();


    public void loadData() {






        //除此进入时获得状态的副本，并进行初始化操作
        //playState = PlayService.playState.copy();
        Music m = PlayState.getCurrentMusic(PlayService.al_music , PlayService.playState.getCurrentPosition());
        if(m!=null){

            amp_tv_music_name.setText(m.getTitle());
            amp_tv_singer.setText(m.getArtist());
            amp_acs_sb.setMax((int) PlayService.playState.getDuration());
            amp_acs_sb.setProgress((int) PlayService.playState.getProgress());

            amp_tv_atime.setText(timeFormat(PlayService.playState.getDuration()));
            amp_tv_ctime.setText(timeFormat(PlayService.playState.getProgress()));


            if(PlayService.playState.isPlaying()){

                amp_iv_play.setImageResource(R.mipmap.play_pause);
                amp_iv_play.setTag(true);

            }else{

                amp_iv_play.setImageResource(R.mipmap.play_button);
                amp_iv_play.setTag(false);

            }

        }


        //初始化播放模式
        if(PlayService.playState.getMode() == PlayService.MODE_ORDER){

            amp_iv_random.setImageResource(R.mipmap.random);
            amp_iv_loop.setImageResource(R.mipmap.loop_red);

            amp_iv_random.setTag(false);
            amp_iv_loop.setTag(true);

        }else{

            amp_iv_random.setImageResource(R.mipmap.random_red);
            amp_iv_loop.setImageResource(R.mipmap.loop_red);

            amp_iv_random.setTag(true);
            amp_iv_loop.setTag(false);

        }


        //动态注册广播
        MyReceiver mr = new MyReceiver() {
            @Override
            public void doAction(Context context, Intent intent) {
                //System.out.println("PlayActivity接收到广播");
                updateUI();

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.shannon.bmplayer.progress");
        registerReceiver(mr , intentFilter);


        //绑定服务
        bindService(new Intent(this, PlayService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                System.out.println("绑定播放服务成功'");
                PlayService.PlayBinder pb = (PlayService.PlayBinder)service;
                ps = pb.getPlayService();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                System.out.println("绑定播放服务失败'");

            }
        },Context.BIND_AUTO_CREATE);



        cf = new CoverFragment();
        lf = new LyricFragment();
        al_fragment.add(cf);
        al_fragment.add(lf);
        amp_vp_cover.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager() , al_fragment , null));


        MediaUtils.setOal(new MediaUtils.OnGetArtWorkListener() {
            @Override
            public void success() {
                System.out.println("在MusicPlayActivity中执行图片更新的操作");
                Message message = new Message();
                message.what = PIC_DOWNLOAD;
                handler.sendMessage(message);
            }

            @Override
            public void fail() {

            }
        });



        //初始化下载按钮
        setDownloadButton(m);

        //初始化搜藏按钮
        setCollectionView(m,PlayActivity.this);











    }

    private void setDownloadButton(Music m){

        //判断本地的音乐文件夹中是否有该歌曲    判断本地的音乐数据库是否有该歌曲
        if(checkFile(m)){

            //如果当前播放的音乐存在则显示下载完成的图标
            System.out.println("当前的歌曲本地存在");
            amp_iv_download.setImageResource(R.mipmap.play_icn_dlded);
            amp_iv_download.setClickable(false);


        }else{

            System.out.println("当前歌曲本地不存在");
            //当前播放的音乐不存在，则显示下载的图标
            amp_iv_download.setImageResource(R.mipmap.play_icn_dld_prs);
            amp_iv_download.setClickable(true);


        }



    }



    public void setCollectionView(Music mi, Context context) {

        //如果已经搜藏过了就不能在搜藏了
        //判断音乐是否搜藏过
        //查询用户是否搜藏过改音乐
        //首先查询音乐
        BmobQuery<Music> query = new BmobQuery<>();
        query.addWhereContains("artist", mi.getArtist()).addWhereContains("title", mi.getTitle());
        query.findObjects(context, new FindListener<Music>() {
            @Override
            public void onSuccess(List<Music> list) {
                //音乐存在则继续查询该音乐是否属于该用户

                //System.out.println("setCollectionView:音乐表中存在该音乐" + list.size());


                if (list.size() > 0) {

                    final Music m = list.get(0);

                    System.out.println("查找到的音乐信息：" + m.toString());
                    //根据音乐的Id查找搜藏的用户
                    BmobQuery<MusicCollection> bq = new BmobQuery<MusicCollection>();
                    bq.addWhereEqualTo("musicId", m.getObjectId());
                    bq.findObjects(PlayActivity.this, new FindListener<MusicCollection>() {
                        @Override
                        public void onSuccess(List<MusicCollection> list) {

                            System.out.println("查询搜藏列表成功");


                            System.out.println("当前用户的ID：" + MyApplication.account.getUsername());
                            for (MusicCollection mc : list) {


                                //System.out.println("搜藏列表中的用户ID：" + mc.getUserId());
                                //如果该用户搜藏过该音乐
                                if (mc.getUserId().equals(MyApplication.account.getUsername())) {

                                    //amp_iv_like.setImageResource(R.mipmap.play_icn_loved);

                                    System.out.println("该用户搜藏过该音乐");
                                    amp_iv_like.setImageResource(R.mipmap.play_icn_loved);
                                    amp_iv_like.setTag(true);

                                    break;


                                }


                            }


                        }

                        @Override
                        public void onError(int i, String s) {
                            amp_iv_like.setTag(false);
                            System.out.println("查询搜藏列表错误");
                        }
                    });


                }else{

                    amp_iv_like.setTag(false);


                }


            }

            @Override
            public void onError(int i, String s) {
                //音乐不存在则用户可以搜藏该音乐
                amp_iv_like.setTag(false);
                System.out.println("查询音乐信息表错误");


            }
        });

    }


    public boolean checkFile(Music mp3Info){


        String artist = mp3Info.getArtist();
        String title = mp3Info.getTitle();
        String filename = artist+"-"+title + ".mp3";
        if((MediaUtils.searchMusicFromDatabase(artist,title,this)!=null)||(MediaUtils.getFileFromLocal(filename)!=null)){

            return true;


        }else{

            return false;

        }



    }


    //设置歌词
    private void setLrc(String path){


        if(al_fragment.size()>1){

            LyricFragment lf = (LyricFragment) al_fragment.get(1);
            lf.setFl_lrc(path);


        }


    }

    //创建一个Handler用来更新界面的时间
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){


                case PIC_DOWNLOAD:

                    //amp_iv_album.setImageBitmap(MyApplication.bitmap);

                    break;


                case LRC_DOWNLOAD:
                    System.out.println("开始设置歌词");
                    String path = msg.getData().getString("path");
                    setLrc(path);
                    Toast.makeText(PlayActivity.this,path + "下载完成",Toast.LENGTH_SHORT).show();
                    break;






            }


        }
    };


    private DateFormat df;


    public String timeFormat(long time){


        if(df == null){

            df = new SimpleDateFormat("mm:ss");
        }

        return df.format(time);



    }

    public void setListener() {


        amp_iv_back.setOnClickListener(this);
        amp_iv_pre.setOnClickListener(this);
        amp_iv_play.setOnClickListener(this);
        amp_iv_next.setOnClickListener(this);
        amp_acs_sb.setOnSeekBarChangeListener(this);
        amp_iv_download.setOnClickListener(this);
        amp_iv_like.setOnClickListener(this);
        amp_content.setOnClickListener(this);
        amp_iv_list.setOnClickListener(this);


        amp_iv_random.setOnClickListener(this);
        amp_iv_loop.setOnClickListener(this);




    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.amp_iv_back:
                finish();
                break;

            //随机播放
            case R.id.amp_iv_random:

                System.out.println("设置随机播放模式");
                if(ps!=null){

                    PlayService.playState.setMode(PlayService.MODE_RANDOM);
                }

                break;

            case R.id.amp_iv_loop:
                if(ps!=null){
                    PlayService.playState.setMode(PlayService.MODE_ORDER);

                }

                break;

            //上一首
            case R.id.amp_iv_pre:

                if(ps!=null){

                    ps.pre();

                }
                break;

            case R.id.amp_iv_play:

                if(PlayService.playState.isPlaying()){
                    ps.pause();
                    PlayService.playState.setPlaying(false);
                    amp_iv_play.setImageResource(R.mipmap.play_button);
                }else{

                    ps.start();
                    PlayService.playState.setPlaying(true);
                    amp_iv_play.setImageResource(R.mipmap.play_pause);

                }

                break;

            case R.id.amp_iv_next:

                if(ps!=null){

                    ps.next();

                }
                break;


            //搜藏按钮：
            case R.id.amp_iv_like:

                actionOfCollection();

                break;

            case R.id.amp_content:
                showToast("评论功能还未实现");
                break;

            case R.id.amp_iv_list:
                showToast("展示列表还未实现");
                break;

            //下载按钮
            /**
             * 1.判断当前的是否为可以下载的状态
             * 2.如果为可下载则获取当前音乐的下载的链接
             * 3.将当前的链接添加到下载列表中
             *
             */
            case R.id.amp_iv_download:



                Toast.makeText(this , "开始下载",Toast.LENGTH_SHORT).show();
                Music m = PlayService.al_music.get(PlayService.playState.getCurrentPosition());
                if(m!=null){

                    String lable = m.getArtist() + "-" + m.getTitle();
                    try {

                        DownloadManager.getInstance().startDownload(m.getPath_music(),lable,DOWN_DIRECTORY+lable+".mp3",false,false,null);

                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                }


                setDownloadButton(m);





                break;




        }




    }

    public void actionOfCollection(){

        final Music mi = PlayService.al_music.get(PlayService.playState.getCurrentPosition());

        if(mi.getPath_music().contains("http://")){
            //当前音乐为网络歌曲

            if(amp_iv_like.getTag()!=null){


                if((boolean)amp_iv_like.getTag() == true){
                    //如果当前音乐搜藏列表中已经存在    则取消搜藏
                    System.out.println("搜藏列表中已经存在");
                    BmobQuery<Music> query = new BmobQuery<>();
                    query.addWhereContains("artist", mi.getArtist()).addWhereContains("title", mi.getTitle());
                    query.findObjects(PlayActivity.this, new FindListener<Music>() {
                        @Override
                        public void onSuccess(List<Music> list) {
                            //音乐存在则继续查询该音乐是否属于该用户

                            if (list.size() > 0) {

                                final Music m = list.get(0);

                                //根据音乐的Id查找搜藏的用户
                                BmobQuery<MusicCollection> bq = new BmobQuery<MusicCollection>();
                                bq.addWhereEqualTo("musicId", m.getObjectId());
                                bq.findObjects(PlayActivity.this, new FindListener<MusicCollection>() {
                                    @Override
                                    public void onSuccess(List<MusicCollection> list) {


                                        for (MusicCollection mc : list) {

                                            //如果用户的的id和列表中的id一致，则进行删除操作
                                            if (mc.getUserId().equals(MyApplication.account.getUsername())) {


                                                System.out.println("删除的音乐信息为：" + mc.getObjectId());
                                                MusicCollection m = new MusicCollection();
                                                m.setObjectId(mc.getObjectId());
                                                m.delete(PlayActivity.this, new DeleteListener() {
                                                    @Override
                                                    public void onSuccess() {

                                                        amp_iv_like.setImageResource(R.mipmap.play_icn_love);
                                                        //取消搜藏之后设置为false
                                                        amp_iv_like.setTag(false);

                                                    }

                                                    @Override
                                                    public void onFailure(int i, String s) {
                                                        System.out.println(s + "  " + i);

                                                    }
                                                });


                                            }


                                        }


                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        amp_iv_like.setTag(false);
                                        System.out.println("查询搜藏列表错误");
                                    }
                                });


                            }


                        }

                        @Override
                        public void onError(int i, String s) {
                            //音乐不存在则用户可以搜藏该音乐
                            amp_iv_like.setTag(false);
                            System.out.println("查询音乐信息表错误");


                        }
                    });

                }else{

                    //该用户没有搜藏过改音乐

                    //查询数据库中是否存在改音乐
                    BmobQuery<Music> bq = new BmobQuery<>();
                    bq.addWhereContains("artist", mi.getArtist()).addWhereContains("title", mi.getTitle());
                    bq.findObjects(PlayActivity.this, new FindListener<Music>() {
                        @Override
                        public void onSuccess(List<Music> list) {

                            if(list.size()>0){
                                //如果存在改音乐
                                final Music m = list.get(0);
                                MusicCollection mc = new MusicCollection(MyApplication.account.getUsername(),m.getObjectId());
                                mc.save(PlayActivity.this, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        //添加到搜藏列表成功
                                        amp_iv_like.setTag(true);
                                        amp_iv_like.setImageResource(R.mipmap.play_icn_loved);


                                        UserAction ua = new UserAction(MyApplication.account ,new Emoji()
                                                ,UserAction.Action.Action_Collect , m);

                                        ua.save(PlayActivity.this, new SaveListener() {
                                            @Override
                                            public void onSuccess() {
                                                System.out.println("记录行为成功");
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                System.out.println("记录行为失败：" + s);

                                            }
                                        });


                                        Toast.makeText(PlayActivity.this , "搜藏成功",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(PlayActivity.this , "搜藏失败",Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }else{

                                //如果不存在改音乐，则保存该音乐信息
                                mi.save(PlayActivity.this, new SaveListener() {
                                    @Override
                                    public void onSuccess() {

                                        //音乐信息添加成功
                                        //将信息添加到搜藏列表中
                                        MusicCollection mc = new MusicCollection(MyApplication.account.getUsername(),mi.getObjectId());

                                        mc.save(PlayActivity.this, new SaveListener() {
                                            @Override
                                            public void onSuccess() {
                                                //添加到搜藏列表成功
                                                amp_iv_like.setTag(true);
                                                amp_iv_like.setImageResource(R.mipmap.play_icn_loved);

                                                Toast.makeText(PlayActivity.this , "搜藏成功",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                Toast.makeText(PlayActivity.this , "搜藏失败",Toast.LENGTH_SHORT).show();

                                            }
                                        });


                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(PlayActivity.this , "音乐信息添加异常",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }


                        }

                        @Override
                        public void onError(int i, String s) {

                            //查询失败有可能表不存在也进行保存操作
                            mi.save(PlayActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {

                                    //音乐信息添加成功
                                    //将信息添加到搜藏列表中
                                    MusicCollection mc = new MusicCollection(MyApplication.account.getUsername(),mi.getObjectId());

                                    mc.save(PlayActivity.this, new SaveListener() {
                                        @Override
                                        public void onSuccess() {
                                            //添加到搜藏列表成功
                                            amp_iv_like.setTag(true);
                                            amp_iv_like.setImageResource(R.mipmap.play_icn_loved);

                                            Toast.makeText(PlayActivity.this , "搜藏成功",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Toast.makeText(PlayActivity.this , "搜藏失败",Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(PlayActivity.this , "音乐信息添加异常",Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    });


                }


            }else{

                System.out.println("当前的搜藏按钮还没有处理过");
            }



        }else{

            Toast.makeText(PlayActivity.this , "本地音乐不能搜藏",Toast.LENGTH_SHORT).show();


        }




    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {




    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {



    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {


        //停止时刷新进度
        //当前的进度变化是
        if(ps!=null){
            ps.seekTo(seekBar.getProgress());
        }


    }




}
