package com.gh.tools.greendao;

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
public class GreenDaoAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public GreenDaoAdapter(Context context, int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.id_tv_id, item.getId()+"");
        helper.setText(R.id.id_tv_name, item.getName());
    }
}
