package com.example.cultivate.live2d;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class JniBridgeJava {
    //调用native层的方法接口
    public static native void nativeOnStart();

    public static native void nativeOnPause();

    public static native void nativeOnStop();

    public static native void nativeOnDestroy();

    public static native void nativeOnSurfaceCreated();

    public static native void nativeOnSurfaceChanged(int width, int height);

    public static native void nativeOnDrawFrame();

    public static native void nativeOnTouchesBegan(float pointX, float pointY);

    public static native void nativeOnTouchesEnded(float pointX, float pointY);

    public static native void nativeOnTouchesMoved(float pointX, float pointY);
    //java层的方法，被native层调用
    public static void SetContext(Context context) {
        JniBridgeJava.context = context;
    }

    public static void SetActivityInstance(Activity activity) {
        activityInstance = activity;
    }

    //获取asset列表，输入dirPath，但是c++层调用GetAssetList、LoadFile方法不是自己执行，是给LAppLive2DManager.cpp文件的SetUpModel使用的
    //所以要换模型，还有输入asset的地址，需要在执行这两个方法的LAppLive2DManager.cpp中修改
    public static String[] GetAssetList(String dirPath) {
        try {
            return context.getAssets().list(dirPath);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static byte[] LoadFile(String filePath) {
        Log.d("Live2D", "Trying to load: " + filePath);
        InputStream fileData = null;
        try {
            fileData = context.getAssets().open(filePath);
            int fileSize = fileData.available();
            byte[] fileBuffer = new byte[fileSize];
            fileData.read(fileBuffer, 0, fileSize);
            return fileBuffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileData != null) {
                    fileData.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void MoveTaskToBack() {
        activityInstance.moveTaskToBack(true);
    }

    private static Activity activityInstance;
    private static Context context;
    private static final String LIBRARY_NAME = "Live2dDemo";

    static {
        System.loadLibrary(LIBRARY_NAME);
    }
}
