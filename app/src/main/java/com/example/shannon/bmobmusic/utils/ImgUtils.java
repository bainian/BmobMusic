package com.example.shannon.bmobmusic.utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * Created by Shannon on 2016/3/21.
 */
public class ImgUtils {


    private static final Uri ALBUM_ART_URI = Uri.parse("content://media/external/audio/albumart");

    public static Bitmap getArtwork(Context context , long idSong , long idAlbum){

        try {

            FileDescriptor fd = null;
            ParcelFileDescriptor pfd = null;

            if(idAlbum<0){

                //从音乐文件中获取专辑封面
                Uri uri = Uri.parse("content://media/external/audio/media/" + idSong + "/albumart");
                pfd = context.getContentResolver().openFileDescriptor(uri , "r");




            }else{


                //从数据库中获取封面
                Uri uri = ContentUris.withAppendedId(ALBUM_ART_URI , idAlbum);
                pfd = context.getContentResolver().openFileDescriptor(uri , "r");


            }


            if(pfd != null){

                fd = pfd.getFileDescriptor();

            }


            /*
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd,null,options);

            int width_img = options.outWidth;
            int height_img = options.outHeight;

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width_win = wm.getDefaultDisplay().getWidth();
            int height_win = wm.getDefaultDisplay().getHeight();

            int ratio_width = width_img/width_win;
            int ratio_height = height_img/height_win;

            if(ratio_width==0 && ratio_height ==0){

                return BitmapFactory.decodeFileDescriptor(fd,null,options);

            }else{


                if(ratio_height>ratio_width){

                    options.inSampleSize = ratio_height;


                }else{

                    options.inSampleSize = ratio_width;

                }

                return BitmapFactory.decodeFileDescriptor(fd,null,options);


            }*/



            return BitmapFactory.decodeFileDescriptor(fd , null,null);


        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            e.printStackTrace();

            return null;
        }











    }




}
