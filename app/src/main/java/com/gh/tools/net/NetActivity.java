package com.gh.tools.net;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.net.RxHelper;
import com.gh.utils.net.RxRetrofitCache;
import com.gh.utils.net.RxSubscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/**
 * @author: gh
 * @description: 联网retrofit+rxjava
 * @date: 2016/10/14 17:06.
 */
public class NetActivity extends BaseActivity {

    @Bind(R.id.id_btn)
    Button idBtn;
    @Bind(R.id.id_tv)
    TextView idTv;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_btn)
    public void onClick() {
        Observable<ArrayList<Benefit>> fromNetwork = Api.getDefault()
                .rxBenefits(10, 1)
                .compose(RxHelper.<ArrayList<Benefit>>handleResult());
        RxRetrofitCache.load(this, "cacheKey", 10 * 60 * 60, fromNetwork, false)
                .subscribe(new RxSubscribe<ArrayList<Benefit>>(this, "正在下载福利") {
                    @Override
                    protected void _onNext(ArrayList<Benefit> befits) {
                        idTv.setText("获得的结果为:" + befits.toString());
                    }

                    @Override
                    protected void _onError(String message) {
                        Toast.makeText(NetActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
