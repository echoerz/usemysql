package com.example.zf.usemysql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity /*implements android.view.GestureDetector.OnGestureListener*/{

    // 定义手势检测器实例
    GestureDetector detector;

    private TextView touxiang_name;
    private SharedPreferences pref1;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "MainActivity";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String text = (String) message.obj;
            String textfen[] = text.split("\n");
            // 0:点赞数
            // 1：主题
            // 2：用户
            // 3：内容
            // 4：id号
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

        //touxiang_name = (TextView)findViewById(R.id.touxiang_name);
        pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        String user = pref1.getString("user","");
        //touxiang_name.setText(user);
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
                mDrawerLayout.closeDrawers();
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
