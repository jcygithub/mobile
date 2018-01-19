package com.jcy.capacity.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jcy.capacity.MainActivity;
import com.jcy.capacity.R;
import com.jcy.capacity.utils.ShareUtils;
import com.jcy.capacity.utils.StaticClass;

public class SplashActivity extends AppCompatActivity {
    private TextView mTextView;
    //public String name="123456";
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断是否是第一次运行
            switch (msg.what){
                case StaticClass.HANDLER_SPLASH:
                    if(isFirst()){
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }
    //初始化View
    private void initView(){
        //延迟两秒
        mHandler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,2000);
        mTextView= (TextView) findViewById(R.id.text);
    }
    private boolean isFirst(){
        boolean isFirst = ShareUtils.getBoolean(this,StaticClass.SHARE_IS_FIRST,true);
        if(isFirst){
            ShareUtils.putBoolean(this,StaticClass.SHARE_IS_FIRST,false);
            return true;
        }else {
            return false;
        }
    }
    //禁止返回键
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
}
