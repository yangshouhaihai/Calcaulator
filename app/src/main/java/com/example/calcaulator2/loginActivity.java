package com.example.calcaulator2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class loginActivity extends AppCompatActivity {
private EditText et_user_name,et_psw;
private TextView tv_register;
private Button btn_login;
private String userName,psw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    private void init(){
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        Intent intent = getIntent();
        String username = intent.getStringExtra("userName");
        String password = intent.getStringExtra("password");
        if(!TextUtils.isEmpty(username)&& !TextUtils.isEmpty(password)){
            et_user_name.setText(username);
            et_psw.setText(password);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();

                SharedPreferences sharedPreferences = getSharedPreferences("SEND",MODE_PRIVATE);
                Map<String,?> map = sharedPreferences.getAll();
                if(usernameISValid(map,userName) && passwordISValid(map,psw)){
                    Toast.makeText(loginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    loginActivity.this.finish();
                    Intent intent = new Intent(loginActivity.this,calcaulateActivty.class);
                    intent.putExtra("username",userName);
                    startActivity(intent);
                }
            }

            private boolean passwordISValid(Map<String,?> map, String psw) {
                if(TextUtils.isEmpty(psw)){
                    Toast.makeText(loginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else {
                    for (Map.Entry<String,?> m:map.entrySet()){
                        if(m.getValue().equals(psw)){
                            return true;
                        }
                    }
                    Toast.makeText(loginActivity.this, "您输入的密码有误", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            private boolean usernameISValid(Map<String,?> map, String userName) {
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(loginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                }else{
                    for(Map.Entry<String,?> m: map.entrySet()){
                        if(m.getKey().equals(userName)){
                            return true;
                        }
                    }
                    Toast.makeText(loginActivity.this, "您还未注册", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(loginActivity.this,registerActivty.class) ;
               startActivity(intent);
            }
        });
    }
}