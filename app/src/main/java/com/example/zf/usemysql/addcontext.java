package com.example.zf.usemysql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class addcontext extends AppCompatActivity {
    private static final String TAG = "addcontext";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
           // ((TextView)findViewById(R.id.tv_result)).setText((String)message.obj);
            String str = "添加失败";
            if(message.what == 1) str = "添加成功";
            Toast.makeText(addcontext.this, str, Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontext);
        final EditText add_title = (EditText)findViewById(R.id.add_title);
        final EditText add_context = (EditText)findViewById(R.id.add_data);
        Button add_create = (Button)findViewById(R.id.add_create);
        add_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = add_title.getText().toString().trim();
                final String context = add_context.getText().toString().trim();
                Log.e(TAG,title);
                Log.e(TAG,context);
                if(title == null || title.equals("") ||context == null || context.equals("")) {
                    Toast.makeText(addcontext.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DBUtils.adddata(title,context);
                            Message msg = new Message();
                          //  msg.what = 0;
                         //   msg.obj =  "查询失败，请检查网络是否连接成功";
                            msg.what = 1;
                                //非UI线程不要试着去操作界面
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
    }
}
