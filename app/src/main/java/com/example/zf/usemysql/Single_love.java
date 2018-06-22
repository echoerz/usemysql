package com.example.zf.usemysql;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zf.usemysql.tools.DBUtils;
import com.example.zf.usemysql.tools.love;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.zf.usemysql.MainActivity.convertIconToString;

/**
 * Created by 18810 on 2018/6/22.
 */

public class Single_love extends AppCompatActivity {
    private int check;
    private String id;
    private ImageView picture;
    private Bitmap bm = null;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            picture.setImageBitmap(bm);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_love);
        String single_updata = getIntent().getStringExtra("item");
        String fen[] = single_updata.split("cccc\n");
        TextView context = findViewById(R.id.love_single_context);
        TextView title = findViewById(R.id.love_single_title);
        TextView zan = findViewById(R.id.love_single_zan);
        TextView user = findViewById(R.id.love_single_username);
        picture = findViewById(R.id.love_single_picture);
        user.setText(fen[0]);
        title.setText(fen[1]);
        context.setText(fen[2]);
        zan.setText("点赞数："+fen[3]);
        id = fen[4];
        check = Integer.parseInt(fen[5]);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(check == 2){
                    try {
                        URL url = new URL("http://123.207.151.226:8080/pic/"+id+".jpg");
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
                    Message msg = new Message();
                    msg.what = 1;
                    //非UI线程不要试着去操作界面
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }
}
