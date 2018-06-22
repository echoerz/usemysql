package com.example.zf.usemysql;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf.usemysql.tools.DBUtils;
import com.example.zf.usemysql.MainActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class myupdata extends AppCompatActivity {
    private SharedPreferences prefupdata;
    private static String user;
    private List<Updata> updataList = new ArrayList<>();
    private String textfen1[];
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String text = (String) message.obj;
            textfen1 = text.split("ddd\n");
            int length = textfen1.length;
            Updata updata[] = new Updata[length];
            for(int i=0;i<length;i++) {
                Updata data = new Updata("", "",0);
                String textfen2[] = textfen1[i].split("cccc\n");
                data.title = textfen2[0];
                data.context = textfen2[1];
                if(data.context.length()>30)
                    data.context = data.context.substring(0,30)+"......";
                data.zan = Integer.parseInt((textfen2[2]));
                updata[i] = data;
                updataList.add(updata[i]);
            }

            ContextAdapter adapter = new ContextAdapter(myupdata.this, R.layout.context_item, updataList);
            ListView listView = findViewById(R.id.list_view);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position,
                                        long id) {
                    Intent intent = new Intent(myupdata.this, Single_updata.class);
                    intent.putExtra("item",textfen1[position]);
                    startActivity(intent);

                }

            });
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
