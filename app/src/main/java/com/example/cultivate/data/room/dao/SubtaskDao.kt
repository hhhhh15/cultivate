package com.example.cultivate.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.cultivate.data.room.entity.Subtask
import com.example.cultivate.data.room.entity.Task
import java.time.LocalDate

@Dao
interface SubtaskDao {
    @Insert
    suspend fun saveOneSubtask(subtask: Subtask)

    @Insert
    suspend fun saveAllSubtaskByTaskId(subtaskList:List<Subtask>)

    @Query("SELECT * FROM subtask WHERE id = :id")
    suspend fun getSubtaskById(id: Int): Subtask?

    @Query("SELECT * FROM subtask Where expectCompletedTime=:date AND completed=false")
    suspend fun getSubtaskByDate(date:LocalDate):List<Subtask>

    @Update
    suspend fun updateSubtask(subtask:Subtask)
}