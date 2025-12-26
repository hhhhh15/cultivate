package com.example.cultivate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cultivate.data.repository.SubTaskRepository
import com.example.cultivate.data.retrofit.RetrofitInstance.subtaskApi
import com.example.cultivate.data.retrofit.RetrofitInstance.taskApi
import com.example.cultivate.data.retrofit.model.SubTaskDto
import com.example.cultivate.data.room.entity.Subtask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskListViewModel(application: Application): AndroidViewModel(application) {
    // 四组数据
    private val _yesterdaySubtask = MutableLiveData<List<Subtask>>()
    val yesterdaySubtask: LiveData<List<Subtask>> = _yesterdaySubtask

    private val _todaySubtask = MutableLiveData<List<Subtask>>()
    val todaySubtask: LiveData<List<Subtask>> = _todaySubtask

    private val _tomorrowSubtask = MutableLiveData<List<Subtask>>()
    val tomorrowSubtask: LiveData<List<Subtask>> = _tomorrowSubtask

    private val _dayAfterTomorrowSubtask = MutableLiveData<List<Subtask>>()
    val dayAfterTomorrowSubtask: LiveData<List<Subtask>> = _dayAfterTomorrowSubtask

    private val subTaskRepository=SubTaskRepository(application)

    fun loadSubTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val result: Map<String, List<Subtask>> =subTaskRepository.getFourDateSubtask()
            try {
                withContext(Dispatchers.Main){
                    _yesterdaySubtask.value=result["yesterday"]?: emptyList()
                    _todaySubtask.value=result["today"]?: emptyList()
                    _tomorrowSubtask.value=result["tomorrow"]?: emptyList()
                    _dayAfterTomorrowSubtask.value=result["afterTomorrow"]?: emptyList()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}