package com.example.shannon.bmobmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.shannon.bmobmusic.bean.Music;
import com.example.shannon.bmobmusic.bean.MusicCache;
import com.example.shannon.bmobmusic.bean.MusicCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by Shannon on 2016/3/21.
 */
public class MediaUtils {

    private static final String DIRE_DOWNLOAD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath()+"/";

    public static ArrayList<Music> getMusicFromLocal(Context context){


        System.out.println("开始获取本地的音乐信息");
        //context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED"));


        ArrayList<Music> al_music = new ArrayList<>();

        Cursor cursor = context.getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        while(cursor.moveToNext()){


            //查询音乐信息
            Long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            Long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            Long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url_local = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            Long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));



            Music m = new Music.MusicBuilder().setId(id).
                    setTitle(title).setArtist(artist).
                    setDuration(duration).setAlbum(album).
                    setSize(size).setPath_music(url_local).
                    setId_Album(album_id).build();


            al_music.add(m);






        }

        return al_music;





    }

    public static final String URL_BSB = "http://music.163.com/discover/toplist?id=19723756";
    public static final String URL_XGB = "http://music.163.com/discover/toplist?id=3779629";
    public static final String URL_RGB = "http://music.163.com/discover/toplist?id=3778678";


    public static final String TYPE_BSB = "sp_bsb";
    public static final String TYPE_XGB = "sp_xgb";
    public static final String TYPE_RGB = "sp_rgb";

    //当前系统的根目录
    public static final String DIRECTORY_ROOT = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();






    //网络获取榜单信息
    public static MusicCache getNetMusicFromNet(String type){

        //首先确定网址，然后获取json字符串

        String url = null;

        switch (type){


            case TYPE_BSB:

                url = URL_BSB;
                break;

            case TYPE_XGB:
                url = URL_XGB;
                break;

            case TYPE_RGB:
                url = URL_RGB;
                break;


        }


        if(url == null){


            return null;

        }else{


            Document doc = null;

            try {


                doc = Jsoup.connect(url).userAgent("Mozilla").get();
                Elements e = doc.select("textarea[style=display:none;]");
                String result = e.get(0).text();
                return new MusicCache(timeFormat(System.currentTimeMillis()),result,type);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
                return null;
            }







        }



    }



    /**
     *
     * 从本地获取缓存
     * @return
     */
    public static MusicCache getNetMusicFromLocal(Context context ,String type){




        String path = null;

        switch (type){


            case TYPE_BSB:

                path = SpUtils.getStringContent(context , type);

                break;

            case TYPE_XGB:

                path = SpUtils.getStringContent(context , type);
                break;

            case TYPE_RGB:

                path = SpUtils.getStringContent(context , type);
                break;


        }


        if(path == null){

            //情形一，本地没有缓存
            return null;

        }else{


            File file = new File(path);
            if(file.exists()){

                String name = file.getName();

                int index = name.indexOf(".");
                if(index>0){

                    //如果文件名格式正常
                    String stime = name.substring(0,index);

                    try {


                        FileInputStream fis = new FileInputStream(file);
                        byte[] bytes = new byte[1024];
                        int length;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while((length = fis.read(bytes))!=-1){

                            baos.write(bytes,0,length);

                        }

                        String result = baos.toString();

                        return new MusicCache(stime , result,type);





                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        System.out.println(e.toString());
                        //异常
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(e.toString());
                        //异常
                        return null;
                    }


                }else{

                    //情形一，本地没有缓存
                    return null;


                }



            }else{

                //文件不存在
                return null;


            }











        }






    }


    //保存json数据到本地
    public static void saveNetMusicToLocal(Context context , MusicCache mc){


        if(mc!=null){


            String path = null;

            switch (mc.getType()){


                case TYPE_BSB:

                    path = DIRECTORY_ROOT + "/" + TYPE_BSB + "/" + mc.getDateSaved() + ".json";

                    break;

                case TYPE_XGB:

                    path = DIRECTORY_ROOT + "/" + TYPE_XGB + "/" + mc.getDateSaved() + ".json";
                    break;

                case TYPE_RGB:
                    path = DIRECTORY_ROOT + "/" + TYPE_RGB + "/" + mc.getDateSaved() + ".json";

                    break;


            }


            try {

                if(path != null){

                    File file = new File(path);

                    FileOutputStream fos = new FileOutputStream(file);

                    String result = mc.getJsonMusic();

                    if(result!=null){

                        fos.write(mc.getJsonMusic().getBytes());
                    }

                    SpUtils.saveStringContent(context , mc.getType(),path);

                    System.out.println("榜单本地保存成功");



                }







            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


            //记录






        }





    }


    public interface OnGetArtWorkListener{

        public void success();
        public void fail();
    }


    private static OnGetArtWorkListener oal;

    public static void setOal(OnGetArtWorkListener oAL) {
        oal = oAL;
    }



    public static String getFileFromLocal(String file_name){

        //拼出完整的路径
        String path = DIRE_DOWNLOAD + file_name;
        File file = new File(path);
        if(file.exists()){

            return path;
        }else{

            return null;

        }







    }


    //判断本地的数据库是有该音乐信息
    public static String searchMusicFromDatabase(String artist , String title,Context context){


        ArrayList<Music> al = getMusicFromLocal(context);



        for(int i=0 ; i<al.size() ; i++){

            Music m = al.get(i);


            System.out.println(m.getArtist().equals(artist));
            if((m.getArtist().equals(artist))&&(m.getTitle().equals(title))){

                System.out.println("本地存在该音乐");
                return m.getPath_music();



            }



        }

        return null;



    }


    //json转集合
    public static ArrayList<Music> jsonToList(String result) {

        if(result!=null){

            ArrayList<Music> al = new ArrayList();
            try {
                JSONArray ja = new JSONArray(result);


                for(int i=0 ; i<ja.length() ; i++){

                    String title;
                    String artist;
                    Long duration;
                    String path_music;
                    String path_img;


                    JSONObject jo = ja.getJSONObject(i);

                    title = jo.getString("name");
                    duration = Long.valueOf(jo.getString("duration"));
                    path_music = jo.getString("mp3Url");


                    JSONObject jo1 = jo.getJSONObject("album");
                    path_img = jo1.getString("blurPicUrl");
                    artist = jo.getJSONArray("artists").optJSONObject(0).getString("name");

                    Music music = new Music.MusicBuilder().setTitle(title)
                            .setArtist(artist).setDuration(duration).setPath_music(path_music)
                            .setPath_img(path_img).build();


                    al.add(music);




                }



                return al;


            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.toString());

                return null;
            }


        }



        return null;







    }

    //时间格式化
    public static String timeFormat(long time){


        DateFormat df = new SimpleDateFormat("yy-MM-dd");
        return df.format(time);


    }




    public static ArrayList<Music> getUserCollection(final Context context , String userId){




        if(!TextUtils.isEmpty(userId)){

            final ArrayList<Music> al_music = new ArrayList<Music>();
            BmobQuery<MusicCollection> bq = new BmobQuery<>();
            bq.addWhereContains("userId",userId);
            bq.findObjects(context, new FindListener<MusicCollection>() {
                @Override
                public void onSuccess(List<MusicCollection> list) {

                    System.out.println("搜藏集合：" + list.size());
                    BmobQuery<Music> bqm = new BmobQuery<Music>();
                    for(MusicCollection mc:list){

                        bqm.getObject(context, mc.getMusicId() , new GetListener<Music>() {
                            @Override
                            public void onSuccess(Music music) {

                                al_music.add(music);

                            }

                            @Override
                            public void onFailure(int i, String s) {


                            }
                        });



                    }



                    Toast.makeText(context,"网络收藏查询完成",Toast.LENGTH_SHORT).show();



                }

                @Override
                public void onError(int i, String s) {


                    Toast.makeText(context,"网络查询失败",Toast.LENGTH_SHORT).show();

                }
            });


            return al_music;



        }else{


            return null;
        }


    }


}
