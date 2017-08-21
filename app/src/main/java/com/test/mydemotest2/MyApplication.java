package com.test.mydemotest2;


import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;
/**
 * Created by Administrator on 2017/8/21 0021.
 */

public class MyApplication extends Application {
    private static Context context;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        flag = false;
    }

    public static Context getContext(){
        return context;
    }
}
