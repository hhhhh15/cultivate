package com.example.cultivate.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cultivate.data.repository.SubTaskRepository
import com.example.cultivate.data.repository.TaskRepository
import com.example.cultivate.data.retrofit.model.SubTaskDto
import com.example.cultivate.data.room.entity.Subtask
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class taskDialogViewModel(application: Application): AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application)
    private val subtaskRepository = SubTaskRepository(application)
    private val TAG="taskDialogViewModel"

    private val _selectedImageUri = MutableLiveData<Uri>()
    val selectedImageUri: LiveData<Uri> = _selectedImageUri

    private val _taskTimeConsume=MutableLiveData<LocalTime>()
    val taskTimeConsume:LiveData<LocalTime> =_taskTimeConsume



    private val _selectedTask = MutableLiveData<Subtask>()
    val selectedTask: LiveData<Subtask> = _selectedTask

    private val _tasks = MutableLiveData<MutableList<Subtask>>()
    val tasks: LiveData<MutableList<Subtask>> = _tasks



        fun setTask(task: Subtask){
        _selectedTask.value=task
    }

    fun setImageUri(uri: Uri){
        _selectedImageUri.value=uri
    }

    fun setTaskItemTime(time: LocalTime){
        _taskTimeConsume.value=time
    }

    fun updateSubtaskToRoom(subtask: Subtask){
        viewModelScope.launch {
            Log.d(TAG, "updateTaskToRoom: 查询 task id = ${subtask.id}")
            val existingData = subtaskRepository.getSubtaskOfId(subtask.id)
            Log.d(TAG, "updateTaskToRoom: 查询结果 = $existingData")
            if (existingData != null){
                subtaskRepository.updateSubTaskData(subtask)
                Log.d(TAG, "updateTaskToRoom: 更新成功")
            } else {
                subtaskRepository.saveASubtask(subtask)
                Log.d(TAG, "updateTaskToRoom: 保存成功")
            }
        }

    }


    fun checkTaskComplete(subtask: Subtask): Boolean {
        val kClass = subtask::class
        return kClass.memberProperties.all { prop ->
            val value = (prop as KProperty1<Subtask, *>).get(subtask)
            when (value) {
                is String -> value.isNotBlank()
                else -> value != null
            }
        }
    }




}