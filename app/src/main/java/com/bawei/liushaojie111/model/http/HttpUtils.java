package com.bawei.liushaojie111.model.http;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Auther: 刘少杰
 * @Date: 2019/2/26 20:24:38
 * @Description:
 */
public class HttpUtils<T> {
    private final OkHttpClient okHttpClient;
    private HttpUtils(){
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new LogginIntecepter())
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public static HttpUtils getInstance(){
        return HttpUtilsInstance.httpUtils;
    }


    private static class HttpUtilsInstance{
        private static HttpUtils httpUtils = new HttpUtils();
    }

    private class LogginIntecepter implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            RequestBody body = request.body();
            Log.e("myMessage","");
            Headers headers = request.headers();
            Response response = chain.proceed(request);
            Headers headers1 = response.headers();

            return response;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                T t = (T) msg.obj;
                mCallback.onResponse(t);
            } else {
                String err = (String) msg.obj;
                mCallback.onFail(err);
            }

        }
    };

    private CallbackData mCallback;
    public void getData(String url, final Class<T> tClass, final CallbackData callbackData){
        this.mCallback = callbackData;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.what = -1;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                T t = gson.fromJson(string, tClass);
                Message message = handler.obtainMessage();
                message.obj = t;
                message.what = 0;
                handler.sendMessage(message);
            }
        });
    }

    public void postData(String url, final Class<T> tClass, HashMap<String,String> hashMap, final CallbackData callbackData){
        this.mCallback = callbackData;

        FormBody.Builder builder = new FormBody.Builder();


        for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            builder.add(key,value);
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.what = -1;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                T t = gson.fromJson(string, tClass);
                Message message = handler.obtainMessage();
                message.obj = t;
                message.what = 0;
                handler.sendMessage(message);
            }
        });
    }


    public interface CallbackData<D>{
         void onResponse(D d);
         void onFail(String err);
    }

}
