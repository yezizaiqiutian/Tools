package com.gh.tools.sign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.views.sign.views.SignaturePad;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gh.tools.R.id.id_sp_sign;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/18 11:35.
 */

public class SignActivity extends BaseActivity {

    @Bind(id_sp_sign)
    SignaturePad idSpSign;
    @Bind(R.id.id_iv_image_show)
    ImageView idIvImageShow;

    /**
     * 启动SignActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SignActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
//        idSpSign.setBackgroundView(BitmapFactory.decodeResource(getResources(), R.mipmap.sign_back));
        idSpSign.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {
//                idSpSign.setSignatureBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sign_back));
//                idIvImageShow.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            }
        });
    }

    @OnClick({R.id.id_btn_build, R.id.id_btn_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_build:
//                Bitmap bitmap = idSpSign.getSignatureBitmap();
                Bitmap bitmap = idSpSign.getTransparentSignatureBitmap();
                idIvImageShow.setImageBitmap(bitmap);
                break;
            case R.id.id_btn_clear:
                idSpSign.clear();
                break;
        }
    }
}
