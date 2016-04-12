package com.example.shannon.bmobmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.example.shannon.bmobmusic.bean.Music;
import com.example.shannon.bmobmusic.bean.PlayState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener{


    public static ArrayList<Music> al_music;
    public static ArrayList<Music> al_music_collection;
    public static PlayState playState = new PlayState();

    public static final int MODE_ORDER = 0x0;
    public static final int MODE_RANDOM = 0x1;

    private MediaPlayer mp;



    public PlayService() {

        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        es.execute(updateStatusRunnable);
        return new PlayBinder();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();

        if(es!=null && !es.isShutdown()){
            es.shutdown();

        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        //播放完成时进行下一曲
        next();

    }

    public class PlayBinder extends Binder {

        public PlayService getPlayService(){

            return PlayService.this;

        }

    }

    //创建单个线程池
    private ExecutorService es = Executors.newSingleThreadExecutor();

    private Runnable updateStatusRunnable = new Runnable() {
        @Override
        public void run() {

            while(true){

                playState.setProgress(getCurrentProgress());
                Intent intent = new Intent();
                intent.setAction("com.shannon.bmplayer.progress");
                sendBroadcast(intent);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

        }
    };


    public int getCurrentProgress(){

        if(mp!=null && mp.isPlaying()){

            return mp.getCurrentPosition();

        }else{

            return 0;
        }

    }

    //已修改
    public void pre(){

        int currentPosition = playState.getCurrentPosition();

        if(currentPosition == 0){

            currentPosition = al_music.size()-1;

        }else{

            currentPosition--;

        }


        playState.setCurrentPosition(currentPosition);

        play(currentPosition);


    }



    //修改
    public void play(final int position){


        Music m = PlayState.getCurrentMusic(al_music , position);

        if(m!=null){


            try {

                mp.reset();
                mp.setDataSource(this , Uri.parse(m.getPath_music()));
                mp.prepareAsync();
                playState.setCurrentPosition(position);
                playState.setPlaying(true);

                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        //准备好后开始更新状态

                        playState.setDuration(mp.getDuration());

                        mp.start();

                    }
                });



            } catch (IOException e) {
                System.out.println("音乐地址解析错误");
                e.printStackTrace();
            }


        }




    }


    //修改
    public void start(){

        if(mp!=null && !mp.isPlaying()){

            playState.setPlaying(true);
            mp.start();
        }


    }


    //获取总时长    方便网络音乐进度的显示
    public int getDuration(){

        if(mp!=null){

            return mp.getDuration();
        }else{

            return 0;
        }


    }


    public void seekTo(int msc){

        if(mp!=null){

            mp.seekTo(msc);
        }

    }

    //修改
    public void pause(){

        if(mp!=null && mp.isPlaying()){


            playState.setPlaying(false);
            mp.pause();
            System.out.println("暂停时的进度：" + getCurrentProgress());

        }


    }


    public void next(){


        int currentPosition = playState.getCurrentPosition();
        System.out.println("当前的位置：" + currentPosition);
        int mode = playState.getMode();

        if(al_music!=null){

            switch (mode){

                case MODE_ORDER:



                    if(currentPosition == al_music.size()-1){

                        currentPosition = 0;
                    }else{

                        currentPosition++;

                    }


                    break;

                case MODE_RANDOM:

                    Random random = new Random(System.currentTimeMillis());
                    currentPosition = random.nextInt(al_music.size());

                    break;



            }

            playState.setCurrentPosition(currentPosition);
            play(currentPosition);



        }






    }
















}
