package com.example.shannon.bmobmusic.bean;

import android.content.ContentUris;
import android.net.Uri;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Shannon on 2016/3/19.
 */
public class Music extends BmobObject{

    public static final int MUSIC_LOCAL = 0x0;
    public static final int MUSIC_NET = 0X1;

    private static final Uri ALBUM_ART_URI = Uri.parse("content://media/external/audio/albumart");


    private long id;
    private String title;
    private String artist;
    private long duration;
    private String path_img;
    private long size;
    private String path_music;
    private int type;
    private String album;
    private long id_album;
    private BmobRelation likes = new BmobRelation();

    public Music(long id, String title, String artist, long duration, String path_img, long size, String path_music, int type,String album,long id_album) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path_img = path_img;
        this.size = size;
        this.path_music = path_music;
        this.type = type;
        this.album = album;
        this.id_album = id_album;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId_album(long id_album) {
        this.id_album = id_album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPath_music(String path_music) {
        this.path_music = path_music;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPath_img(String path_img) {
        this.path_img = path_img;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public String getPath_img() {
        return path_img;
    }

    public long getSize() {
        return size;
    }

    public String getPath_music() {
        return path_music;
    }

    public int getType() {
        return type;
    }

    public String getAlbum() {
        return album;
    }


    public long getId_album() {
        return id_album;
    }


    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    //创建者设计模式
    public static class MusicBuilder{

        private long id = -1;
        private String title = null;
        private String artist = null;
        private long duration = -1;
        private String path_img = null;
        private long size = -1;
        private String path_music = null;
        private int type = MUSIC_LOCAL;
        private String album = null;
        private long id_album = -1;





        public MusicBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public MusicBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public MusicBuilder setArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public MusicBuilder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public MusicBuilder setPath_img(String path_img) {
            this.path_img = path_img;
            return this;
        }

        public MusicBuilder setSize(long size) {
            this.size = size;
            return this;
        }

        public MusicBuilder setPath_music(String path_music) {
            this.path_music = path_music;
            return this;
        }

        public MusicBuilder setType(int type) {
            this.type = type;
            return this;
        }

        public MusicBuilder setAlbum(String album){

            this.album = album;
            return this;

        }

        public MusicBuilder setId_Album(long id_album){

            this.id_album = id_album;
            return this;

        }




        public Music build(){


            return new Music(id, title, artist, duration, path_img, size, path_music, type,album , id_album);

        }


    }


    public static String getImgUri(long idSong , long idAlbum){


        Uri uri;
        if(idAlbum<0){

            uri = Uri.parse("content://media/external/audio/media/" + idSong + "/albumart");

        }else{


            uri = ContentUris.withAppendedId(ALBUM_ART_URI , idAlbum);


        }

        return uri.toString();




    }



}
