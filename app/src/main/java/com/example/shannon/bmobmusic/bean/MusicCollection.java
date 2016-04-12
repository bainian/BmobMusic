package com.example.shannon.bmobmusic.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Shannon on 2016/2/25.
 */
public class MusicCollection extends BmobObject{

    private String userId;
    private String musicId;

    public MusicCollection(String userId, String musicId) {
        this.userId = userId;
        this.musicId = musicId;
    }

    public MusicCollection() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    @Override
    public String toString() {
        return "MusicCollection{" +
                "userId='" + userId + '\'' +
                ", musicId='" + musicId + '\'' +
                '}';
    }


}
