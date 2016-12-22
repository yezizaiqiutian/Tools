package com.gh.tools.webView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.tools.common.T;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author: gh
 * @description: WebView的使用
 * @date: 2016/12/2 15:17.
 * @from: http://mp.weixin.qq.com/s?__biz=MzI3NDM3Mjg5NQ==&mid=2247483682&idx=1&sn=b1e03bfb789f75467c351a8ed7dfc156&scene=0#rd
 */

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.id_tv_center_title)
    TextView id_tv_center_title;
    @Bind(R.id.id_webView)
    WebView id_webView;
    @Bind(R.id.id_progressbar)
    ProgressBar id_progressbar;
    @Bind(R.id.id_tv_reload)
    TextView id_tv_reload;

    //记录出错的页面
    private String mFailingUrl;

    //错误页面为本地
    //是否加载错误
//    private boolean mLoadError;

    /**
     * 启动PermissionsActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        id_tv_center_title.setText("载入中...");
    }

    @Override
    protected void initView() {
        super.initView();
        initWebView();
        initWebSettings();
        initWebViewClient();
        initWebChromeClient();
    }

    private void initWebView() {
        id_webView.loadUrl("http://www.baidu.com");
    }

    private void initWebSettings() {
        WebSettings settings = id_webView.getSettings();
        //支持获取手势焦点
        id_webView.requestFocusFromTouch();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //禁止或允许WebView从网络上加载图片。需要注意的是，如果设置是从禁止到允许的转变的话，图片数据并不会在设置改变后立刻去获取，而是在WebView调用reload()的时候才会生效。
        //这个时候，需要确保这个app拥有访问Internet的权限，否则会抛出安全异常。
        //通常没有禁止图片加载的需求的时候，完全不用管这个方法，因为当我们的app拥有访问Internet的权限时，这个flag的默认值就是false。
        settings.setBlockNetworkImage(false);
        //webView从5.0开始默认不允许混合模式，https当中不能加载http资源（图片），需要设置开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //支持插件
        settings.setPluginState(WebSettings.PluginState.ON);
        //设置适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持缩放
        settings.setSupportZoom(false);
        //隐藏原生的缩放控件
        settings.setDisplayZoomControls(false);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(id_webView.getContext().getCacheDir().getAbsolutePath());

        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持自动加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        settings.setNeedInitialFocus(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
        //用于webView调用原生方法
        id_webView.addJavascriptInterface(new JsInterface(), "jsinterface");
    }

    private void initWebViewClient() {
        id_webView.setWebViewClient(new WebViewClient() {

            /**
             * 该方法在WebView开始加载页面且仅在Main frame loading（即整页加载）时回调，一次Main frame的加载只会回调该方法一次。
             * 我们可以在这个方法里设定开启一个加载的动画，告诉用户程序在等待网络的响应。
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //显示进度
                id_progressbar.setVisibility(VISIBLE);
            }

            /**
             * 该方法只在WebView完成一个页面加载时调用一次（同样也只在Main frame loading时调用），
             * 我们可以可以在此时关闭加载动画，进行其他操作。
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //隐藏进度
                id_progressbar.setVisibility(GONE);

                //错误页面为本地
//                if (!mLoadError) {
//                    id_webView.setVisibility(VISIBLE);
//                    id_tv_reload.setVisibility(GONE);
//                    mLoadError = false;
//                }
            }

//            /**
//             * 拦截不到
//             * url拦截
//             * 再试没用到
//             * @param view
//             * @param request
//             * @return
//             */
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                //返回true为webView不跳转
//                //回false为webView跳转
//                T.S(WebViewActivity.this, "点击效果");
//                Log.e("gh", request.toString());
//                return true;
////                return super.shouldOverrideUrlLoading(view, request);
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //方式1
                //重定向无问题
                //返回true为webView显示
                //回false为默认浏览器显示
//                if (!url.contains("https://m.baidu.com/?from=844b&vit=fps")) {
//                    T.S(WebViewActivity.this, "点击效果");
//
//                } else {
//                    view.loadUrl(url);
//                }
//                return true;

                //方式2
                //重定向无问题
//                WebView.HitTestResult hitTestResult = view.getHitTestResult();
//                //hitTestResult==null解决重定向问题
//                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
//                    view.loadUrl(url);
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, url);

                //方式3
                //重定向无问题
                return false;
            }

            /**
             * 该方法在加载页面资源时会回调，每一个资源（比如图片）的加载都会调用一次。
             * @param view
             * @param url
             */
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //网络错误的逻辑,比如显示一个错误页面
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mFailingUrl = failingUrl;
                //加载出错的自定义界面
                //错误页面H5做
                view.loadUrl("file:///android_asset/error.html");
                //错误页面为本地
//                id_webView.setVisibility(GONE);
//                id_tv_reload.setVisibility(VISIBLE);
//                mLoadError = true;
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });

    }

    private void initWebChromeClient() {
        id_webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //获取标题
                id_tv_center_title.setText(title);
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
//                当我们的Web页面包含视频时，我们可以在HTML里为它设置一个预览图，WebView会在绘制页面时根据它的宽高为它布局。
//                而当我们处于弱网状态下时，我们没有比较快的获取该图片，那WebView绘制页面时的gitWidth()方法就会报出空指针异常~ 于是app就crash了。。
//                这时我们就需要重写该方法，在我们尚未获取web页面上的video预览图时，给予它一个本地的图片，避免空指针的发生。
                return super.getDefaultVideoPoster();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e("newProgress", newProgress + "");
                if (newProgress >= 100) {
                    id_progressbar.setVisibility(GONE);
                } else {
                    if (id_progressbar.getVisibility() == GONE)
                        id_progressbar.setVisibility(VISIBLE);
                    id_progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);

            }
        });
    }

    //错误页面为本地
//    @OnClick(R.id.id_tv_reload)
//    public void onClick() {
//        if (mFailingUrl != null) {
//            id_webView.loadUrl(mFailingUrl);
//            mLoadError = false;
//        }
//    }

    public class JsInterface {

        @JavascriptInterface
        public void log(final String msg) {
            id_webView.post(new Runnable() {
                @Override
                public void run() {
                    id_webView.loadUrl("javascript: log(" + "'" + msg + "'" + ")");
                }
            });
        }

        //错误页面H5做
        @JavascriptInterface
        public void onError() {
            T.S(mActivity, "刷新");
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mFailingUrl != null) {
                        id_webView.loadUrl(mFailingUrl);
                    }
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && id_webView.canGoBack()) {
            id_webView.goBack();
            return true;
        } else {

        }
        return super.onKeyDown(keyCode, event);
    }
}
