package com.gh.tools.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.base.MyApplication;
import com.gh.tools.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/28 15:00.
 */

public class NotificationActivity extends BaseActivity {

    private final int pushId=1;

    /**
     * 启动NotificationActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_btn_1)
    public void onClick() {
        showNotification1();
    }

    private void showNotification1() {
////系统消息，自己发的消息，程序在前台的时候不通知
//        if (msg==null||Foreground.get().isForeground()||
//                (msg.getConversation().getType()!=TIMConversationType.Group&&
//                        msg.getConversation().getType()!= TIMConversationType.C2C)||
//                msg.isSelf()||
//                msg.getRecvFlag() == TIMGroupReceiveMessageOpt.ReceiveNotNotify ||
//                MessageFactory.getMessage(msg) instanceof CustomMessage) return;
        String senderStr,contentStr;
//        Message message = MessageFactory.getMessage(msg);
//        if (message == null) return;
        senderStr = "设置通知栏标题";
        contentStr = "设置通知栏内容";
        NotificationManager mNotificationManager = (NotificationManager) MyApplication.getContext().getSystemService(MyApplication.getContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyApplication.getContext());
        Intent notificationIntent = new Intent(MyApplication.getContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(MyApplication.getContext(), 0,
                notificationIntent, 0);
        mBuilder.setContentTitle(senderStr)//设置通知栏标题
                .setContentText(contentStr)
                .setContentIntent(intent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                .setTicker(senderStr+":"+contentStr) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(pushId, notify);
    }

//    //自定义显示的通知 ，创建RemoteView对象
//    private void showCustomizeNotification() {
//
//        CharSequence title = "i am new";
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//        Notification noti = new Notification(icon, title, when + 10000);
//        noti.flags = Notification.FLAG_INSISTENT;
//
//        // 1、创建一个自定义的消息布局 view.xml
//        // 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段
//        RemoteViews remoteView = new RemoteViews(this.getPackageName(),R.layout.notification);
//        remoteView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//        remoteView.setTextViewText(R.id.text , "通知类型为：自定义View");
//        noti.contentView = remoteView;
//        // 3、为Notification的contentIntent字段定义一个Intent(注意，使用自定义View不需要setLatestEventInfo()方法)
//
//        //这儿点击后简单启动Settings模块
//        PendingIntent contentIntent = PendingIntent.getActivity
//                (NotificationActivity.this, 0,new Intent("android.settings.SETTINGS"), 0);
//        noti.contentIntent = contentIntent;
//
//        NotificationManager mnotiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mnotiManager.notify(0, noti);
//
//    }
//
//    // 默认显示的的Notification
//    private void showDefaultNotification() {
//        // 定义Notication的各种属性
//        CharSequence title = "i am new";
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//        Notification noti = new Notification(icon, title, when + 10000);
//        noti.flags = Notification.FLAG_INSISTENT;
//
//        // 创建一个通知
//        Notification mNotification = new Notification();
//
//        // 设置属性值
//        mNotification.icon = R.mipmap.ic_launcher;
//        mNotification.tickerText = "NotificationTest";
//        mNotification.when = System.currentTimeMillis(); // 立即发生此通知
//
//        // 带参数的构造函数,属性值如上
//        // Notification mNotification = = new Notification(R.drawable.icon,"NotificationTest", System.currentTimeMillis()));
//
//        // 添加声音效果
//        mNotification.defaults |= Notification.DEFAULT_SOUND;
//
//        // 添加震动,后来得知需要添加震动权限 : Virbate Permission
//        //mNotification.defaults |= Notification.DEFAULT_VIBRATE ;
//
//        //添加状态标志
//
//        //FLAG_AUTO_CANCEL          该通知能被状态栏的清除按钮给清除掉
//        //FLAG_NO_CLEAR                 该通知能被状态栏的清除按钮给清除掉
//        //FLAG_ONGOING_EVENT      通知放置在正在运行
//        //FLAG_INSISTENT                通知的音乐效果一直播放
//        mNotification.flags = Notification.FLAG_INSISTENT ;
//
//        //将该通知显示为默认View
//        PendingIntent contentIntent = PendingIntent.getActivity
//                (NotificationActivity.this, 0,new Intent("android.settings.SETTINGS"), 0);
//        mNotification.setLatestEventInfo(NotificationActivity.this, "通知类型：默认View", "一般般哟。。。。",contentIntent);
//
//        // 设置setLatestEventInfo方法,如果不设置会App报错异常
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //注册此通知
//        // 如果该NOTIFICATION_ID的通知已存在，会显示最新通知的相关信息 ，比如tickerText 等
//        mNotificationManager.notify(2, mNotification);
//
//    }
//
//    private void removeNotification()
//    {
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        // 取消的只是当前Context的Notification
//        mNotificationManager.cancel(2);
//    }

}
