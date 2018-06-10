package com.example.zf.usemysql.myroom;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zf.usemysql.R;
import com.example.zf.usemysql.login.BaseActivity;

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
        String oldages = pref_change.getString("ages","");
        String oldsex = pref_change.getString("sex","");
        String oldlocation = pref_change.getString("location","");
        old_user=findViewById(R.id.changedata_user);
        old_ages=findViewById(R.id.changedata_ages);
        old_sex=findViewById(R.id.changedata_sex);
        old_location=findViewById(R.id.changedata_location);
        old_user.setHint(olduser);
        old_ages.setHint(oldages);
        old_sex.setHint(oldsex);
        old_location.setHint(oldlocation);

        changebutton=(TextView) findViewById(R.id.changebutton);
        changebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String newuser =
            }
        });
    }
}
