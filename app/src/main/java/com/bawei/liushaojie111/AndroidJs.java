package com.bawei.liushaojie111;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * @Auther: 刘少杰
 * @Date: 2019/3/7 20: 20:31:58
 * @Description:
 */
public class AndroidJs {

        @JavascriptInterface
        public void hello(String msg){
            Log.e("my","androidtojs"+msg);
        }

}
