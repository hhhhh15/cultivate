package com.example.cultivate.adapter

import com.example.cultivate.data.retrofit.model.SubTaskDto
import com.example.cultivate.data.room.entity.Subtask
import com.example.cultivate.data.room.entity.Task

interface onClickTransferEvent {
//    fun onClickEditCard(task: Task)
//    fun onClickTime(task: Task)
    fun onClickEditCard(task: Subtask)
    fun onClickTime(task: Subtask)
}