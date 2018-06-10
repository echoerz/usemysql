package com.example.zf.usemysql.login;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zf on 2018/6/10.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity)
    {
        activities.add(activity);
    }
    public static void removeActivity(Activity activity)
    {
        activities.remove(activity);
    }
    public static void finishAll()
    {
        for(Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
