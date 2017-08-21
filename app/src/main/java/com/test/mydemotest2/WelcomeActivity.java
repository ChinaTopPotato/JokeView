package com.test.mydemotest2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import org.litepal.crud.DataSupport;

import java.util.List;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<User> userList = DataSupport.findAll(User.class);
                    SharedPreferences sp = getSharedPreferences("account", MODE_PRIVATE);
                    String username = sp.getString("username", "");
                    boolean judge = false;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for(User u :userList){
                        if (u.getAccount().equals(username)){
                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            intent.putExtra("userinfo", u);
                            startActivity(intent);
                            judge=true;

                        }
                    }
                    if (judge == false){
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }).start();
        }
    }
