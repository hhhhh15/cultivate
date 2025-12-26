package com.example.cultivate.data.mapper

import com.example.cultivate.data.retrofit.model.SubTaskDto
import com.example.cultivate.data.room.entity.Subtask

fun SubTaskDto.toSubtask(taskId:Int,afterEndTaskId:Long):Subtask{//这个要的是保存到本地的task的自增id
    return Subtask(
        taskId=taskId,
        afterEndTaskId=afterEndTaskId,
        //后端获取的数据，本地entity设置不为空的变量一定要有值
        subtaskUid=this.subTaskUid,
        subtaskName=this.subTaskName,
        subject = this.subject,
        difficult=this.difficult,
        completed=this.completed,
        expectCompletedTime=this.expectCompletedTime,
        taskName=this.taskName,


    )
}
