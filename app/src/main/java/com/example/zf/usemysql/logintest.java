package com.example.zf.usemysql;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class logintest extends AppCompatActivity implements View.OnClickListener{

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;


    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {




        final EditText user = (EditText)findViewById(R.id.user_t);
        final EditText password = (EditText)findViewById(R.id.password_t);


        final String users = user.getText().toString().trim();
        final String passwords = password.getText().toString().trim();
        Log.e(TAG,users);
        Log.e(TAG,passwords);




        if(users == null || users.equals("") || passwords == null || passwords.equals("")) {
            Toast.makeText(logintest.this,"输入不能为空",Toast.LENGTH_SHORT).show();
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HashMap<String, String> mp =
                            DBUtils.cheakuser(users);
                    Message msg = new Message();
                    if(mp == null) {
                        msg.what = 0;
                        //非UI线程不要试着去操作界面
                    }
                    else {
                        String ss = new String();
                        for (String key : mp.keySet()) {
                            ss =ss+mp.get(key) + "\n";
                        }
                        String text=(String)ss;
                        String textfen[]=text.split("\n");
                        if (textfen[1].equals(passwords)) {
                            msg.what = 1;
                            // 隐藏输入框
                        }
                        else {msg.what=2;}
                    }
                    handler_logintest.sendMessage(msg);
                }
            }).start();
        }
    }


    /**
     * 输入框的动画效果
     *
     * @param view
     *      控件
     * @param w
     *      宽
     * @param h
     *      高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                mInputLayout.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }
    private static final String TAG = "logintest";
    Handler handler_logintest = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            // 0:用户
            // 1：密码
            // ((TextView)findViewById(R.id.tv_result)).setText(textfen[3]);
            String str = "用户不存在，请立即注册";
            if(message.what == 1)
            {
                str = "登录成功";
            }
            if(message.what == 2)
            {
                str = "登录密码错误，请重试";
            }
            Toast.makeText(logintest.this, str, Toast.LENGTH_SHORT).show();

            if(message.what == 1)
            {
                // 计算出控件的高与宽
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();

                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                inputAnimator(mInputLayout, mWidth, mHeight);

                //Intent intent = new Intent(logintest.this,MainActivity.class);
                new Handler(new Handler.Callback() {
                    //处理接收到的消息的方法
                    @Override
                    public boolean handleMessage(Message arg0) {
                        //实现页面跳转
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 900); //表示延时三秒进行任务的执行
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_logintest);
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initView();



        TextView zhuce = (TextView)findViewById(R.id.zhuce_t);
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_zhuce = new Intent(logintest.this,zhuce.class);
                startActivity(intent_zhuce);
            }
        });
        ImageView return1 = (ImageView)findViewById(R.id.return_login);
        return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
