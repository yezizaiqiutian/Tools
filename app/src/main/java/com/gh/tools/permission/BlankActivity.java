package com.gh.tools.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gh.tools.base.BaseActivity;
import com.gh.utils.permission.Acp;
import com.gh.utils.permission.AcpListener;
import com.gh.utils.permission.AcpOptions;

import java.util.List;

/**
 * @author: gh
 * @description: 6.0权限管理
 * @date: 2016/11/22 14:19.
 */

public class BlankActivity extends BaseActivity{

    /**
     * 启动BlankActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BlankActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, BlankFragment.newInstance()).commitAllowingStateLoss();
    }

    public static class BlankFragment extends Fragment implements View.OnClickListener {

        public BlankFragment() {
        }


        public static BlankFragment newInstance() {
            BlankFragment fragment = new BlankFragment();
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setText("点我试试imei权限");
            textView.append("\n\nFragment中申请权限");
            textView.setOnClickListener(this);
            return textView;
        }

        @Override
        public void onClick(View v) {
            Acp.getInstance(this.getContext()).request(new AcpOptions.Builder()
                            .setPermissions(Manifest.permission.READ_PHONE_STATE).build(),
                    new AcpListener() {
                        @Override
                        public void onGranted() {
                            getIMEI();
                        }

                        @Override
                        public void onDenied(List<String> permissions) {
                            Toast.makeText(BlankFragment.this.getActivity(), permissions.toString() + "权限拒绝", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void getIMEI() {
            FragmentActivity activity = this.getActivity();
            TelephonyManager tm = (TelephonyManager) this.getActivity().getSystemService(Activity.TELEPHONY_SERVICE);
            if (tm != null)
                Toast.makeText(activity, tm.getDeviceId(), Toast.LENGTH_SHORT).show();
        }
    }
}
