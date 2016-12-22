package com.gh.utils.tools.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 媒体播放工具
 */
public class MediaUtil {

    /**
     * 播放文件的唯一标识
     * 用于区别播放/暂停
     */
    private String mTag;

    private static final String TAG = "MediaUtil";

    private MediaPlayer player;
    private EventListener eventListener;

    private MediaUtil() {
        player = new MediaPlayer();
    }

    private static MediaUtil instance = new MediaUtil();

    public static MediaUtil getInstance() {
        return instance;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setEventListener(final EventListener eventListener) {
        if (player != null) {
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    eventListener.onStop();
                }
            });
        }
        this.eventListener = eventListener;
    }

    public void play(FileInputStream inputStream) {
//        stop();
        try {
            if (eventListener != null) {
                eventListener.onStop();
            }
            player.reset();
            player.setDataSource(inputStream.getFD());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "play error:" + e);
        }
    }

    public void play(FileInputStream inputStream, String tag) {
        //可以实现播放/暂停，但无法修改ui
//        if (tag != null && tag.equals(mTag) && player.isPlaying()) {
//            stop();
//            return;
//        }
        mTag = tag;
        play(inputStream);
    }

    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            eventListener.onStop();
        }
    }

    public long getDuration(Context context, String path) {
        player = MediaPlayer.create(context, Uri.parse(path));
        return player.getDuration();
    }

    /**
     * 播放器事件监听
     */
    public interface EventListener {
        void onStop();
    }

    /**
     * 以下两个方法用于确认该录音是否正在播放
     *
     * @return
     */
    public String getTag() {
        return mTag;
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }
}
