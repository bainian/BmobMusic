package com.example.shannon.bmobmusic.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shannon on 2016/3/21.
 */
public class SpUtils {


    private static final String SP_PARAMETER = "parameter";



    public static void saveStringContent(Context context , String key , String value){


        SharedPreferences sp = context.getSharedPreferences(SP_PARAMETER,Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();


    }

    public static String getStringContent(Context context , String key){

        SharedPreferences sp = context.getSharedPreferences(SP_PARAMETER , Context.MODE_WORLD_READABLE);
        return sp.getString(key,null);



    }




}
