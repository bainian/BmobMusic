package com.example.shannon.bmobmusic.bean;

/**
 * Created by master on 2016/5/20/0020.
 */
public class EmojiResult {

    private String resultcode;
    private String rc_main;
    private String rc_main_value;
    private String servertime;

    public EmojiResult() {
    }



    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getRc_main() {
        return rc_main;
    }

    public void setRc_main(String rc_main) {
        this.rc_main = rc_main;
    }

    public String getRc_main_value() {
        return rc_main_value;
    }

    public void setRc_main_value(String rc_main_value) {
        this.rc_main_value = rc_main_value;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    @Override
    public String toString() {
        return "EmojiResult{" +
                "resultcode='" + resultcode + '\'' +
                ", rc_main='" + rc_main + '\'' +
                ", rc_main_value='" + rc_main_value + '\'' +
                ", servertime='" + servertime + '\'' +
                '}';
    }
}
