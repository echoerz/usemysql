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

public class zhuce extends AppCompatActivity {
    public TextView cheakchong;
    private static final String TAG = "zhuce";
    Handler handler_login = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            // 0:用户
            // 1：密码
            // ((TextView)findViewById(R.id.tv_result)).setText(textfen[3]);
            String str = "该昵称已被注册";
            if(message.what == 1)
            {
                str = "注册成功";
                Toast.makeText(zhuce.this, str, Toast.LENGTH_SHORT).show();
            }
            cheakchong.setText(str);
            if(message.what == 1)
            {
                Intent intent = new Intent(zhuce.this,MainActivity.class);
                startActivity(intent);
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
        {
            actionBar.hide();
        }
        final EditText user_cheak = (EditText)findViewById(R.id.user_cheak);
        final EditText password_cheak = (EditText)findViewById(R.id.password_cheak);
        Button zhuce_but = (Button)findViewById(R.id.zhuce_but);
        cheakchong = (TextView)findViewById(R.id.cheakchong);

        zhuce_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String users_cheak = user_cheak.getText().toString().trim();
                final String passwords_cheak = password_cheak.getText().toString().trim();
                Log.e(TAG,users_cheak);
                Log.e(TAG,passwords_cheak);
                if(     users_cheak == null || users_cheak.equals("") ||
                        passwords_cheak == null || passwords_cheak.equals("")
                        ) {
                    Toast.makeText(zhuce.this,"请按要求填写所有信息",Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> mp =
                                    DBUtils.CheakUser(users_cheak);
                            Message msg = new Message();
                            if(mp == null) {
                                msg.what = 1;
                                HashMap<String, String> mp1 =
                                        DBUtils.adduser(users_cheak,passwords_cheak);
                                //非UI线程不要试着去操作界面
                            }
                            else {
                                msg.what=0;
                            }
                            handler_login.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
    }
}
