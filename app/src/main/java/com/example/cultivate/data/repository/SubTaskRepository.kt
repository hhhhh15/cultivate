package com.example.cultivate.data.repository

import android.content.Context
import com.example.cultivate.data.mapper.toSubtask
import com.example.cultivate.data.mapper.toTask
import com.example.cultivate.data.retrofit.RetrofitInstance
import com.example.cultivate.data.retrofit.model.TaskDto
import com.example.cultivate.data.retrofit.response.TaskResponse
import com.example.cultivate.data.room.databaseInstance
import com.example.cultivate.data.room.entity.Subtask
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime

class SubTaskRepository(context: Context) {
    private val db= databaseInstance.getInstance(context)
    private val subtaskdao=db.subtaskDao()
    private val taskdao=db.taskDao()

    suspend fun getSubtaskOfId(subtaskId:Int): Subtask? {
        return subtaskdao.getSubtaskById(subtaskId)
    }
    suspend fun getTotalTime(subtaskId: Int): LocalTime {
        return subtaskdao.getSubtaskById(subtaskId)?.setTotalTime?: LocalTime.MIDNIGHT
    }
    suspend fun saveASubtask(subtask: Subtask){//可能用不到，保存一个subtask数据
        subtaskdao.saveOneSubtask(subtask)
    }
    suspend fun  updateSubTaskData(subtask: Subtask){
        subtaskdao.updateSubtask(subtask)
    }

    //从网络端获取subtask，将数据存入本地room
    suspend fun getSubtaskData(taskdto: TaskDto, response: Response<TaskResponse>){
        if (!response.isSuccessful) {
            throw IllegalStateException("Response unsuccessful: ${response.code()}")
        }
        val body = response.body()
        val afterEndId = body?.taskId ?: throw IllegalStateException("后端没有返回taskId")

//保存到本地task表
        val task = taskdto.toTask(afterEndId)
        val taskNativeId=taskdao.saveTask(task)
//通过Id获取到对应后端划分的subtask数据
        val subtaskList= RetrofitInstance.subtaskApi.getSubtaskByTaskID(afterEndId)?.data
            ?: throw IllegalStateException("retrofit层调用getSubtaskByTaskID方法失败，没有根据($afterEndId)得subtask数据返回")

        val subtaskListForRoom = subtaskList.map { subtaskDto ->
            subtaskDto.toSubtask(taskNativeId.toInt(), afterEndId)
        }
        subtaskdao.saveAllSubtaskByTaskId(subtaskListForRoom.toList())//将所有task的子任务保存到本地
    }
//从本地获取到四个日期的subtask
suspend fun getFourDateSubtask():Map<String,List<Subtask>>{
    val today= LocalDate.now()
    val data0=subtaskdao.getSubtaskByDate(today.minusDays(1))
    val data1=subtaskdao.getSubtaskByDate(today)
    val data2=subtaskdao.getSubtaskByDate(today.plusDays(1))
    val data3=subtaskdao.getSubtaskByDate(today.plusDays(2))
    val result= mutableMapOf <String,List<Subtask>>()

    result.put("yesterday",data0)
    result.put("today",data1)
    result.put("tomorrow",data2)
    result.put("afterTomorrow",data3)
    return result
    }
}