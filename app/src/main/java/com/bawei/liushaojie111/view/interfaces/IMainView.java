package com.bawei.liushaojie111.view.interfaces;

/**
 * @Auther: 刘少杰
 * @Date: 2019/2/26 20:28:31
 * @Description:
 */
public interface IMainView<T> extends IBaseView {
    void onSuccess(T t);
    void onErr(String errMessage);
}
