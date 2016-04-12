package com.example.shannon.bmobmusic.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Shannon on 2016/3/27.
 */
public class UserAction extends BmobObject{


    //用户行为的枚举
    public static enum Action{Action_Play , Action_Collect , Action_Collect_Remove
        , Action_Delete , Action_Switch , Action_Download};

    //该操作的用户
    private Account account;
    private Action action;
    private Emoji emoji;
    private Music music;


    public UserAction(Account account, Emoji emoji, Action action , Music music) {
        this.account = account;
        this.emoji = emoji;
        this.action = action;
        this.music = music;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public void setEmoji(Emoji emoji) {
        this.emoji = emoji;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
