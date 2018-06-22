package com.example.zf.usemysql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zf.usemysql.tools.DBUtils;
import com.example.zf.usemysql.tools.love;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.zf.usemysql.MainActivity.convertIconToString;

public class mylove extends AppCompatActivity {
    private SharedPreferences prefupdata;
    private static String user;
    private List<Love_item> LoveList = new ArrayList<>();
    private String textfen[];
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String text = (String) message.obj;
            textfen = text.split("ddd\n");
            int length = textfen.length;
            Love_item loves[] = new Love_item[length];
            for(int i=0;i<length;i++) {
                Love_item data = new Love_item("", "",0,"",0);
                String textfen2[] = textfen[i].split("cccc\n");
                data.user = textfen2[0];
                data.title = textfen2[1];
                data.context = textfen2[2];
                if(data.context.length()>30)
                    data.context = data.context.substring(0,30)+"......";
                data.zan = Integer.parseInt((textfen2[3]));
                data.picid = Integer.parseInt((textfen2[4]));
                loves[i] = data;
                data = null;
                LoveList.add(loves[i]);
            }

            LoveAdapter adapter = new LoveAdapter(mylove.this,R.layout.mylove_item,LoveList);
            ListView listView = findViewById(R.id.love_list_view);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position,
                                        long id) {
                    Intent intent = new Intent(mylove.this, Single_love.class);
                    intent.putExtra("item", textfen[position]);
                    startActivity(intent);

                }

            });
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylove);
        prefupdata = PreferenceManager.getDefaultSharedPreferences(this);
        user = prefupdata.getString("user","");




        new Thread(new Runnable() {
            @Override
            public void run() {
                String myupdata = love.findlovezong(user);
                Message msg = new Message();
                msg.what = 1;
                msg.obj=myupdata;
                //非UI线程不要试着去操作界面
                handler.sendMessage(msg);
            }
        }).start();

    }
}
