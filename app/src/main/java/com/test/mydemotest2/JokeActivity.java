package com.test.mydemotest2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * JokeActivity是笑话内容的展示界面,主要实现了一个动态加载的背景图片,api接口来自《第一行代码》中的加载每日bing的背景图片。
 */
public class JokeActivity extends BaseActivity {
    private TextView jokeContent;
    private Toolbar jokeTitle;
    private ImageView bingPicImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        jokeContent = (TextView)findViewById(R.id.joke_content);
        jokeTitle = (Toolbar) findViewById(R.id.joke_title);
        ActivityCollector.hidebar(JokeActivity.this);
        Intent intent = getIntent();
        Joke joke = (Joke)intent.getSerializableExtra("joke");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jokeContent.setText(Html.fromHtml(joke.getContent(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            jokeContent.setText(Html.fromHtml(joke.getContent()));
        }
        jokeTitle.setTitle(joke.getTitle());
        bingPicImg = (ImageView)findViewById(R.id.bing_pic);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPicString = prefs.getString("bing_pic",null);
        if(bingPicString!=null){
            Glide.with(this).load(bingPicString).into(bingPicImg);
        }else{
            loadBingPic();
        }
    }

    /**
     *loadBingPic方法，主要是实现了动态载入bing背景图的功能
     */
    private void  loadBingPic(){
    String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback()  {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
             final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(JokeActivity.this)
                        .edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(JokeActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
