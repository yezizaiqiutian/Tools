package com.gh.tools.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.gh.tools.R;
import com.gh.utils.tools.ui.ActivityCollector;
import com.gh.utils.tools.ui.statusbar.StatusBarUtil;

/**
 * @author: gh
 * @description: Activity基类
 * 设置状态栏颜色
 * 记住每个打开的Activity以便退出调用
 * @date: 2016/10/14 17:06.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext = null;
    protected Activity mActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;

        //设置没有Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //记住每个Activity,用于退出登录
        ActivityCollector.addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    /**
     * 当Activity彻底运行起来之后回调onPostCreate方法
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initBundle();
        initTitle();
        initView();
        initData();
        initListener();
        initLoad();
    }

    /**
     * 设置状态栏颜色,需要在初始化View后调用
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 获取上个界面传输的intent
     */
    private void initBundle() {
    }

    /**
     * Title
     */
    protected void initTitle() {
    }

    /**
     * 加载布局
     */
    protected void initView() {
    }

    /**
     * 加载数据
     */
    protected void initData() {
    }

    /**
     * 加载监听
     */
    protected void initListener() {
    }

    /**
     * 加载url
     */
    protected void initLoad() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记住每个Activity,用于退出登录
        ActivityCollector.removeActivity(this);
    }
}
