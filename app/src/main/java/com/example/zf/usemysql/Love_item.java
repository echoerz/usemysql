package com.example.zf.usemysql;

/**
 * Created by 18810 on 2018/6/22.
 */

public class Love_item {
    public String title;
    public String user;
    public String context;
    public int zan;
    public int picid;

    public Love_item(String t,String c,int z,String u,int p){
        this.title = t;
        this.user = u;
        this.context = c;
        this.zan = z;
        this.picid = p;
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
    public int getPicID() {
        return picid;
    }

    public String getUser(){ return user; }
    public String ShowInLove(){
        return getTitle()+"\n\n"+getUser()+"\n\n"+getContext();
    }
}
