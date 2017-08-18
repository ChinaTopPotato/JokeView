package com.test.mydemotest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {
    private TextView account_text;
    private TextView password_text;
    private TextView age_text;
    private Button register_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        account_text = (EditText)findViewById(R.id.r_account);
        password_text = (EditText)findViewById(R.id.r_password);
        age_text = (EditText)findViewById(R.id.r_age);
        register_button = (Button)findViewById(R.id.register);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setAccount(account_text.getText().toString());
                user.setPassword(password_text.getText().toString());
                user.setAge(Integer.parseInt(age_text.getText().toString()));
                user.save();
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
