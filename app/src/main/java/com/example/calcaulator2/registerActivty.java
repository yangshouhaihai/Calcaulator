package com.example.calcaulator2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class registerActivty extends AppCompatActivity {
private EditText et_user_name,et_psw,et_psw_again;
private RadioGroup rg_gender;
private Button btn_register;

private String userName,psw,pswAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    public void init(){
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_pswAgain);
        rg_gender = findViewById(R.id.GenerRadio);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getEditString();
               int sex;
               int sexChoseId = rg_gender.getCheckedRadioButtonId();
               switch (sexChoseId){
                   case R.id.girl:
                       sex = 0;
                       break;
                   case R.id.boy:
                       sex = 1;
                       break;
                   default:
                       sex = -1;
                       break;
               }
               if(TextUtils.isEmpty(userName)){
                   Toast.makeText(registerActivty.this, "请输入账号", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(psw)) {
                   Toast.makeText(registerActivty.this, "请输入密码", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(pswAgain)) {
                   Toast.makeText(registerActivty.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
               } else if (sex<0) {
                   Toast.makeText(registerActivty.this, "请选择性别", Toast.LENGTH_SHORT).show();
               } else if (!psw.equals(pswAgain)) {
                   Toast.makeText(registerActivty.this, "输入的密码不一致", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(registerActivty.this, "注册成功", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(registerActivty.this,loginActivity.class);
                   intent.putExtra("userName",userName);
                   intent.putExtra("password",psw);
                   startActivity(intent);
                   SharedPreferences sharedPreferences = getSharedPreferences("SEND", Context.MODE_PRIVATE);
                   SharedPreferences.Editor edit = sharedPreferences.edit();
                   edit.putString(userName,psw);
                   edit.apply();
                   registerActivty.this.finish();
               }
            }

            private void getEditString() {
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                pswAgain = et_psw_again.getText().toString().trim();
            }
        });
    }
}