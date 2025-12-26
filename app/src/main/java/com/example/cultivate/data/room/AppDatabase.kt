package com.example.cultivate.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cultivate.data.room.converter.Converters
import com.example.cultivate.data.room.dao.SubtaskDao
import com.example.cultivate.data.room.dao.TaskDao
import com.example.cultivate.data.room.dao.userDao
import com.example.cultivate.data.room.entity.Subtask
import com.example.cultivate.data.room.entity.Task
import com.example.cultivate.data.room.entity.User

@Database(entities = [Task::class,User::class,Subtask::class], version = 1)
@TypeConverters(Converters::class)
 abstract  class AppDatabase : RoomDatabase(){
     abstract  fun taskDao():TaskDao
     abstract  fun userDao():userDao
     abstract  fun subtaskDao():SubtaskDao
}