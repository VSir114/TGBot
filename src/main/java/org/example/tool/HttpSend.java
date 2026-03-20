package org.example.tool;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class HttpSend {

    public static String HttpGet(String url ,Map<String,String> heads) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request build = new Request
                .Builder()
                .headers(HeadInit(heads))
                .url(url).build();
        try(Response execute = okHttpClient.newCall(build).execute();) {
            return execute.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] HttpGetImage(String url ,Map<String,String> heads) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request build = new Request
                .Builder()
                .headers(HeadInit(heads))
                .url(url).build();
        try(Response execute = okHttpClient.newCall(build).execute()) {
            return execute.body().bytes();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Headers HeadInit(Map<String, String> map){
        Headers.Builder builder = new Headers.Builder();
        for(String key : map.keySet()){
            builder.add(key,map.get(key));
        }
        return builder.build();
    }
}
