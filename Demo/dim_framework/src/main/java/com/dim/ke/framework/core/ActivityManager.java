package com.dim.ke.framework.core;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Stack;

public class ActivityManager {

    private Stack<WeakReference<Activity>> activityStack = new Stack<>();
    private static ActivityManager mInstance=null;

    public static synchronized ActivityManager newInstance(){
        if(mInstance == null){
            mInstance = new ActivityManager();
        }
        return mInstance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(new WeakReference<>(activity));
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(new WeakReference<>(activity));
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        WeakReference<Activity> activity = activityStack.lastElement();
        if(activity != null){
            return activity.get();
        }
        return null;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null || activityStack.size() == 0) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && activityStack.get(i).get() != null) {
                activityStack.get(i).get().finish();
            }
        }
        activityStack.clear();
        activityStack = null;
    }




    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();

            System.exit(0);
        } catch (Exception e) {
        }
    }

    public Stack<WeakReference<Activity>> getActivityStack() {
        return activityStack;
    }
}
