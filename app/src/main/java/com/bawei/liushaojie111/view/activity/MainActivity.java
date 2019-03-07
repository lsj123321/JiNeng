package com.bawei.liushaojie111.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.liushaojie111.R;
import com.bawei.liushaojie111.app.App;
import com.bawei.liushaojie111.model.bean.ShoppingCartBean;
import com.bawei.liushaojie111.view.adapter.ShoppingCartAdapter;
import com.igexin.sdk.PushManager;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ShoppingCartBean shoppingCartBean;
    private CheckBox checkAll;
    private TextView price_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new App());

        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), com.bawei.liushaojie111.um.DemoPushService.class);
// com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),com.bawei.liushaojie111.um.DemoIntentService.class);

        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }


        Button button = findViewById(R.id.but);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMShareAPI shareAPI = UMShareAPI.get(MainActivity.this);
                shareAPI.getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, authListener);
            }
        });

        initTestData();
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            String iconurl = data.get("iconurl");

            Toast.makeText(MainActivity.this, "成功了", Toast.LENGTH_LONG).show();

        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(MainActivity.this, "失败：" + t.getMessage(),                                     Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    private void initView() {
        expandableListView = findViewById(R.id.expandableListView);
        checkAll = findViewById(R.id.check_All);
        price_all = findViewById(R.id.pricr_All);
        ShoppingCartAdapter cartAdapter=new ShoppingCartAdapter(this);
        expandableListView.setAdapter(cartAdapter);
        for (int i = 0; i < shoppingCartBean.getData().size(); i++) {
            expandableListView.expandGroup(i);
        }
        cartAdapter.setData(shoppingCartBean);
        cartAdapter.setCheckBox(checkAll);
        cartAdapter.setTextView(price_all);
    }

    private void initTestData() {
        shoppingCartBean = new ShoppingCartBean();
        ArrayList all = new ArrayList();
        ArrayList arrayListA = new ArrayList();
        ArrayList arrayListB = new ArrayList();

        ShoppingCartBean.DataBean.ListBean goods1 = new ShoppingCartBean.DataBean.ListBean();
        goods1.setTitle("商品1");
        goods1.setPrice("10");
        goods1.setNum("1");
        goods1.setCheck(false);

        ShoppingCartBean.DataBean.ListBean goods2 = new ShoppingCartBean.DataBean.ListBean();
        goods2.setTitle("商品2");
        goods2.setPrice("20");
        goods2.setNum("1");
        goods2.setCheck(false);

        ShoppingCartBean.DataBean.ListBean goods3 = new ShoppingCartBean.DataBean.ListBean();
        goods3.setTitle("商品3");
        goods3.setPrice("30");
        goods3.setNum("1");
        goods3.setCheck(false);

        ShoppingCartBean.DataBean.ListBean goods4 = new ShoppingCartBean.DataBean.ListBean();
        goods4.setTitle("商品4");
        goods4.setPrice("40");
        goods4.setNum("1");
        goods4.setCheck(false);

        arrayListA.add(goods1);
        arrayListA.add(goods2);
        arrayListB.add(goods3);
        arrayListB.add(goods4);

        ShoppingCartBean.DataBean dataBean1 = new ShoppingCartBean.DataBean();
        dataBean1.setList(arrayListA);
        dataBean1.setSellerName("天猫");

        ShoppingCartBean.DataBean dataBean2 = new ShoppingCartBean.DataBean();
        dataBean2.setList(arrayListB);
        dataBean2.setSellerName("京东");
        all.add(dataBean1);
        all.add(dataBean2);
        shoppingCartBean.setData(all);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
