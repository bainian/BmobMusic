package com.example.shannon.bmobmusic.bean;

/**
 * Created by Shannon on 2016/3/23.
 */
public class MusicCache {

    private String dateSaved;
    private String type;
    private String jsonMusic;

    public MusicCache(String dateSaved, String jsonMusic, String type) {
        this.dateSaved = dateSaved;
        this.jsonMusic = jsonMusic;
        this.type = type;
    }



    public String getDateSaved() {
        return dateSaved;
    }

    public void setDateSaved(String dateSaved) {
        this.dateSaved = dateSaved;
    }

    public String getJsonMusic() {
        return jsonMusic;
    }

    public void setJsonMusic(String jsonMusic) {
        this.jsonMusic = jsonMusic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MusicCache{" +
                "dateSaved='" + dateSaved + '\'' +
                ", type='" + type + '\'' +
                ", jsonMusic='" + jsonMusic + '\'' +
                '}';
    }
}
