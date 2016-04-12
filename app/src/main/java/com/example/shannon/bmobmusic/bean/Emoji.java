package com.example.shannon.bmobmusic.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Shannon on 2016/3/27.
 */
public class Emoji extends BmobObject{


    //情绪对应的枚举
    public static enum EmojiType{cT , kR , kA , kP , Default , Ka , Kp , Kr , Ct , Cg , Yc
        ,Yl , Yv , Ml , Ms , Ws , Wc };

    private EmojiType tag;
    private String describle;

    public Emoji() {
        tag = EmojiType.Default;
        describle = "";

    }

    public EmojiType getTag() {
        return tag;
    }

    public void setTag(EmojiType tag) {
        this.tag = tag;
    }

    public String getDescrible() {
        return describle;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }
}
