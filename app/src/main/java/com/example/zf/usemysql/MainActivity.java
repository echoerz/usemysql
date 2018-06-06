package com.example.zf.usemysql;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            ((TextView)findViewById(R.id.tv_result)).setText((String)message.obj);
            String str = "查询不存在";
            if(message.what == 1) str = "查询成功";
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et_name = (EditText) findViewById(R.id.et_name);
        (findViewById(R.id.btn_01)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  final String name = et_name.getText().toString().trim();
              //  Log.e(TAG, name);
               // if(name == null || name.equals("")) {
              //      Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
             //   }
              //  else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Random random = new Random();
                            TextView tv_result = findViewById(R.id.tv_result);
                            HashMap<String, String> mp =
                                    DBUtils.getUserInfoByName(random.nextInt(10)+1);
                            Message msg = new Message();
                            if(mp == null) {
                                msg.what = 0;
                                msg.obj =  "查询失败，请检查网络是否连接成功";
                                //非UI线程不要试着去操作界面
                            }
                            else {
                                String ss = new String();
                                for (String key : mp.keySet()) {
                                    if (key.equals("context")) {
                                        ss =ss+key + ":" + mp.get(key) + "\n";
                                    }
                                }
                                msg.what = 1;
                                msg.obj = ss;
                            }
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
           // }
        });
        Button addcontext = (Button)findViewById(R.id.add_context);
        addcontext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,addcontext.class);
                startActivity(intent);
            }
        });
    }
}
