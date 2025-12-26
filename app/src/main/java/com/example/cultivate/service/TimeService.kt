package com.example.cultivate.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.cultivate.R
import java.util.Timer
import java.util.TimerTask

class TimeService : Service() {
    private val TAG="TimeService"
    private  val  mBinder=timer()
    inner class timer:Binder(){

        fun startTimer(totaltime:Int,tick:(Int)->Unit){
            Log.d(TAG, "Binder.startTimer called with totaltime=$totaltime")
            var time=totaltime
            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if (time>0){
                        //为啥在service中调用vm的更新方法,使用回调，修改，因为service里面最好不用这个vm，会导致内存泄漏的问题的
                        tick(time)
                        time--;
                    }
                }
            },0,1000)
        }
        
        fun stopTimer(){
            Log.d(TAG, "stopTimer: ")
        }

    }
    override fun onCreate() {
        super.onCreate()
        //前台service创建
        Log.d(TAG, "onCreate: 创建前台service")
        val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //Android版本判断，Android8以上才有通知渠道
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel=NotificationChannel("timeService","前台计时器通知",NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created")
        }
//        val intent=Intent(this,taskTimeStudy::class.java)
//        //?这里是getActivity方法啊，但是这个页面taskTimeStudy是fragment页面，还有最后一个参数改了，从Android12开始，PendingIntent对象是可变还是不可变
//        val pi=PendingIntent.getActivity(this,0,intent,FLAG_IMMUTABLE)
        val notification=NotificationCompat.Builder(this,"timeService")
            .setContentTitle("计时器通知")
            .setContentText("这是一个计时通知，String类型,想弄一个显示器，能显示计时的进度")
            .setSmallIcon(R.drawable.lishen1)
            //这个大的啥意思，是解码吗？还是咋滴
//            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.handsome))
//            .setContentIntent(pi)
            .build()
        //记得注册表中再多加一个属性，表示这个是前台service
        try {
            startForeground(1, notification)
            Log.d(TAG, "Foreground service started successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start foreground service", e)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
       return mBinder
    }
}