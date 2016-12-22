package com.gh.tools.saveViewAsBitmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description: 把控件的样子保存成Bitmap/图片文件
 * @date: 2016/12/7 17:10
 * @note:
 */

public class SaveViewActivity extends BaseActivity {

    @Bind(R.id.id_ll_show)
    LinearLayout id_ll_show;
    @Bind(R.id.id_iv_showsave)
    ImageView id_iv_showsave;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SaveViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveview);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_btn_savebitmap, R.id.id_btn_savefile})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_savebitmap:
                id_iv_showsave.setImageBitmap(getBitmapFromView(id_ll_show));
                break;
            case R.id.id_btn_savefile:
                //存储到文件自己实现
                break;
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
