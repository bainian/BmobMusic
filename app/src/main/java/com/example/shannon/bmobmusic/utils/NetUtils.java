package com.example.shannon.bmobmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.shannon.bmobmusic.bean.Music;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Shannon on 2016/1/13.
 */
public class NetUtils {



    public static final String url_search_head = "http://www.tingqq.com/include/search.php?ac=play&key=";
    public static final String url_search_lrc = "http://www.cnlyric.com/search.php?k=";
    public static final String url_home = "http://www.cnlyric.com/";


    //下载文件
    public static boolean downFile(String url , String path){


        try {


            HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
            if(huc.getResponseCode() == 200){


                InputStream is = huc.getInputStream();


                byte[] bytes = new byte[1024];

                int len;

                File file = new File(path);

                if(!file.exists()){

                    file.createNewFile();
                    System.out.println("文件是否存在：" + file.exists());

                }


                FileOutputStream fos = new FileOutputStream(file);


                while((len = is.read(bytes))!=-1){


                    fos.write(bytes, 0, len);


                }

                return true;

            }else{

                System.out.println(huc.getResponseCode());
                return false;
            }



        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("MalformedURLException");
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }



    }


    //获得歌词的下载链接
    public static String getLrcUrl(String artist , String title){

        try {

            String eartist = URLEncoder.encode(artist, "gbk");
            String etitle = URLEncoder.encode(title,"gbk");

            String full_url = url_search_lrc + eartist + "+" + etitle + "&t=s";
            System.out.println("搜索歌词的链接为：" + full_url);
            Document doc = Jsoup.parse(new URL(full_url), 5000);
            Elements ele2 = doc.select("a.ld");

            if(ele2.size()>0){

                return url_home + ele2.get(0).attr("href");
            }else{

                return null;
            }


        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }






    }




    public static String getUrl_download(String url){

        try {


            Document doc = Jsoup.parse(new URL(url),5000);
            String url_download = doc.select("textarea").text();
            System.out.println(url_download);
            //String url_download = el1.get(0).text();

            return url_download;


        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }


    }




    public static ArrayList<Music> searchMusic(String input){

        try {

            ArrayList<Music> al = new ArrayList<>();
            String code = URLEncoder.encode(input,"gbk");
            String url_full = "http://www.tingqq.com/include/search.php?ac=play&key=" + code;
            //System.out.println("搜索的完整链接为：" + url_full);

            Document doc = Jsoup.parse(new URL(url_full),50000);
            //先定位到div
            Elements el1 = doc.select("div#f2");

            for(Element e : el1){



                //找到左边的div
                Elements el2 = e.select("div.aleft");
                Elements el3 = el2.select("a");

                //找到包含a标签的歌曲信息
                for(Element ee:el3){

                    String text = ee.text();
                    String url = "http://www.tingqq.com" + ee.attr("href");
                    //System.out.println("下载地址的页面：" + url);
                    String url_download = getUrl_download(url);
                    //System.out.println("下载地址：" + url_download);
                    Music m = new Music.MusicBuilder().build();
                    String[] result = text.split(" ");
                    if(result.length<2){

                        m.setTitle("???");
                        m.setArtist(result[0]);
                    }else{

                        m.setTitle(result[1]);
                        m.setArtist(result[0]);
                    }

                    m.setPath_music(url_download);
                    al.add(m);


                }


            }

            return al;



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return  null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }



    }

    public static void downMusic(String url,String path){

        try {
            HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
            File file = new File(path);

            FileInputStream fis = (FileInputStream) huc.getInputStream();

            FileOutputStream fos = new FileOutputStream(file);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes))!=-1){

                fos.write(bytes,0,length);


            }




        } catch (IOException e) {
            e.printStackTrace();
        }


    }







    public static Bitmap downImage(String url){


        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = 2;


        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            if(huc.getResponseCode() == 200){

                InputStream is = huc.getInputStream();
                return  BitmapFactory.decodeStream(is , null , op);

            }

            return null;



        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("图片下载地址解析错误");
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("图片下载网络连接错误");
            return null;
        }


    }

    public static boolean checkNet(Context context){

        //获得连接管理
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null){

            return false;

        }else{
            //获得所有的网络信息
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if(info !=null){

                //遍历网络信息
                for(int i=0 ; i<info.length ; i++){

                    //如果当前网络可用
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){


                        NetworkInfo ni = info[i];
                        if(ni.getType() == ConnectivityManager.TYPE_WIFI){

                            return true;
                        }else if(ni.getType() == ConnectivityManager.TYPE_MOBILE){

                            return true;
                        }


                    }




                }



            }



        }


        return false;


    }




}
