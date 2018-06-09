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

    private EditText UserCheak;

    private EditText PasswordCheak;

    private EditText PasswordCheakTwice;

    private TextView ZC_Button;

    private static final String TAG = "zhuce";
    Handler handler_login = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            // 0:用户
            // 1：密码
            // ((TextView)findViewById(R.id.tv_result)).setText(textfen[3]);
            String str = "该昵称已被注册";
            cheakchong.setText(str);
            if(message.what == 1)
            {
                //str = "注册成功";
                //Toast.makeText(zhuce.this, str, Toast.LENGTH_SHORT).show();
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
        UserCheak = (EditText)findViewById(R.id.zc_user_t);
        PasswordCheak = (EditText)findViewById(R.id.zc_password_t);
        PasswordCheakTwice = (EditText)findViewById(R.id.zc_password_t_2);
        ZC_Button = (TextView) findViewById(R.id.zhuce_btn);
        cheakchong = (TextView)findViewById(R.id.cheakchong);

        ZC_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_cheak = UserCheak.getText().toString().trim();
                final String password_cheak = PasswordCheak.getText().toString().trim();
                final String password_cheak_twice = PasswordCheakTwice.getText().toString().trim();
                Log.e(TAG,user_cheak);
                Log.e(TAG,password_cheak);
                if(     user_cheak == null || user_cheak.equals("") ||
                        password_cheak == null || password_cheak.equals("")
                        ) {
                    Toast.makeText(zhuce.this,"请按要求填写所有信息",Toast.LENGTH_SHORT).show();
                }
                else if(password_cheak_twice == null || password_cheak_twice.equals("")){
                    Toast.makeText(zhuce.this,"请再次输入密码",Toast.LENGTH_SHORT).show();
                }
                else if(!(password_cheak_twice.equals(password_cheak))){
                    Toast.makeText(zhuce.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> mp =
                                    DBUtils.CheakUser(user_cheak);
                            Message msg = new Message();
                            if(mp == null) {
                                msg.what = 1;
                                HashMap<String, String> mp1 =
                                        DBUtils.adduser(user_cheak,password_cheak);
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
