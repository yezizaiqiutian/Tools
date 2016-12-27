package com.gh.tools.base;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/18 17:33.
 */

public interface BaseView {

    void showLoading();

    void hideLoading();

    void showToast(String msg);

    void showToast(int msg);

}
