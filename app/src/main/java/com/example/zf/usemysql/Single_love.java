package com.example.zf.usemysql;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 18810 on 2018/6/22.
 */

public class Single_love extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_love);
        String single_updata = getIntent().getStringExtra("item");
        String fen[] = single_updata.split("cccc\n");
        TextView context = findViewById(R.id.love_single_context);
        TextView title = findViewById(R.id.love_single_title);
        TextView zan = findViewById(R.id.love_single_zan);
        TextView user = findViewById(R.id.love_single_username);
        ImageView picture = findViewById(R.id.single_picture);

        user.setText(fen[0]);
        title.setText(fen[1]);
        context.setText(fen[2]);
        zan.setText("点赞数："+fen[3]);


    }
}
