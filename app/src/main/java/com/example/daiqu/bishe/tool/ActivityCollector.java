package com.example.daiqu.bishe.tool;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    //活动管理器，方便管理活动，暂时用于解决程序退出问题
    public static List<Activity> activityList = new ArrayList<>();
    //任意添加活动
    public static void addActivity(Activity activity){
        activityList.add(activity);
    }
    //选择移除特定的活动
    public static void removeActivity(Activity activity){
        activityList.remove(activity);
        activity.finish();
    }
    //一键关闭所有的活动
    public static void finishAll(){
        for(Activity activity:activityList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
