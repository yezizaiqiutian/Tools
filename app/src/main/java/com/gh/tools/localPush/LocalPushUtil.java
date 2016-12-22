//package com.gh.tools.localPush;
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.gh.tools.R;
//
//import java.util.Calendar;
//
//import static android.R.attr.id;
//
///**
// * @author: gh
// * @description:
// * @date: 2016/11/10 14:54.
// */
//
//public class LocalPushUtil {
//
//    public static String PushAction = "cn.XXX.PushAction";
//
//    public static String pushData="1|2|09:50|内容^2|2|09:58|内容";  // id|类型|时间|内容
//
//    /**
//     *
//     */
//    设置重复型闹钟
//    public static void setMorePush(Activity activity) {
//        SharedPreferences sharedPreferences = activity.getContext().getSharedPreferences("SP", activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("key", pushData);
//        editor.commit();
//
//        Intent intent =new Intent(activity.getContext(), PushReceiver.class);
//        intent.setAction(PushAction);
//        PendingIntent sender=PendingIntent.getBroadcast(activity.getContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager alarm=(AlarmManager)activity.getContext().getSystemService(Context.ALARM_SERVICE);
//        alarm.setRepeating(AlarmManager.RTC,System.currentTimeMillis(),60*1000, sender);// --设置每隔一分钟发送一次PushAction 设置重复执行
//    }
//
//
//
//
//    设置一次型闹钟
//
//    long t = Long.parseLong(time)*1000+System.currentTimeMillis();
//    Intent intent =new Intent(Cocos2dxActivity.getContext(), PushReceiver.class);
//    intent.setAction(PushAction);
//    intent.putExtra("id", id);//--注意这个id最好唯一，假如设置多条推送时 ，id必须唯一 要不就乱了
//    intent.putExtra("content", body);
//    intent.putExtra("type",2); //对应PushReceiver 类型判断
//    PendingIntent sender=PendingIntent.getBroadcast(Cocos2dxActivity.getContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT); --注意第二个参数 一定唯一 当有多条推送的时候
//    AlarmManager alarm=(AlarmManager)Cocos2dxActivity.getContext().getSystemService(Context.ALARM_SERVICE);
//    alarm.set(AlarmManager.RTC, t, sender);--从当前开始 间隔time之后 触发推送
//
//    触发推送的实现 PushReceiver类
//
//    @Override
//    public void onReceive(Context arg0, Intent intent) {
//// TODO Auto-generated method stub
//        if (intent.getAction().equals(Push.PushAction))
//        {
//            pushNotify(arg0); // 设置重复性推送
//            if(intent.getIntExtra("type",0) ==2){//对应之前一次型推送里面的类型
//                sendNotify1(intent.getIntExtra("id",0),intent.getStringExtra("content"),arg0);
//                }
//            }
//    }
//
//
//    public static void pushNotify(Context ctx) {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences("SP", Cocos2dxActivity.MODE_PRIVATE);
//        String con = sharedPreferences.getString("key", "");
//        Log.e("EEEE", con);
//        String temp[] = con.split("\\^");
//        if (temp.length<=0) return;
//        int week =Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
//
//        int hour =Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        String strHour = "";
//        if (hour<=9)
//        {
//            strHour = "0"+hour;
//            } else {
//            strHour = hour+"";
//            }
//
//        int mimute = Calendar.getInstance().get(Calendar.MINUTE);
//        String strMimute= "";
//        if (mimute<=9) {
//            strMimute ="0"+mimute;
//            }
//        else {
//            strMimute = mimute+"";
//            }
//        for(int i=0;i<temp.length;i++)
//        {
//            String pushStr[] = temp[i].split("\\|");
//            int id = Integer.parseInt(pushStr[0]) ;
//            int type = Integer.parseInt(pushStr[1]) ;
//            String time = pushStr[2];
//            String content = pushStr[3];
//            switch (type) {
//                case 2: //设置几点几分的推送
//                    String t =strHour+":"+strMimute;
//                    if (time.equals(t)){
//                        sendNotify1(id, content,ctx);
//                        }
//                    break;
//                case 3: //星期几几点几分的推送
//                    int tempWeek =0;
//                    switch (week) {
//                        case 1:
//                            tempWeek = 7;
//                            break;
//                        case 2:
//                            tempWeek = 1;
//                            break;
//                        case 3:
//                            tempWeek = 2;
//                            break;
//                        case 4:
//                            tempWeek = 3;
//                            break;
//                        case 5:
//                            tempWeek = 4;
//                            break;
//                        case 6:
//                            tempWeek = 5;
//                            break;
//                        case 7:
//                            tempWeek = 6;
//                            break;
//
//                        default:
//                            break;
//                        }
//                    String t1 =tempWeek+":"+strHour+":"+strMimute;
//                    if (time.equals(t1)){
//                        sendNotify1(id, content,ctx);
//                        }
//                    week = 0;
//                    break;
//                default:
//                    break;
//                }
//        }
//
//    }
//
//    @SuppressWarnings("deprecation") //设置推送
//    public static void sendNotify1(final int id,final String body,final Context ctx)
//    {
//        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Notification noti = new Notification(R.mipmap.ic_launcher, body,System.currentTimeMillis());
//
//        noti.defaults = Notification.DEFAULT_SOUND;
//
//        String title = ctx.getString(R.string.app_name);
//
//        noti.flags = Notification.FLAG_AUTO_CANCEL;
//
//        Intent intent = new Intent(ctx, Pokemon.class);
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(ctx, id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        noti.setLatestEventInfo(ctx,title, body, contentIntent);
//
//        nm.notify(id, noti);
//    }
//
//}
