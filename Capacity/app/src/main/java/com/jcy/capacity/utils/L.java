package com.jcy.capacity.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/8/25.
 */

public class L {
    private static final boolean DEBUG=true;
    private static final String TAG="Smartbutler";
    //五个等级  DIWE

    public static void d(String text){
        if(DEBUG){
            Log.d(TAG,text);
        }
    }
    public static void i(String text){
        if(DEBUG){
            Log.i(TAG,text);
        }
    }
    public static void w(String text){
        if(DEBUG){
            Log.w(TAG,text);
        }
    }
    public static void e(String text){
        if(DEBUG){
            Log.e(TAG,text);
        }
    }
}
