package com.gh.tools.map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.tools.project.AppUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description:
 * @date: 2016/11/10 11:20.
 */

public class MapLinePlanActivity extends BaseActivity {

    private int[] location = {40, 116};
    private String[] from_location = {"呵呵呵", "36.2", "116.1"};
    private String[] to_location = {"哈哈哈", "36.3", "116.2"};

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MapLinePlanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_line_plan);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_btn_gaode, R.id.id_btn_baidu, R.id.id_btn_google})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_gaode:
                toGaoDe();
                break;
            case R.id.id_btn_baidu:
                toBaiDu();
                break;
            case R.id.id_btn_google:
                toGoogle();
                break;
        }
    }

    /**
     * 跳转到百度地图
     */

//    参数名称	描述	是否必选	格式(示例)
//    origin	起点名称或经纬度，或者可同时提供名称和经纬度，此时经纬度优先级高，将作为导航依据，名称只负责展示	origin和destination
//    二者至少一个有值（默认值是当前定位地址）	name:中关村 (注意：坐标先纬度，后经度)
//    destination	终点名称或经纬度，或者可同时提供名称和经纬度，此时经纬度优先级高，将作为导航依据，名称只负责展示。	同上	name:中关村 (注意：坐标先纬度，后经度)
//    mode	导航模式，
//    固定为transit（公交）、
//    driving（驾车）、
//    walking（步行）和riding（骑行）.
//    默认:driving	可选
//    region	城市名或县名	可选
//    origin_region	起点所在城市或县	可选
//    origin_region	起点所在城市或县	可选
//    destination_region	终点所在城市或县	可选
//    sy	公交检索策略，只针对公交检索有效，值为数字。
//            0：推荐路线
//    2：少换乘
//    3：少步行
//    4：不坐地铁
//    5：时间短
//    6：地铁优先	可选
//    index	公交结果结果项，只针对公交检索，值为数字，从0开始	可选
//    target	0 图区，1 详情，只针对公交检索有效	可选
    private void toBaiDu() {
        Intent intent;
        if (AppUtils.isAvilible(this, "com.baidu.BaiduMap")) {//传入指定应用包名
            intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/direction?origin=name:呵呵呵|latlng:36.2,116.1&destination=name:哈哈哈|latlng:36.3,116.2&mode=transit&sy=3&index=0&target=0"));
            intent.setData(Uri.parse("baidumap://map/direction?origin=name:"+from_location[0]+"|latlng:"+from_location[1]+","+from_location[2]+"&destination=name:"+to_location[0]+"|latlng:"+to_location[1]+","+to_location[2]+"&mode=transit&sy=3&index=0&target=0"));
            this.startActivity(intent); //启动调用
        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
            Toast.makeText(this, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            this.startActivity(intent);
        }
    }

    /**
     * 高德
     */
//    参数	说明	是否必填
//    route	服务类型	是
//    sourceApplication	第三方调用应用名称。如 amap	是
//    slat	起点纬度	是
//    slon	起点经度	是
//    sname	起点名称	否
//    dlat	终点纬度	是
//    dlon	终点经度	是
//    dname	终点名称	否
//    dev	起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)	是
//    m	驾车方式 =0（速度快）=1（费用少） =2（路程短）=3 不走高速 =4（躲避拥堵）=5（不走高速且避免收费） =6（不走高速且躲避拥堵） =7（躲避收费和拥堵） =8（不走高速躲避收费和拥堵）。 公交 =0（速度快）=1（费用少） =2（换乘较少）=3（步行少）=4（舒适）=5（不乘地铁）
//
//    由于与用户本地设置冲突，Android平台7.5.9版本起不支持该参数，偏好设置将以用户本地设置为准	是
//    t	t = 1(公交) =2（驾车） =4(步行)
    private void toGaoDe() {
        Intent intent;
        if (AppUtils.isAvilible(this, "com.autonavi.minimap")) {
            intent = new Intent();
            intent.setData(Uri.parse("androidamap://route?sourceApplication=跟上&slat="+from_location[1]+"&slon="+from_location[2]+"&sname="+from_location[0]+"&dlat="+to_location[1]+"&dlon="+to_location[2]+"&dname="+to_location[0]+"&dev=0&t=1"));
            this.startActivity(intent); //启动调用
        } else {
            Toast.makeText(this, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            this.startActivity(intent);
        }
    }

    public void toGoogle() {
        Intent intent;
        if (AppUtils.isAvilible(this, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + location[0] + "," + location[1] + ", + Sydney +Australia");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            this.startActivity(mapIntent);
        } else {
            Toast.makeText(this, "您尚未安装谷歌地图", Toast.LENGTH_LONG).show();

            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            this.startActivity(intent);
        }
    }
}
