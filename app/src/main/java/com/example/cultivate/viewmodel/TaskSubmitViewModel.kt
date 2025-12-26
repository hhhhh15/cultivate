package com.example.cultivate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cultivate.data.repository.SubTaskRepository
import com.example.cultivate.data.repository.TaskRepository
import com.example.cultivate.data.retrofit.NetworkException

import com.example.cultivate.data.retrofit.model.TaskDto
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.time.LocalDate

class TaskSubmitViewModel(application: Application) : AndroidViewModel(application) {

    val taskName = MutableLiveData<String>()
    val taskCount = MutableLiveData<Int?>()
    val startTime = MutableLiveData<LocalDate?>()
    val endTime = MutableLiveData<LocalDate?>()
    val taskDifficulty = MutableLiveData<String?>()
    val taskSubject = MutableLiveData<String>()
    val taskType = MutableLiveData<String?>()

    //10.17之后我想要添加一个能自动添加学科类型的按键，对了，对于是中文的记得要转换成英文，算了，转不转换都行
    val subjectList=MutableLiveData<List<String>>().apply {
        value= listOf("代码","数学","建模","OpenGl","C++")
    }
    private val subtaskOfManual=MutableLiveData<List<String>>()
    private val taskRepository = TaskRepository(application)
    private val subtaskRepository = SubTaskRepository(application)
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg
    private var submitJob:Deferred<String>?=null

    fun setManualTask(a:List<String>){
        subtaskOfManual.value=a.toList()
    }
    fun prepareForPost():TaskDto?{
        val name = taskName.value
        val count = taskCount.value
        val start = startTime.value
        val end = endTime.value
        val difficulty = taskDifficulty.value
        val subject = taskSubject.value
        val division = taskType.value
        val subtasks = subtaskOfManual.value ?: emptyList()

        val fields = hashMapOf<String, Any?>(
            "任务名称" to name,
            "数量" to count,
            "起始时间" to start,
            "截止时间" to end,
            "难度" to difficulty,
            "学科" to subject,
            "类型" to division
        )

        val emptyField = fields.entries.firstOrNull {
            it.value == null || (it.value is String && (it.value as String).isBlank())
        }

        if (emptyField != null) {
            _errorMsg.value = "${emptyField.key} 不能为空"
            return null
        }
        return TaskDto(
            taskName = name!!,
            subtasksCount = count!!,
            startDate = start!!,
            deadlineDate = end!!,
            difficulty = difficulty!!,
            divisionType = division!!,
            subject = subject!!,
            completed=false,
            subtask = subtasks,
        )
    }

      fun submitTask(taskdto:TaskDto):Deferred<String> {//这个是一个普通方法
       return submitJob?:viewModelScope.async(Dispatchers.IO) {
           try {
               val response=taskRepository.postTaskDto(taskdto)//在这里捕捉到异常，直接跳到catch代码块了，下面的if都不执行了
               val data = response.body()
               if(response.isSuccessful){
                   //上传成功后获取到子任务，并把task保存到room中
                   subtaskRepository.getSubtaskData(taskdto,response)
                   withContext(Dispatchers.Main) {
                       resetForm()//清空填写的表单，上传成功，切换主线程才能setValue
                   }
                   return@async  "成功：${data?.message}"//这个就是submitTask.await()会返回的内容
               }else{
                   return@async "失败：状态码是${response.code()}，"+"原因是：${data?.message}"
               }
           }catch (e: NetworkException) {
               // 这是拦截器抛出的异常
               e.message ?: "网络异常"
           }catch(e: Exception) {
               "非网络异常：${e.message}"
           }finally {
               submitJob=null
           }
       }.also { submitJob=it }

    }
    fun resetForm(){
        taskName.value = ""
        taskCount.value = null
        startTime.value = null
        endTime.value = null

        subjectList.value?.let { list ->//因为subjectList是Livedata类型，得先取值才能调用list的isNotEmpty()方法
            if (list.isNotEmpty()) {
                taskSubject.value = list[0]
            }
        }
        taskDifficulty.value = null
        taskType.value = null
    }


}