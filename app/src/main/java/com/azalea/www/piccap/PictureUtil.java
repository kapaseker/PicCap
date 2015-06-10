package com.azalea.www.piccap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by Panoo on 2015/6/7.
 */
public class PictureUtil {

    public static void cleanImageView(ImageView iv){

        if(iv.getDrawable() instanceof BitmapDrawable){
            BitmapDrawable bMap = (BitmapDrawable) iv.getDrawable();
            bMap.getBitmap().recycle();
            iv.setImageDrawable(null);
        }
    }


    public static BitmapDrawable getScaledPictrue(Activity ac,String path){

        DisplayMetrics dMetric = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(dMetric);
        int screeWidth = dMetric.widthPixels;
        int screenHeight = dMetric.heightPixels;

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,option);

        int srcWidth= option.outWidth;
        int srcHeight = option.outHeight;

        int ratioSize = 1;
        if(srcHeight>screeWidth||srcHeight>screenHeight){
            if(srcHeight>srcWidth){
                ratioSize = Math.round(srcWidth/screeWidth);
            }else{
                ratioSize = Math.round(srcHeight/screenHeight);
            }
        }

        BitmapFactory.Options rationOption = new BitmapFactory.Options();
        rationOption.inSampleSize = ratioSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path,rationOption);

        return new BitmapDrawable(ac.getResources(),bitmap);
    }
}
