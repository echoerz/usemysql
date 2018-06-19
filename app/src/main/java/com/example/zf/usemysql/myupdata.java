package com.example.zf.usemysql;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf.usemysql.tools.DBUtils;

public class myupdata extends AppCompatActivity {
    private SharedPreferences prefupdata;
    private static String user;
    private static String data;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            data = (String)message.obj;
            TextView show_myupdata = (TextView)findViewById(R.id.show_myupdata);
            show_myupdata.setText(data);
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myupdata);
        prefupdata = PreferenceManager.getDefaultSharedPreferences(this);
        user = prefupdata.getString("user","");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String myupdata = DBUtils.findupdata(user);
                Message msg = new Message();
                msg.what = 1;
                msg.obj=myupdata;
                //非UI线程不要试着去操作界面
                handler.sendMessage(msg);
            }
        }).start();

    }
}
