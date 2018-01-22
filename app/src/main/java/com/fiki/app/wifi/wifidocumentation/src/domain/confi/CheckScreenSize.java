package com.fiki.app.wifi.wifidocumentation.src.domain.confi;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by root on 2017/11/22.
 */

public class CheckScreenSize
{
    public CheckScreenSize() {
    }

    public boolean screenBiggerThan(double screenRange )
    {
        DisplayMetrics metrics = new DisplayMetrics();

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=screenRange){
            // same as range or bigger
            return true;
        }else{
            // smaller device
            return false;
        }
    }

}
