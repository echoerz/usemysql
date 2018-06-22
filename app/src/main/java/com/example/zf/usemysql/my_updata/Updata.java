package com.example.zf.usemysql.my_updata;

import android.graphics.Bitmap;

/**
 * Created by 18810 on 2018/6/21.
 */

public class Updata {
    public String title;
    public String context;
    public int zan;
    public Bitmap bitmap;

    public Updata(String t,String c,int z,Bitmap b){
        this.title = t;
        this.context = c;
        this.zan = z;
        this.bitmap = b;
    }

    public String getTitle(){
        return title;
    }

    public String getContext() {
        return context;
    }

    public int getZan() {
        return zan;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String ShowInUpdata(){
        return getTitle()+"\n\n"+getContext();
    }
}
