package com.example.cultivate.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import com.example.cultivate.MyApplication
import com.example.cultivate.R
import com.example.cultivate.service.TimeService
import com.example.cultivate.viewmodel.studyTimeViewModel

class activityForService:AppCompatActivity() {
    // Activity
//    val vm = ViewModelProvider(this).get(studyTimeViewModel::class.java)
    //9.28这个viewmodel没有实现全局的调用的啊，靠北
//    private lateinit var vm: studyTimeViewModel

    private val vm: studyTimeViewModel by lazy {
        (application as MyApplication).getAppViewModel(studyTimeViewModel::class.java)
    }

    private var startTime:Int?=null
    lateinit var timerBinder: TimeService.timer
    private val TAG="activityForService"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_service)


        val totalTime=intent.getIntExtra("startTime",0)
        startTime=totalTime
        Log.d("9.23", "6.activity中onCreate中获取到传递来的值$totalTime: ")


        val intent= Intent(this,TimeService::class.java)
        startForegroundService(intent)
        bindService(intent,connection, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "onCreate: 前台service绑定成功？")

    }

    private val connection= object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected: Service 已连接")

            timerBinder=service as TimeService.timer
            //不是说这个onServiceConnected是activity和service成功绑定会运行，？？然后调用service的方法都在这里操作？
            //9.12这个应该是第二次，不是初始跳转到页面，第二次计时的时候，Binder对象已经加载过。观察totaltime，
            Log.d("9.23", "7.onServiceConnected中调用计时器: ")
            tryStartTimer()
        }
        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    fun tryStartTimer(){
        val time = startTime
        if ( time != null && time > 0) {
            Log.d(TAG, "tryStartTimer: 启动计时器 totalTime=$time")
            timerBinder.startTimer(time) { currentTime ->
                Log.d(TAG, "计时器回调 currentTime=$currentTime")
                Log.d("9.23", "计时器回调 currentTime=$currentTime")
                vm.updateTime(currentTime)
            }
            startTime = null
        } else {
            Log.d(TAG, "tryStartTimer: 条件未满足startTime=$time")
        }
    }

    override fun onDestroy() {
        unbindService(connection)
        super.onDestroy()
    }
}