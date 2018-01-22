package com.fiki.app.wifi.wifidocumentation.src.domain.confi;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by root on 2017/11/22.
 */

public class IsAtablet {
    public IsAtablet() {
    }
    public static boolean isItATablet(Context ctx){
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
