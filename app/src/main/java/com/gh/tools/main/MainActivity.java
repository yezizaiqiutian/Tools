package com.gh.tools.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.gh.tools.R;
import com.gh.tools.apkupload.ApkUploadActivity;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.bottomsheet.BottomSheetActivity;
import com.gh.tools.clip.ClipShowActivity;
import com.gh.tools.clock.ClockActivity;
import com.gh.tools.glide.CircleRoundActivity;
import com.gh.tools.glide.GlideDownLoadActivity;
import com.gh.tools.indexaberecyclerview.IndexableRVActivity;
import com.gh.tools.localPush.LocalPushActivity;
import com.gh.tools.luban.LuBanActivity;
import com.gh.tools.map.MapLinePlanActivity;
import com.gh.tools.net.NetActivity;
import com.gh.tools.notification.NotificationActivity;
import com.gh.tools.permission.PermissionsActivity;
import com.gh.tools.photo.PhotoActivity;
import com.gh.tools.saveViewAsBitmap.SaveViewActivity;
import com.gh.tools.scratch.ScratchActivity;
import com.gh.tools.scrollablePanel.ScrollablePanelActivity;
import com.gh.tools.selectDate.SelectDateActivity;
import com.gh.tools.selectTime.SelectTimeActivity;
import com.gh.tools.sign.SignActivity;
import com.gh.tools.webView.WebViewActivity;
import com.gh.utils.tools.common.T;
import com.gh.utils.tools.ui.statusbar.StatusBarUtil;
import com.gh.utils.views.recyclerview.base.BaseQuickAdapter;
import com.gh.utils.views.recyclerview.base.listener.OnItemClickListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.id_dl_root)
    DrawerLayout id_dl_root;

    @Bind(R.id.id_rv_sliding_list)
    RecyclerView id_rv_sliding_list;

    @Bind(R.id.id_rv_list)
    RecyclerView id_rv_list;

    @Bind(R.id.fab)
    FloatingActionButton mFabBtn;
    private MainAdapter mAdapter;
    private MainAdapter mSlidingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        mFabBtn.attachToRecyclerView(id_rv_list);
        mFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.S(MainActivity.this,"点击");
            }
        });
    }

    @Override
    public void initData() {
        id_rv_list.setHasFixedSize(true);
        id_rv_list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(this, R.layout.rv_item_text, getList());
        id_rv_list.setAdapter(mAdapter);

        id_rv_sliding_list.setHasFixedSize(true);
        id_rv_sliding_list.setLayoutManager(new LinearLayoutManager(this));
        mSlidingAdapter = new MainAdapter(this, R.layout.rv_item_text, getListSliding());
        id_rv_sliding_list.setAdapter(mSlidingAdapter);
    }

    @Override
    protected void initListener() {
        id_rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        NetActivity.actionStart(mContext);
                        break;
                    case 1:
                        SelectTimeActivity.actionStart(mContext);
                        break;
                    case 2:
                        SelectDateActivity.actionStart(mContext);
                        break;
                    case 3:
                        SignActivity.actionStart(mContext);
                        break;
                    case 4:
                        ClipShowActivity.actionStart(mContext);
                        break;
                    case 5:
                        PhotoActivity.actionStart(mContext);
                        break;
                    case 6:
                        NotificationActivity.actionStart(mContext);
                        break;
                    case 7:
                        CircleRoundActivity.actionStart(mContext);
                        break;
                    case 8:
                        GlideDownLoadActivity.actionStart(mContext);
                        break;
                    case 9:
                        MapLinePlanActivity.actionStart(mContext);
                        break;
                    case 10:
                        LocalPushActivity.actionStart(mContext);
                        break;
                    case 11:
                        ClockActivity.actionStart(mContext);
                        break;
                    case 12:
                        ScratchActivity.actionStart(mContext);
                        break;
                    case 13:
                        BottomSheetActivity.actionStart(mContext);
                        break;
                    case 14:
                        PermissionsActivity.actionStart(mContext);
                        break;
                    case 15:
                        WebViewActivity.actionStart(mContext);
                        break;
                    case 16:
                        ScrollablePanelActivity.actionStart(mContext);
                        break;
                    case 17:
                        SaveViewActivity.actionStart(mContext);
                        break;
                    case 18:
                        LuBanActivity.actionStart(mContext);
                        break;
                    case 19:
                        IndexableRVActivity.actionStart(mContext);
                        break;
                    case 20:
                        ApkUploadActivity.actionStart(mContext);
                        break;
                }
            }
        });
//        mAdapter.setOnRecyclerViewItemClickListener(this);
//        mSlidingAdapter.setOnRecyclerViewItemClickListener(this);
    }

    public List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add("net");
        list.add("时间选择");
        list.add("日期选择");
        list.add("签名");
        list.add("截取头像");
        list.add("拍照相册");
        list.add("通知栏");
        list.add("圆图/圆角");
        list.add("图片缓存");
        list.add("地图线路规划");
        list.add("本地推送");
        list.add("设置时钟");
        list.add("刮奖");
        list.add("bottomsheet");
        list.add("6.0权限");
        list.add("WebView");
        list.add("ScrollablePanel");
        list.add("把控件的样子保存成Bitmap/图片文件");
        list.add("LuBan压缩工具用法");
        list.add("indexablerecyclerview");
        list.add("Apk更新");
        list.add("截取头像");
        list.add("截取头像");
        list.add("截取头像");
        list.add("截取头像");

        return list;
    }

    public List<String> getListSliding() {
        List<String> list = new ArrayList<>();
        list.add("net");
        list.add("时间选择");
        list.add("签名");
        list.add("截取头像");
        list.add("通知栏");

        return list;
    }

    /**
     * 返回不退出
     * 类似qq
     * @param keyCode
     * @param event
     * @return
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 二次返回退出
     */
    private static Boolean isExit = false;
    private Boolean hasTask = false;
    Timer tExit = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit == false) {
                isExit = true;
                Toast.makeText(this, "再次按返回键退出程序", Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                    tExit.schedule(task, 3000);
                }
            } else {
                isExit = false;
                finish();
            }
        }
        return false;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.id_dl_root), getResources().getColor(R.color.colorAccent), StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

}
