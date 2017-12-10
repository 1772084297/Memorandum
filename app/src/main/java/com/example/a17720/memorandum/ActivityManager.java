package com.example.a17720.memorandum;

import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公共类，用来实现整个程序共同使用的一些函数
 * Created by 17720 on 2017/12/8.
 */

public class ActivityManager {
    //用静态变量存储内容
    private static ActivityManager instance;
    private List<Activity> list;

    //默认的铃声地址     不存在的
//    private static Uri uri = Uri.parse("/sdcard/demo.mp3");

    public static ActivityManager getInstance(){
        if (instance == null)
            instance  = new ActivityManager();
        return instance;
    }
    //添加Activity进列表
    public void AddActivity(Activity activity){
        if (list ==null)
            list = new ArrayList<Activity>();
        if (activity!=null)
            list.add(activity);
    }

    //获得铃声的路径  不存在的
//    public static Uri getUri(){
//        return uri;
//    }

    //设置铃声    不存在的
//    public static Uri setUri(Uri uri){
//        ActivityManager.uri =uril
//    }

    //退出所有的程序
    public void exitAllProgress(){
        for (int i=0;i<list.size();i++){
            Activity activity = list.get(i);
            activity.finish();
        }
    }

    //更新文件
    public void saveNote(String title,String content,String time){
        Data data = new Data();
        data.setTitle(title);
    };

    //添加文件
    public void addNote(){};

//返回当前时间
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String returnTime(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(d);
        return time;
    }


}
