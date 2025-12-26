package com.example.cultivate.data.mapper

import com.example.cultivate.data.retrofit.model.TaskDto
import com.example.cultivate.data.room.entity.Task

//data class taskDto-> Entity task,用kotlin特性给taskDto加扩展函数方法
//左边是Task的属性，右边this.TaskDto的属性
fun TaskDto.toTask(backEndId:Long):Task{
    return Task(
        id=0,
        taskName=this.taskName,
        taskSubject=this.subject,
        startDate=this.startDate,
        deadlineDate=this.deadlineDate,
        completed=this.completed,
        difficulty=this.difficulty,
//        subtaskCompleted,
//        subtask
        subtasksCount=this.subtasksCount,
        afterEndId=backEndId,
    )
}