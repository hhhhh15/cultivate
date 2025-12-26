package com.example.cultivate.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cultivate.data.repository.SubTaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class studyTimeViewModel(application: Application) : AndroidViewModel(application){
    private val subtaskRepository = SubTaskRepository(application)
    private val TAG="studyTimeViewModel"

    private val _taskId = MutableLiveData<Int>()
    val taskId: LiveData<Int> = _taskId

    private val _totalTime =MutableLiveData<Int>()
    val totalTime:LiveData<Int> = _totalTime

    private var _timeRemaining=MutableLiveData<Int>()
    val timeRemaining:LiveData<Int> =_timeRemaining

        fun getTotalTime(){
            Log.d("9.23", "3.vm中执行getTotalTime获取到totaltime ")
            Log.d(TAG, "getTotalTime:卧槽，这个放到是不是没有调用啊 ")
            viewModelScope.launch(Dispatchers.IO){
                taskId.value?.let {id->
                    _totalTime.postValue(subtaskRepository.getTotalTime(id).toSecondOfDay())
                    Log.d(TAG, "getTotalTime:看看${_totalTime.value} ")
                }
            }

        }
    // suspend 函数直接获取总时间
    suspend fun fetchTotalTime(): Int? {
        val id = taskId.value ?: return null
        val time = subtaskRepository.getTotalTime(id).toSecondOfDay()
        _totalTime.postValue(time)
        return time
    }

    fun setTaskId(id:Int){
        _taskId.value=id
        Log.d("9.23", "1.vm中方法setTaskId:设置 $_taskId")

    }
    fun setTotalTime(t:Int){
        _totalTime.value=t
    }
    fun updateTime(t:Int){
        //9.23之前这里报错了，因为setValue on a background thread，
        //因为这个 _timeRemaining.value = t 是在主线程修改的，但是现在是timer计时器是在子线程，所以不能setvalue，得需要使用postvalue才行，ok改完了，计时器在运动
        Log.d("9.23", "updateTime: ")
        _timeRemaining.postValue(t)
    }

    
}