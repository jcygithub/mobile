package com.jcy.capacity.application;

import android.app.Application;

import com.jcy.capacity.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/8/25.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化DUGLY
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APPID, true);
        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APPID);
    }
}
