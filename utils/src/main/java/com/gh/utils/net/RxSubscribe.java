package com.gh.utils.net;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import rx.Subscriber;

/**
 * author: gh
 * time: 2016/10/5.
 * description:
 */

public abstract class RxSubscribe<T> extends Subscriber<T>{

    private Context mContext;
    private LoadingDialog dialog = new LoadingDialog();
    private String msg;

    protected boolean showDialog() {
        return true;
    }

    public RxSubscribe(Context context, String msg) {
        this.mContext = context;
        this.msg = msg;
    }

    public RxSubscribe(Context context) {
        this(context, "请稍后...");
    }

    @Override
    public void onCompleted() {
        if (showDialog()) {
            dialog.stop();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (showDialog()) {
            dialog.setOnCancleListener(new LoadingDialog.OnCancleListener() {
                @Override
                public void onCancle(DialogInterface view) {
                    unsubscribe();
                }
            });
            dialog.show(true,((Activity)mContext).getFragmentManager(),"玩命加载中");
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (false) {
            _onError("网络不可用");
        } else if (e instanceof ServerException) {
            _onError(e.getMessage());
        } else {
            _onError("请求失败,请稍后再试...");
        }
        if (showDialog()) {
            dialog.stop();
        }
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
}
