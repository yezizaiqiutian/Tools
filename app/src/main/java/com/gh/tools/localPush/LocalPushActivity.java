package com.gh.tools.localPush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.main.MainActivity;

/**
 * @author: gh
 * @description:
 * @date: 2016/11/10 14:50.
 */

public class LocalPushActivity extends BaseActivity{

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LocalPushActivity.class);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        push();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void push() {
        NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String title = "通知标题" ;

        String content = "通知内容" ;

        //1.实例化一个通知，指定图标、概要、时间

//        Notification n=new Notification(R.mipmap.ic_launcher,"这是通知,早上好",1000);
        Notification.Builder builder1 = new Notification.Builder(this);
        builder1.setSmallIcon(R.mipmap.ic_launcher); //设置图标
        builder1.setTicker("显示第二个通知");
        builder1.setContentTitle("通知"); //设置标题
        builder1.setContentText("点击查看详细内容"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent =new Intent (this,MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(this, 0, intent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        nm.notify(124, notification1); // 通过通知管理器发送通知
//
//        //2.指定通知的标题、内容和intent
//
//        Intent intent = new Intent(this, MainActivity.class);
//
////设置跳转到的页面 ，时间等内容
//
//        PendingIntent pi= PendingIntent.getActivity(this, 0, intent, 1000);
//
//        n.setLatestEventInfo(this, title, content, pi);
//        //3.指定声音
//        n.defaults = Notification.DEFAULT_SOUND;
//        //4.发送通知
//        nm.notify(1, n);
    }

}
