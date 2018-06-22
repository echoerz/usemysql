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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf.usemysql.login.BaseActivity;
import com.example.zf.usemysql.my_love.mylove;
import com.example.zf.usemysql.myroom.MainZhuYe;
import com.example.zf.usemysql.my_updata.myupdata;
import com.example.zf.usemysql.tools.DBUtils;
import com.example.zf.usemysql.tools.love;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private static String user;
    private static int userid;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String text = (String) message.obj;
            String textfen[] = text.split("aaaa\n");
            ((TextView) findViewById(R.id.main_username)).setText(textfen[0]);
            ((TextView) findViewById(R.id.main_title)).setText(textfen[1]);
            ((TextView) findViewById(R.id.main_context)).setText(textfen[2]);
            ((TextView) findViewById(R.id.main_zan)).setText("点赞数："+textfen[4]);
            userid= Integer.parseInt(textfen[3]);
            if(textfen[5].equals("2")) {
                Bitmap bitmap = convertStringToIcon(textfen[6]);
                ((ImageView) findViewById(R.id.main_picture)).setImageBitmap(bitmap);
            }
            else {
                Bitmap bitmap = null;
                ((ImageView) findViewById(R.id.main_picture)).setImageBitmap(bitmap);
            }
           //0-3数据   4图片

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
        user= pref1.getString("user","");
        touxiang_name.setText(user);
        Toast.makeText(this,"欢迎回来，"+user,Toast.LENGTH_SHORT).show();

        (findViewById(R.id.btn_01)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String data = DBUtils.ChackID();
                        //Message message = Message.obtain();
                        Message msg = new Message();
                        try {
                            String idnow = String.valueOf(DBUtils.nowid);
                            URL url = new URL("http://123.207.151.226:8080/pic/"+idnow+".jpg");
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            //这里就简单的设置了网络的读取和连接时间上线，如果时间到了还没成功，那就不再尝试
                            httpURLConnection.setReadTimeout(8000);
                            httpURLConnection.setConnectTimeout(8000);
                            InputStream inputStream = httpURLConnection.getInputStream();
                            //这里直接就用bitmap取出了这个流里面的图片，哈哈，其实整篇文章不就主要是这一句嘛
                            Bitmap bm = BitmapFactory.decodeStream(inputStream);
                            //下面这是把图片携带在Message里面，简单，不多说
                            data = data+convertIconToString(bm) + "aaaa\n";

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        msg.what = 1;
                        msg.obj=data;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
            // }
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
                        Intent intent1 = new Intent(MainActivity.this,myupdata.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_myshoucang:
                        Intent intent2 = new Intent(MainActivity.this,mylove.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_myroom:
                        Intent intent3 = new Intent(MainActivity.this,MainZhuYe.class);
                        startActivity(intent3);
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
                Intent intent = new Intent(MainActivity.this, addcontext.class);
                startActivity(intent);
                break;
            case R.id.show_pic:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        love.Addlove(userid,user);
                    }
                }).start();
                Toast.makeText(MainActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
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
