package com.example.zf.usemysql.myroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zf.usemysql.login.BaseActivity;
import com.example.zf.usemysql.R;

public class MainZhuYe extends BaseActivity {
    ImageView ivBackGround;
    TextView name1;
    TextView sex1;
    TextView ages1;
    TextView location1;
    TextView break_out;
    TextView change_data;
    private SharedPreferences pref2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_zhu_ye);
        //getSupportActionBar().hide();
        ivBackGround = (ImageView) findViewById(R.id.iv_bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        name1 = (TextView)findViewById(R.id.room_name);
        sex1 = (TextView)findViewById(R.id.room_sex);
        ages1 = (TextView)findViewById(R.id.room_ages);
        location1 = (TextView)findViewById(R.id.room_location);
        break_out = (TextView)findViewById(R.id.break_out);
        change_data = (TextView)findViewById(R.id.change_data);


        pref2 = PreferenceManager.getDefaultSharedPreferences(this);
        String user = pref2.getString("user","");
        String sex = pref2.getString("sex","");
        String ages = pref2.getString("ages","");
        String location = pref2.getString("location","");
            //修改主页信息
        name1.setText(user);
        sex1.setText(sex);
        ages1.setText(ages);
        location1.setText(location);
        Bitmap bitmap = BlurBitmapUtil.blurBitmap(this, BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang), 3f);
        ivBackGround.setImageBitmap(bitmap);

        break_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example." +
                        "brosdcastbestpractice.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });

        change_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainZhuYe.this,changedata.class);
                startActivity(intent);
            }
        });
    }
}
