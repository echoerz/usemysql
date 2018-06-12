package com.example.zf.usemysql;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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


    private AnimatorSet mRightOutSet; // 右出动画
    private AnimatorSet mLeftInSet; // 左入动画
    private boolean mIsShowBack;
    FrameLayout mFlContainer;
    FrameLayout mFlCardBack;
    FrameLayout mFlCardFront;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String text = (String) message.obj;
            String textfen[] = text.split("qa\n");
            // 0：图片
            // 1:点赞数
            // 2：主题
            // 3：用户
            // 4：内容
            // 5：id号
            String str = "查询失敗";
            if (message.what == 5) {
                //str = "查询成功";
                ((TextView) findViewById(R.id.fanmian_zan_pic)).setText("👍：");
                ((TextView) findViewById(R.id.fanmian_context)).setText(textfen[1]);
                ((TextView) findViewById(R.id.fanmian_user)).setText(textfen[0]);
                ((TextView) findViewById(R.id.fanmian_zan)).setText(textfen[2]);
                ((TextView) findViewById(R.id.fanmian_title)).setText(textfen[3]);
            }
            else if ( message.what == 6){
                ((TextView) findViewById(R.id.zhengmian_zan_pic)).setText("👍：");
                ((TextView) findViewById(R.id.zhengmian_context)).setText(textfen[1]);
                ((TextView) findViewById(R.id.zhengmian_user)).setText(textfen[0]);
                ((TextView) findViewById(R.id.zhengmian_zan)).setText(textfen[2]);
                ((TextView) findViewById(R.id.zhengmian_title)).setText(textfen[3]);
            }
            else{
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
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
        touxiang_name = (TextView) headerView.findViewById(R.id.touxiang_name);  //获取用户名
        use_touxiang = (ImageView) headerView.findViewById(R.id.use_touxiang);  //获取用户头像
        //touxiang_name =(TextView)findViewById(R.id.touxiang_name);

        pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        String user = pref1.getString("user", "");
        touxiang_name.setText(user);
        Toast.makeText(this, "欢迎回来，" + user, Toast.LENGTH_SHORT).show();


        mFlContainer = (FrameLayout) findViewById(R.id.main_fl_container);
        mFlCardBack = (FrameLayout) findViewById(R.id.main_fl_card_back);
        mFlCardFront = (FrameLayout) findViewById(R.id.main_fl_card_front);

        setAnimators(); // 设置动画
        setCameraDistance(); // 设置镜头距离
        // final EditText et_name = (EditText) findViewById(R.id.et_name);
        FloatingActionButton addcontext = (FloatingActionButton) findViewById(R.id.addsomething);
        addcontext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addcontext.class);
                startActivity(intent);
            }
        });


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_myziyuan:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_myshoucang:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_myroom:
                        Intent intent = new Intent(MainActivity.this, MainZhuYe.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.add_biao:
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
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
        if (imagePath != null) {
            Bitmap mypic = BitmapFactory.decodeFile(imagePath);
            return mypic;
        } else {
            Toast.makeText(this, "主人我无法偷到照片嘤嘤嘤", Toast.LENGTH_SHORT).show();
        }
        return null;
    }



    /*
    *  主屏动画
    *
    *
     */

    // 设置动画
    private void setAnimators() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_in);

        // 设置点击事件
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFlContainer.setClickable(false);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFlContainer.setClickable(true);
            }
        });
    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mFlCardFront.setCameraDistance(scale);
        mFlCardBack.setCameraDistance(scale);
    }

    // 翻转卡片
    public void flipCard(View view) {
        // 正面朝上
        if (!mIsShowBack) {
            mRightOutSet.setTarget(mFlCardFront);
            mLeftInSet.setTarget(mFlCardBack);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
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
                            ss = ss + mp.get(key) + "qa\n";
                        }
                        msg.what = 5;
                        msg.obj = ss;
                    }
                    handler.sendMessage(msg);
                }
            }).start();
        } else { // 背面朝上
            mRightOutSet.setTarget(mFlCardBack);
            mLeftInSet.setTarget(mFlCardFront);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
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
                            ss = ss + mp.get(key) + "qa\n";
                        }
                        msg.what = 6;
                        msg.obj = ss;
                    }
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }
}
