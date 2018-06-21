package com.example.zf.usemysql;

/**
 * Created by 18810 on 2018/6/21.
 */

public class Updata {
    public String title;
    public String context;
    public int zan;

    public Updata(String t,String c,int z){
        this.title = t;
        this.context = c;
        this.zan = z;
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

    public String ShowInUpdata(){
        return getTitle()+"\n\n"+getContext();
    }
}
