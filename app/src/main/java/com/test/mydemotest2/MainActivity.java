package com.test.mydemotest2;



import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.test.mydemotest2.R.style.ToolbarTitle;


public class MainActivity extends BaseActivity{
    private List<Joke>jokeList = new ArrayList<>();
    private TextView userText;
    private TextView ageText;
    private SwipeRefreshLayout swipeRefresh;
    protected User user;
    private static final int UPDATE = 1;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollector.finishAll();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = getSharedPreferences("account",MODE_PRIVATE).edit();
        editor.putString("username",user.getAccount());
        editor.apply();
        super.onDestroy();
        Log.d("MainActivity", "onDestroy: ");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
        View header = nav.getHeaderView(0);
        userText = (TextView)header.findViewById(R.id.n_username);
        ageText = (TextView)header.findViewById(R.id.n_age);
        Toolbar toolbar  = (Toolbar)findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this,ToolbarTitle);
        sendRequestWithOkhttp();
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("userinfo");
        userText.setText(String.format(getResources().getString(R.string.show_username),user.getAccount()));
        ageText.setText(String.format(getResources().getString(R.string.show_age),user.getAge()));
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshJokes();
            }
        });
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_logout:
                    finish();
                    Intent intent =new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_quit:
                    ActivityCollector.finishAll();
                    break;
            }
            return true;
            }
        });
        }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE:
                    setRecyclerView();
                    break;
                default:
                    break;
            }
        }
    };
    private  void setRecyclerView(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        JokeAdapter mAdapter = new JokeAdapter(jokeList);
        recyclerView.setAdapter(mAdapter);
    }

    private void sendRequestWithOkhttp( ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://api.laifudao.com/open/xiaohua.json").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    jokeList= gson.fromJson(responseData,new TypeToken<List<Joke>>(){}.getType());
                    Message msg = new Message();
                    msg.what = UPDATE;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }
    private void refreshJokes(){
        new Thread(new Runnable() {
            private int test = new Random().nextInt(1000);
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://apis.haoservice" +
                            ".com/lifeservice/Joke/ContentList?pagesize=20&page="+test+"&key" +
                            "=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONArray jsonarray = new JSONObject(responseData).getJSONArray("result");
                    String jokeInfo = jsonarray.toString();
                    Gson gson = new Gson();
                    jokeList = gson.fromJson(jokeInfo, new TypeToken<List<Joke>>() {
                    }.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRecyclerView();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


}
