package com.gh.tools.glide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.tools.glide.transform.GlideCircleTransform;
import com.gh.utils.tools.glide.transform.GlideRoundTransform;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author: gh
 * @description:
 * @date: 2016/11/3 16:15.
 */

public class CircleRoundActivity extends BaseActivity {

    @Bind(R.id.id_iv_circle)
    ImageView id_iv_circle;
    @Bind(R.id.id_iv_round)
    ImageView id_iv_round;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CircleRoundActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_round);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        Glide.with(this).load(R.mipmap.nav_header_bg).transform(new GlideCircleTransform(this)).into(id_iv_circle);
        Glide.with(this).load(R.mipmap.nav_header_bg).transform(new GlideRoundTransform(this)).into(id_iv_round);
    }
}
