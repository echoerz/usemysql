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

import java.util.HashMap;
import static com.example.zf.usemysql.tools.DBUtils.LastID;

public class MainActivity extends BaseActivity /*implements android.view.GestureDetector.OnGestureListener*/{

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
            String textfen[] = text.split("\n");
            // 0：图片
            // 1:点赞数
            // 2：主题
            // 3：用户
            // 4：内容
            // 5：id号
            ((TextView) findViewById(R.id.tv_result)).setText(text);
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

      //  detector = new GestureDetector(this, this);//构建手势滑动对象

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        touxiang_name= (TextView) headerView.findViewById(R.id.touxiang_name);  //获取用户名
        use_touxiang = (ImageView)headerView.findViewById(R.id.use_touxiang);  //获取用户头像
        //touxiang_name =(TextView)findViewById(R.id.touxiang_name);

        pic_show = (ImageView)findViewById(R.id.show_pic);
        pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        String user = pref1.getString("user","");
        touxiang_name.setText(user);
        Toast.makeText(this,"欢迎回来，"+user,Toast.LENGTH_SHORT).show();

       // final EditText et_name = (EditText) findViewById(R.id.et_name);
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

                        TextView tv_result = findViewById(R.id.tv_result);
                        HashMap<String, String> mp =
                                DBUtils.ChackID();
                        Message msg = new Message();
                        if (mp == null) {
                            msg.what = 0;
                            msg.obj = "查询失败，请检查网络是否连接成功";
                            //非UI线程不要试着去操作界面
                        } else {
                            String ss = new String();
                            for (String key : mp.keySet()) {
                                ss = ss +mp.get(key) + "\n";
                            }
                            msg.what = 1;
                            msg.obj = ss;
                        }
                        handler.sendMessage(msg);
                        if(msg.what == 1) {
                            DBUtils.getBlob(LastID);
                            Picture = (ImageView)findViewById(R.id.iv_result);
                            Picture.setImageBitmap(displayImage("picture"+LastID+".jpg"));
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


/*
    // 将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me) {
        return detector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    //花瓶检测
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float minMove = 120; // 最小滑动距离
        float minVelocity = 0; // 最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) { // 左滑
            Toast.makeText(this, velocityX + "左滑", Toast.LENGTH_SHORT).show();
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) { // 右滑
            Toast.makeText(this, velocityX + "右滑", Toast.LENGTH_SHORT).show();
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) { // 上滑
            Toast.makeText(this, velocityX + "上滑", Toast.LENGTH_SHORT).show();
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) { // 下滑
            Toast.makeText(this, velocityX + "下滑", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
                            float velocityY) {

        return false;
    }
*/

}
