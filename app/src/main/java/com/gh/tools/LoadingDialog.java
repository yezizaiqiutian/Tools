package com.gh.tools;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gh.utils.R;

import butterknife.ButterKnife;

/**
 * 加载对话框
 */
public class LoadingDialog extends DialogFragment {

    private String tag = "UpdateDialog";
    private TextView id_tv_msg;
    private boolean cancelable = false;
    private OnCancleListener mOnCancleListener;
    private String msg;

    public void show(boolean cancelable, FragmentManager fm) {
        show(cancelable, fm, "努力加载中...");
    }

    public void show(boolean cancelable, FragmentManager fm, String msg) {
        this.cancelable = cancelable;
        this.msg = msg;
        setCancelable(this.cancelable);
//        id_tv_msg.setText(mes);
        show(fm, tag);
    }

    public void stop() {
        if (null != this && this.getDialog() != null && this.getDialog().isShowing()) {
            this.dismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mOnCancleListener != null) {
            mOnCancleListener.onCancle(dialog);
        }
        super.onCancel(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(com.gh.utils.R.layout.dialog_loading, container);
        id_tv_msg = (TextView) view.findViewById(R.id.id_tv_msg);
        id_tv_msg.setText(msg);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface OnCancleListener {
        void onCancle(DialogInterface view);
    }

    public void setOnCancleListener(OnCancleListener onCancleListener) {
        mOnCancleListener = onCancleListener;
    }

}
