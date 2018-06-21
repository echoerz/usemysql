package com.example.zf.usemysql.myroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf.usemysql.MainActivity;
import com.example.zf.usemysql.R;
import com.example.zf.usemysql.login.BaseActivity;
import com.example.zf.usemysql.tools.DBUtils;
import com.example.zf.usemysql.tools.love;

public class changedata extends BaseActivity {

    private TextView old_user;
    private TextView old_sex;
    private TextView old_ages;
    private TextView old_location;
    private TextView changebutton;
    private SharedPreferences pref_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changedata);
        pref_change = PreferenceManager.getDefaultSharedPreferences(this);
        String olduser = pref_change.getString("user","");
        final String oldages = pref_change.getString("ages","");
        String oldsex = pref_change.getString("sex","");
        String oldlocation = pref_change.getString("location","");
        old_user=findViewById(R.id.changedata_user);
        old_ages=findViewById(R.id.changedata_ages);
        old_sex=findViewById(R.id.changedata_sex);
        old_location=findViewById(R.id.changedata_location);
        old_user.setText(olduser);
        old_ages.setText(oldages);
        old_sex.setText(oldsex);
        old_location.setText(oldlocation);

        changebutton=(TextView) findViewById(R.id.changebutton);
        changebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username =old_user.getText().toString().trim();
                final String newsex =old_sex.getText().toString().trim();
                final String newages =old_ages.getText().toString().trim();
                final String newlocation =old_location.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBUtils.changemessage(username,newsex,newages,newlocation);
                    }
                }).start();
                Toast.makeText(changedata.this,"资料已修改,请重新登录后生效",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(changedata.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
