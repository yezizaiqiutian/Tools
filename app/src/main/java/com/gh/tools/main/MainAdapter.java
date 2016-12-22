package com.gh.tools.main;

import android.content.Context;

import com.gh.tools.R;
import com.gh.utils.views.recyclerview.base.BaseQuickAdapter;
import com.gh.utils.views.recyclerview.base.BaseViewHolder;

import java.util.List;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/14 17:06.
 */
public class MainAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MainAdapter(Context context, int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.id_tv, item);
    }
}
