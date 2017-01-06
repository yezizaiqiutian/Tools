package com.gh.tools.greendao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.base.MyApplication;
import com.gh.tools.greendao.gen.UserDao;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description: 数据库greendao的简单使用
 * @date: 2017/1/6 11:14
 * @note: 数据是否可以重复(姓名):
 * 如果可以从复,删除时删除单条会报错,更新时不知道该更新哪个,可以查询时查所有全部操作,感觉不合理
 * 如果不可以重复,插入时需要判断  现在采用这种
 */

public class GreenDaoActivity extends BaseActivity {

    @Bind(R.id.id_et_name)
    EditText id_et_name;
    @Bind(R.id.id_et_name2)
    EditText id_et_name2;
    @Bind(R.id.id_rv_list)
    RecyclerView id_rv_list;

    private GreenDaoAdapter mAdapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GreenDaoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greendao);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        id_rv_list.setHasFixedSize(true);
        id_rv_list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GreenDaoAdapter(this, R.layout.rv_item_greendao, selectAll());
        id_rv_list.setAdapter(mAdapter);
    }

    /**
     * 数据库-查询所有
     * @return
     */
    private List<User> selectAll() {
        return GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder().build().list();
    }

    /**
     * 数据库-插入一条
     * @param user
     */
    private void insertUser(User user) {
        if (!selectUser(user).isEmpty()) {
            Toast.makeText(MyApplication.getContext(), "数据已存在", Toast.LENGTH_SHORT).show();
            return;
        }
        GreenDaoManager.getInstance().getSession().getUserDao().insert(user);
    }

    /**
     * 数据库-删除一条
     * @param user
     */
    private void deleteUser(User user) {
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        //如果有两个一样的名字会报错
        User findUser = userDao.queryBuilder().where(UserDao.Properties.Name.eq(user.getName())).build().unique();
        if (findUser != null) {
            userDao.deleteByKey(findUser.getId());
        }
    }

    /**
     *数据库-查询一条
     * @param user
     */
    private List<User> selectUser(User user) {
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        List<User> list = userDao.queryBuilder()
                .where(UserDao.Properties.Name.eq(user.getName())).build().list();
        return list;
    }

    /**
     * 数据库-更新一条
     * @param oldUser
     * @param newUser
     */
    private void updateUser(User oldUser, User newUser) {
        User findUser = GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.Name.eq(oldUser.getName())).build().unique();
        if(findUser != null) {
            findUser.setName(newUser.getName());
            GreenDaoManager.getInstance().getSession().getUserDao().update(findUser);
            Toast.makeText(MyApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.getContext(), "用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.id_btn_add, R.id.id_btn_delete, R.id.id_btn_update, R.id.id_btn_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_add:
                insertUser(new User(null, id_et_name.getText().toString()));
                mAdapter.getData().clear();
                mAdapter.addData(selectAll());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.id_btn_delete:
                deleteUser(new User(null, id_et_name.getText().toString()));
                mAdapter.setNewData(selectAll());
                break;
            case R.id.id_btn_update:
                updateUser(new User(null, id_et_name.getText().toString()), new User(null, id_et_name2.getText().toString()));
                mAdapter.setNewData(selectAll());
                break;
            case R.id.id_btn_select:
                mAdapter.setNewData(selectUser(new User(null, id_et_name.getText().toString())));
                break;
        }
    }
}
