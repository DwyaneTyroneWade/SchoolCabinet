package com.xiye.schoolcabinet.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.os.Process;

import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.sclibrary.base.L;


/**
 * 
 * @author shuang.wu
 *
 */
public class ActivityStack {
	private Stack<WeakReference<BaseActivity>> mActivities = new Stack<WeakReference<BaseActivity>>();
//    private Stack<BaseActivity>  mActivities = new Stack<BaseActivity>();
    private final String         TAG         = ActivityStack.class.getSimpleName();
    private static ActivityStack INSTANCE;

    private ActivityStack() {

    }

    public static ActivityStack getInstance() {
        if (INSTANCE == null) {
            synchronized (ActivityStack.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ActivityStack();
                }
            }
        }
        return INSTANCE;
    }

    public void push(BaseActivity activity) {
        mActivities.push(new WeakReference<BaseActivity>(activity));
    }
    
    public void pop(BaseActivity activity) {
    	for (int i=0;i<mActivities.size();i++){
    		WeakReference<BaseActivity> cache = mActivities.get(i);
    		if (cache.get() == activity){
    			cache.clear();
    			mActivities.remove(cache);
    			break;
    		}
    	}
    	L.i(TAG, "pop: "+activity.getClass().getSimpleName());
    }
    
    public boolean has(Class target){
    	for (int i=0;i<mActivities.size();i++){
    		WeakReference<BaseActivity> cache = mActivities.get(i);
    		BaseActivity a = cache.get();
    		if (a != null && a.getClass().getName().equals(target.getName())){
    			return true;
    		}
    	}
    	return false;
    }
    
    public void finishActivity(Class<?> clz) {
        if (mActivities.size() > 0) {
            for (int i = 0; i < mActivities.size(); i++) {
            	WeakReference<BaseActivity> cache = mActivities.get(i);
            	BaseActivity a = cache.get();
            	if (a != null && a.getClass().equals(clz)){
            		a.finish();
            	} 
            }
        }
    }

    public Activity topActivity() {
        return mActivities.lastElement().get();
    }

    public Activity currentActivity() {
        return mActivities.peek().get();
    }

    public void finishAllActivityUntilCls(boolean isForceClose) {
        //		boolean isForceClose = false;
        /*while (mActivities.size() > 0) {
            finishActivity(currentActivity());
        }*/
    	for (int i=0;i<mActivities.size();i++){
    		WeakReference<BaseActivity> cache = mActivities.get(i);
    		Activity a = cache.get();
    		if (a != null){
    			a.finish();
    		}
    	}
        if (isForceClose) {
            Process.killProcess(Process.myPid());
        }
    }

    public int size() {
        return mActivities.size();
    }

    public void finishAllActivityExceptCls(Class target){
    	List<WeakReference<BaseActivity>> finished = new ArrayList<WeakReference<BaseActivity>>();
    	for(int i=0;i<mActivities.size();i++){
    		WeakReference<BaseActivity> cache = mActivities.get(i);
    		Activity a = cache.get();
    		if (a == null){
    			continue;
    		}
    		if (a.getClass().equals(target)){
    			continue;
    		}
    		finished.add(cache);
    	}
    	for(int i=0;i<finished.size();i++){
    		WeakReference<BaseActivity> cache = finished.get(i);
    		Activity a = cache.get();
    		mActivities.remove(a);
    		a.finish();
    	}
    }
}
