package com.test.mydemotest2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 登录界面LoginActivity，主要内容是实现登录的相关功能，并且能够跳转到注册界面
 */
public class LoginActivity extends BaseActivity {
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSharedPreferences("account",MODE_PRIVATE).edit().clear().apply();//进入login界面时自动删除保存的账户信息
        accountEdit = (EditText)findViewById(R.id.account);
        passwordEdit = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.to_register);
        login.setOnClickListener(new View.OnClickListener() {
            boolean judge=false;
            @Override
            public void onClick(View v) {
                List<User> users = DataSupport.findAll(User.class);
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
               for(User user : users){
                   if(account.equals(user.getAccount())&&password.equals(user.getPassword())){
                       Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                       intent.putExtra("userinfo",user);
                       startActivity(intent);
                        judge = true;
                   }
               }
              if(!judge) Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
