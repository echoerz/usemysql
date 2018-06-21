package com.example.zf.usemysql;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 18810 on 2018/6/21.
 */

public class Single_updata extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_updata);
        String single_updata = getIntent().getStringExtra("item");
        String fen[] = single_updata.split("cccc\n");
        TextView context = findViewById(R.id.single_context);
        TextView title = findViewById(R.id.single_title);
        ImageView picture = findViewById(R.id.single_picture);

        title.setText(fen[0]);
        context.setText(fen[1]);

    }
}
