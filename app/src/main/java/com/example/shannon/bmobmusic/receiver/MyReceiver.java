package com.example.shannon.bmobmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Shannon on 2016/3/22.
 */
public abstract class MyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        doAction(context , intent);
    }

    public abstract void doAction(Context context, Intent intent);



}
