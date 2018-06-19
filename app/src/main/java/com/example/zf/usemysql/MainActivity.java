package com.example.zf.usemysql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.zf.usemysql.login.BaseActivity;
import com.example.zf.usemysql.myroom.MainZhuYe;
import com.example.zf.usemysql.tools.DBUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import static com.example.zf.usemysql.tools.DBUtils.LastID;

public class MainActivity extends BaseActivity {

    // 定义手势检测器实例
    GestureDetector detector;

    public TextView touxiang_name;
    private ImageView Picture;
    private ImageView use_touxiang;
    private SharedPreferences pref1;
    private DrawerLayout mDrawerLayout;
    private ImageView pic_show;
    private static final String TAG = "MainActivity";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String text = (String) message.obj;
            String textfen[] = text.split("aaaa\n");
            Bitmap bitmap=convertStringToIcon(textfen[4]);
           //0-3数据   4图片
            ((ImageView) findViewById(R.id.show_pic)).setImageBitmap(bitmap);
            ((TextView) findViewById(R.id.tv_result)).setText(textfen[0]+"\n"+textfen[1]+"\n"+textfen[2]+"\n"+textfen[3]+"\n");
            String str = "查询不存在";
            if (message.what == 1){
                str = "查询成功";
            }
            if (message.what != 1){Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();}
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        touxiang_name= (TextView) headerView.findViewById(R.id.touxiang_name);  //获取用户名
        use_touxiang = (ImageView)headerView.findViewById(R.id.use_touxiang);  //获取用户头像


        pic_show = (ImageView)findViewById(R.id.show_pic);
        pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        String user = pref1.getString("user","");
        touxiang_name.setText(user);
        Toast.makeText(this,"欢迎回来，"+user,Toast.LENGTH_SHORT).show();

        (findViewById(R.id.btn_01)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv_result = findViewById(R.id.tv_result);
                        HashMap<String, String> mp =
                                DBUtils.ChackID();
                        Message message = Message.obtain();
                       // Message msg = new Message();
                        if (mp == null) {
                            message.what = 0;
                           // msg.obj = "查询失败，请检查网络是否连接成功";
                            //非UI线程不要试着去操作界面
                        } else {
                            String ss = new String();
                            for (String key : mp.keySet()) {
                                ss = ss +mp.get(key) + "aaaa\n";
                            }
                            try {
                                URL url = new URL("http://123.207.151.226:8080/pic/45.jpg");
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                //这里就简单的设置了网络的读取和连接时间上线，如果时间到了还没成功，那就不再尝试
                                httpURLConnection.setReadTimeout(8000);
                                httpURLConnection.setConnectTimeout(8000);
                                InputStream inputStream = httpURLConnection.getInputStream();
                                //这里直接就用bitmap取出了这个流里面的图片，哈哈，其实整篇文章不就主要是这一句嘛
                                Bitmap bm = BitmapFactory.decodeStream(inputStream);
                                //下面这是把图片携带在Message里面，简单，不多说
                                ss = ss+convertIconToString(bm) + "aaaa\n";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            message.what = 1;
                            message.obj = ss;
                            handler.sendMessage(message);
                           }

                    }
                }).start();
            }
            // }
        });
        Button addcontext = (Button) findViewById(R.id.add_context);
        addcontext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addcontext.class);
                startActivity(intent);
            }
        });


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_myziyuan:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_myshoucang:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_myroom:
                        Intent intent = new Intent(MainActivity.this,MainZhuYe.class);
                        startActivity(intent);
                        break;
                    default:break;
                }
                return true;
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.add_biao:
                Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
        return super.onKeyDown(keyCode, event);
    }

    private Bitmap displayImage(String imagePath) {
        if (imagePath!=null){
            Bitmap mypic = BitmapFactory.decodeFile(imagePath);
            return mypic;
        }else {
            Toast.makeText(this,"主人我无法偷到照片嘤嘤嘤",Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
