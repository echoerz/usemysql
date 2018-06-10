package com.example.zf.usemysql;

import com.example.zf.usemysql.login.BaseActivity;

/**
 * Created by 18810 on 2018/6/8.
 */

public class DianZan_L extends BaseActivity {

    /*private static final String TAG = "点赞";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleButton(Button button) {
            // ((TextView)findViewById(R.id.tv_result)).setText((String)message.obj);
            String str = null;
            if(button.setOnClickListener(); == 1) {
                str = "点赞成功";
            }else{
                str = "点赞撤销";
            }


            Toast.makeText(DianZan_L.this, str, Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button dianzan = (Button)findViewById(R.id.thumb_button);
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(DianZan_L.this,"输入不能为空",Toast.LENGTH_SHORT).show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DBUtils.AddZan();
                            Message msg = new Message();
                            //  msg.what = 0;
                            //   msg.obj =  "查询失败，请检查网络是否连接成功";
                            msg.what = 1;
                            //非UI线程不要试着去操作界面
                            handler.sendMessage(msg);
                        }
                    }).start();

            }
        });
    }*/

}
