package com.test.mydemotest2;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * MainActivity主要是对于从API接受的笑话标题进行展示，并且提供注销账号与退出软件的功能。
 */

public class MainActivity extends BaseActivity{
    private List<Joke>jokeList = new ArrayList<>();
    private TextView userText;
    private TextView ageText;
    private RecyclerView recyclerView;
    private JokeAdapter mAdapter;
    private LinearLayoutManager layoutManager;
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
        SharedPreferences.Editor editor = getSharedPreferences("account",MODE_PRIVATE).edit();//退出时保存相关账户信息
        editor.putString("username",user.getAccount());
        editor.apply();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRecyclerView();
        ActivityCollector.hidebar(MainActivity.this);
        NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
        View header = nav.getHeaderView(0);
        userText = (TextView)header.findViewById(R.id.n_username);
        ageText = (TextView)header.findViewById(R.id.n_age);
        Toolbar toolbar  = (Toolbar)findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this,R.style.ToolbarTitle);
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
                        finish();
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
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new JokeAdapter(jokeList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }//设置RecyclerView

    private void sendRequestWithOkhttp( ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {HttpUtil.sendOkHttpRequest("http://api.laifudao.com/open/xiaohua.json", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        jokeList= gson.fromJson(responseData,new TypeToken<List<Joke>>(){}.getType());
                        Message msg = new Message();
                        msg.what = UPDATE;
                        handler.sendMessage(msg);
                    }
                });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }//请求笑话的方法
    private void refreshJokes(){
        new Thread(new Runnable() {
            private int random = new Random().nextInt(1000);
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://apis.haoservice" +
                        ".com/lifeservice/Joke/ContentList?pagesize=20&page="+random+"&key" +
                        "=57a9bdfd931f4c5d9da8cb0060c9969a", new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws
                            IOException{
                        JSONArray jsonarray;
                        try {
                            jsonarray = new JSONObject(response.body().string().replace("\r\n", "\n"))
                                    .getJSONArray("result");
                            String jokeInfo = jsonarray.toString();
                            Gson gson = new Gson();
                            jokeList = gson.fromJson(jokeInfo, new TypeToken<List<Joke>>() {
                            }.getType());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRecyclerView();
                        mAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }//刷新笑话目录的方法


}

