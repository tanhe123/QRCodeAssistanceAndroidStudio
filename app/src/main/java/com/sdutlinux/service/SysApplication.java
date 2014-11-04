package com.sdutlinux.service;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * 为了能够一键退出程序，即销毁所有的 Activity，
 * 需要在每个Acitivity的oncreate方法里面通过SysApplication.getInstance().addActivity(this); 
 * 添加当前Acitivity到ancivitylist里面去，
 * 最后在想退出的时候调用SysApplication.getInstance().exit();
 * 可直接关闭所有的Acitivity并退出应用程序。
 */

public class SysApplication extends Application { 
    private List<Activity> mList = new LinkedList<Activity>(); 
    private static SysApplication instance; 
 
    private SysApplication() {   
    } 
    public synchronized static SysApplication getInstance() { 
        if (null == instance) { 
            instance = new SysApplication(); 
        } 
        return instance; 
    } 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
    
    /**
     * 关掉除 activity 的所有 activity
     * @param act
     */
    public void exit(Activity act) {
    	for (Activity activity : mList) { 
            if (activity != null && activity != act) 
                activity.finish(); 
        }
    }
    
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
}