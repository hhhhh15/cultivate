package com.example.cultivate.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.cultivate.data.room.entity.Task

@Dao
interface TaskDao {


    @Query("SELECT * FROM task")
    suspend fun getAllTask():MutableList<Task>

    @Insert
    suspend fun saveTask(task: Task):Long

    @Update
    suspend fun updateTask(task: Task)

}