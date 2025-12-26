package com.example.cultivate.data.repository

import android.content.Context
import android.util.Log
import com.example.cultivate.data.mapper.toSubtask
import com.example.cultivate.data.mapper.toTask
import com.example.cultivate.data.retrofit.RetrofitInstance.subtaskApi
import com.example.cultivate.data.retrofit.RetrofitInstance.taskApi
import com.example.cultivate.data.retrofit.response.TaskResponse
import com.example.cultivate.data.retrofit.model.TaskDto
import com.example.cultivate.data.room.databaseInstance
import com.example.cultivate.data.room.entity.Task
import retrofit2.Response

class TaskRepository(context: Context) {
    init {
        Log.d("REPO_INIT", "✅ TaskRepository 初始化完成")
    }
    private val db=databaseInstance.getInstance(context)
    private val taskdao=db.taskDao()



    suspend fun saveTask(task: Task){
        taskdao.saveTask(task)
    }

    suspend fun  updateToSaveTask(task: Task){
        taskdao.updateTask(task)
    }



    suspend fun getAllTask():MutableList<Task>{
        return  taskdao.getAllTask()
    }



    //上面是数据库存储，下面是retrofit网络请求的方法
    suspend fun postTaskDto(taskdto: TaskDto): Response<TaskResponse> {
        return if (taskdto.divisionType == "auto" && taskdto.subtask.isEmpty()) {
            taskApi.postAutoTasks(taskdto)
        } else if (taskdto.divisionType == "manual" && taskdto.subtask.isNotEmpty()) {
            taskApi.postManualTasks(taskdto)
        } else {
            throw IllegalArgumentException("任务类型与子任务列表不匹配")
        }
    }


}