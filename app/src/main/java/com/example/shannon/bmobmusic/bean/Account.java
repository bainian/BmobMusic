package com.example.shannon.bmobmusic.bean;

import android.os.Environment;

import com.example.shannon.bmobmusic.utils.ACache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;

/**
 * Created by Shannon on 2016/3/20.
 */
public class Account extends BmobUser{


    public static final String DIRECTORY_ACCOUNT = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/user";
    public static final File FILE_ACCOUNT = new File(DIRECTORY_ACCOUNT);

    /**
     * 缓存当前的用户
     *
     * @param account
     */
    public static void saveAccountToLocal(Account account){


        System.out.println("当前需要缓存的用户信息：" + account.toString());

        if(account!=null){

            JSONObject jo = new JSONObject();
            try {

                jo.put("sex",account.getSex());
                jo.put("describle",account.getDescrible());
                jo.put("url_img",account.getUrl_img());
                jo.put("name",account.getName());
                jo.put("title",account.getTitle());
                String result = jo.toString();
                File file_dire = new File(DIRECTORY_ACCOUNT);

                if(!FILE_ACCOUNT.exists()){

                    file_dire.mkdirs();
                }

                File file = new File(FILE_ACCOUNT,"account.txt");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(result.getBytes());

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }

        }



    }

    //读取用户本地的缓存
    public static Account getAccountFromLoal(){

        File file = new File(DIRECTORY_ACCOUNT,"account.txt");
        if(file.exists()){



            try {

                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int lenght;
                byte[] bytes = new byte[1024];
                while((lenght = fis.read(bytes))!=-1){


                    baos.write(bytes,0,lenght);

                }

                String result = baos.toString();

                JSONObject jo = new JSONObject(result);

                System.out.println("读取本地用户缓存成功");
                return new Account(jo.getInt("sex") , jo.getString("describle")
                        ,jo.getString("url_img"),jo.getString("name"),jo.getString("title"));


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.toString());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(e.toString());
                return null;
            }

        }else{


            return null;
        }


    }


    private Integer sex;
    private String describle;
    private String url_img;
    private String name;
    private String title;


    public static final String SP_USERNAME = "username";
    public static final String SP_PASSWORD = "password";



    public Account(Integer sex, String describle, String url_img, String name, String title) {
        this.sex = sex;
        this.describle = describle;
        this.url_img = url_img;
        this.name = name;
        this.title = title;
    }




    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getDescrible() {
        return describle;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Builder{

        private Integer sex = -1;
        private String describle = "未设置";
        private String url_img = "";
        private String name = "未设置";
        private String title = "未设置";

        public Builder setSex(Integer sex) {
            this.sex = sex;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setUrl_img(String url_img) {
            this.url_img = url_img;
            return this;
        }

        public Builder setDescrible(String describle) {
            this.describle = describle;
            return this;
        }

        public Account build(){



            return new Account(sex, describle, url_img, name, title);


        }

    }

    @Override
    public String toString() {
        return "Account{" +
                "sex=" + sex +
                ", describle='" + describle + '\'' +
                ", url_img='" + url_img + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
