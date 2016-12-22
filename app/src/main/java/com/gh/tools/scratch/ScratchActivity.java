package com.gh.tools.scratch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.tools.common.T;
import com.gh.utils.views.scratch.FlipAnimator;
import com.gh.utils.views.scratch.ScratchImageView;
import com.gh.utils.views.scratch.ScratchTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author: gh
 * @description: 刮奖
 * @date: 2016/11/11 09:47.
 */

public class ScratchActivity extends BaseActivity {

    @Bind(R.id.id_tv_befor)
    TextView id_tv_befor;
    @Bind(R.id.id_tv_after)
    TextView id_tv_after;
    @Bind(R.id.id_fl_text)
    FrameLayout id_fl_text;
    @Bind(R.id.id_stv)
    ScratchTextView id_stv;
    @Bind(R.id.id_siv)
    ScratchImageView id_siv;

    /**
     * 启动ScratchActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ScratchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        ButterKnife.bind(this);
        id_tv_befor.setText("原价500");
        id_stv.setRevealListener(new ScratchTextView.IRevealListener() {
            @Override
            public void onRevealed(ScratchTextView tv) {
                showPrice();
            }
        });
        id_siv.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView tv) {
                T.S(ScratchActivity.this,"终于刮完了");
            }
        });
    }

    private void showPrice() {
        FlipAnimator animator = new FlipAnimator(id_tv_befor, id_tv_after, id_fl_text.getWidth()/2, id_fl_text.getHeight()/2);
        animator.setDuration(800);
        animator.setRotationDirection(FlipAnimator.DIRECTION_Y);
        id_fl_text.startAnimation(animator);
    }
}
