package com.bawei.liushaojie111.view.activity;

import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.bawei.liushaojie111.AndroidJs;
import com.bawei.liushaojie111.R;

/**
 * @Auther: 刘少杰
 * @Date: 2019/3/7 20: 20:10:48
 * @Description:
 */
public class JavaScript extends BaseActivity {

    private Button button;
    private WebView webView;

    @Override
    public void initView() {
        button = findViewById(R.id.but);
        webView = findViewById(R.id.web);
    }

    @Override
    public void initData() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:callJS()");
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new AndroidJs(),"test");
        webView.loadUrl("file:///android_asset/javascript.html");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }




    @Override
    public int bindView() {
        return R.layout.activity_js;
    }
}
