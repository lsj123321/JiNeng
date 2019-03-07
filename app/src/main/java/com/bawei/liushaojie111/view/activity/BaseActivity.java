package com.bawei.liushaojie111.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @Auther: 刘少杰
 * @Date: 2019/2/26 20:32:16
 * @Description:
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bindView());

        initView();
        initData();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract int bindView();
}
