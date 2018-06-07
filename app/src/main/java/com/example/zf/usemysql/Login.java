package com.example.zf.usemysql;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    Handler handler_login = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            // 0:用户
            // 1：密码
           // ((TextView)findViewById(R.id.tv_result)).setText(textfen[3]);
            String str = "用户不存在，请立即注册";
            if(message.what == 1)
            {
                str = "登录成功";
            }
            if(message.what == 2)
            {
                str = "登录密码错误，请重试";
            }
            Toast.makeText(Login.this, str, Toast.LENGTH_SHORT).show();

            if(message.what == 1)
            {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
        {
            actionBar.hide();
        }

        final EditText user = (EditText)findViewById(R.id.user);
        final EditText password = (EditText)findViewById(R.id.password);
        Button login = (Button)findViewById(R.id.login);
        TextView zhuce = (TextView)findViewById(R.id.zhuce);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String users = user.getText().toString().trim();
                final String passwords = password.getText().toString().trim();
                Log.e(TAG,users);
                Log.e(TAG,passwords);
                if(users == null || users.equals("") || passwords == null || passwords.equals("")) {
                    Toast.makeText(Login.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> mp =
                                    DBUtils.cheakuser(users);
                            Message msg = new Message();
                            if(mp == null) {
                                msg.what = 0;
                                //非UI线程不要试着去操作界面
                            }
                            else {
                                String ss = new String();
                                for (String key : mp.keySet()) {
                                    ss =ss+mp.get(key) + "\n";
                                }
                                String text=(String)ss;
                                String textfen[]=text.split("\n");
                                if (textfen[1].equals(passwords)) {
                                    msg.what = 1;
                                }
                                else {msg.what=2;}
                            }
                            handler_login.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_zhuce = new Intent(Login.this,MainActivity.class);
                startActivity(intent_zhuce);
            }
        });

    }

    public void cheaklogin(){


    }

}
