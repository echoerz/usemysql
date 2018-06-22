package com.example.zf.usemysql.my_updata;

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

import com.example.zf.usemysql.R;
import com.example.zf.usemysql.tools.DBUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class myupdata extends AppCompatActivity {
    private SharedPreferences prefupdata;
    private static String user;
    private List<Updata> updataList = new ArrayList<>();
    private String textfen1[];
    private Bitmap bm;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

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
                String text = DBUtils.findupdata(user);
                Message msg = new Message();
                msg.what = 1;
                //String text = (String) message.obj;
                textfen1 = text.split("ddd\n");
                int length = textfen1.length;
                Updata updata[] = new Updata[length];
                for(int i=0;i<length;i++) {
                    Updata data = new Updata("", "",0,null);
                    String textfen2[] = textfen1[i].split("cccc\n");
                    data.title = textfen2[0];
                    data.context = textfen2[1];
                    if(data.context.length()>30)
                        data.context = data.context.substring(0,30)+"......";
                    data.zan = Integer.parseInt((textfen2[2]));
                    int check = Integer.parseInt((textfen2[4]));
                    if(check == 2) {
                        try {
                            URL url = new URL("http://123.207.151.226:8080/pic/" + textfen2[3] + ".jpg");
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            //这里就简单的设置了网络的读取和连接时间上线，如果时间到了还没成功，那就不再尝试
                            httpURLConnection.setReadTimeout(8000);
                            httpURLConnection.setConnectTimeout(8000);
                            InputStream inputStream = httpURLConnection.getInputStream();
                            //这里直接就用bitmap取出了这个流里面的图片，哈哈，其实整篇文章不就主要是这一句嘛
                            bm = BitmapFactory.decodeStream(inputStream);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data.bitmap = bm;
                    }
                    updata[i] = data;
                    updataList.add(updata[i]);
                }

                //非UI线程不要试着去操作界面
                handler.sendMessage(msg);
            }
        }).start();

    }
}
