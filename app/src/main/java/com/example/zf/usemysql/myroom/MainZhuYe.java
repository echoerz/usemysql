package com.example.zf.usemysql.myroom;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zf.usemysql.R;

public class MainZhuYe extends AppCompatActivity {
    ImageView ivBackGround;
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
        //TextView name1 = (TextView)findViewById(R.id.room_name);
        pref2 = PreferenceManager.getDefaultSharedPreferences(this);
        String user = pref2.getString("user","");
        String sex = pref2.getString("sex","");
        String ages = pref2.getString("ages","");
        String location = pref2.getString("location","");
        if (location.equals("")){
            //修改主页信息

        }
        Bitmap bitmap = BlurBitmapUtil.blurBitmap(this, BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang), 3f);
        ivBackGround.setImageBitmap(bitmap);
    }
}
