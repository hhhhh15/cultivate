package com.example.cultivate;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;


public class MyApplication extends Application implements ViewModelStoreOwner {

    private ViewModelStore appViewModelStore;
    private ViewModelProvider.AndroidViewModelFactory appFactory;

    //2.设置一个能获取到viewmodelstore的例子的方法
    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return appViewModelStore;
    }

    //3.提供一个全局获取 ViewModel 的方法
    public <T extends ViewModel> T getAppViewModel(Class<T> modelClass) {
        return new ViewModelProvider(this, appFactory).get(modelClass);
    }
    @Override
    public void onCreate() {
        super.onCreate();

        // 1.初始化全局 ViewModelStore 和 Factory
        appViewModelStore = new ViewModelStore();
        appFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this);

        // 注册 Activity 生命周期监听器
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                setStatusBar(activity); // 设置沉浸式状态栏
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }


    // 设置沉浸式状态栏
    private void setStatusBar(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                WindowInsetsController controller = decorView.getWindowInsetsController();
                if (controller != null) {
                    controller.hide(WindowInsets.Type.statusBars());
                    controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                }
            }
        }
    }

}

