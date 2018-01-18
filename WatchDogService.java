package com.zd.bim.common.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zd.bim.R;
import com.zd.bim.activity.MsgCenterAct;
import com.zd.bim.activity.WelcomeAct;
import com.zd.bim.adapter.MsgListAdapter;
import com.zd.bim.common.BIMApplication;
import com.zd.bim.db.ZCache;
import com.zd.bim.http.BimURL;
import com.zd.bim.utils.LogUtils;
import com.zd.bim.utils.StringUtils;
import com.zd.bim.utils.UIUtils;


import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by zx on 2017/9/7.
 * 和即时消息的通讯服务
 */
public class WatchDogService extends Service {

    private Intent intent = new Intent("com.zd.msg.RECEIVER");
    private Socket mSocket;
    private int msgId = 0;
    private boolean flag = true;
    private com.alibaba.fastjson.JSONObject obj;
    private Thread dog;

    {
        try {
            mSocket = IO.socket(BimURL.URL_HTTP_SOCKETIO);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        ((BIMApplication)getApplication()).addService(this);
        if (mSocket == null) {
            try {
                mSocket = IO.socket(BimURL.SOCKET_URL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("sm_select_login", onLogIn);

        mSocket.connect();

//        init();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return Service.START_STICKY;
        return Service.START_REDELIVER_INTENT;
    }

    private void init() {
        flag = true;
        obj = new com.alibaba.fastjson.JSONObject();
        obj.put("user", ZCache.getInstance().get("username", ""));
        msgId = (int) ZCache.getInstance().get(ZCache.KEY_MSGID, 0);
        if(dog!=null &&dog.isAlive())
            return;
        new Thread(WatchRunnable).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        flag = false;
        ZCache.getInstance().put("msgId", msgId);
        LogUtils.e("WatchDog挂了");

    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("ZX", "连接成功watchDog");
            UIUtils.RunOnUIThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("调用init方法");
                    init();
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("ZX", "断开连接watchDog");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("ZX", "连接出错watchDog");
        }
    };


    Emitter.Listener onLogIn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject data = (JSONObject) args[0];
                Log.e("ZX", "soketIoservice2接收到的最新消息" + data.toString());
                int ret = data.getInt("ret");
                Log.e("ZX", ret + "");
                if (ret == -1) {
                    LogUtils.e("没消息");
                    LogUtils.e("消息id" + msgId);
                } else {
                    //解析消息,并发送广播，更新msgId
                    String info = data.getString("info");
                    LogUtils.e("info" + info);
                    com.alibaba.fastjson.JSONArray arr = com.alibaba.fastjson.JSONObject.parseArray(info);
                    com.alibaba.fastjson.JSONObject obj = arr.getJSONObject(0);
                    LogUtils.e("obj" + obj.toJSONString());
                    msgId = Integer.parseInt(obj.getString("id"));
                    ZCache.getInstance().put(ZCache.KEY_MSGID, msgId);
                    String type = obj.getString("type");
                    String content = "";
                    if (MsgListAdapter.MSG_INVITE.equals(type)) {
                        if (!StringUtils.isEmpty(obj.getString("content"))) {
                            com.alibaba.fastjson.JSONObject o = com.alibaba.fastjson.JSONObject.parseObject(obj.getString("content"));
                            content = o.getString("content");
                        }
                    } else {
                        content = obj.getString("content");
                    }
                    String title = obj.getString("title");
                    Log.e("ZX", "内容：" + content);
                    Log.e("ZX", "标题：" + title);
                    sendBrodCast(title, content);
                }
                flag = true;
                if (!dog.isAlive()) {
                    new Thread(WatchRunnable).start();
                    Log.e("ZX", "再次启动了..");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 每隔2秒，
    private Runnable WatchRunnable = new Runnable() {
        public void run() {
            // 服务未死，看门不停
            LogUtils.e("WatchDog在循环判断");
            while (flag) {
                synchronized (this) {
                    try {
                        obj.put("id", msgId);
                        String param = obj.toJSONString();
                        LogUtils.e("发送给婻发送的id" + msgId + "参数：" + param);
                        mSocket.emit("sm_select_login", param);

                        flag = false;
                        Thread.sleep(2000);
                        dog = Thread.currentThread();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    public void sendBrodCast(String title, String content) {
//        1获取通知管理器NotificationManager，它也是一个系统服务
//        2建立通知Notification notification = new Notification(icon, null, when);
//        3为新通知设置参数(比如声音，震动，灯光闪烁)
//        4把新通知添加到通知管理器
//        发送消息的代码如下：
//        获取通知管理器
        LogUtils.e("发送通知");
        Context context = getApplicationContext();
        Intent intent = new Intent(this, MsgCenterAct.class);//跳转到消息中心
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);//当点击消息时就会向系统发送openintent意图
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title)
                .setTicker("中达bim的通知")
                .setAutoCancel(true)                            //打开程序后图标消失
                .setContentIntent(pendingIntent)                //设置点击响应
                .setSmallIcon(R.drawable.icon_android);


        if (!StringUtils.isEmpty(content)) {
            builder.setContentText(content);
        }
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        Log.i("repeat", "showNotification");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());

    }

}
