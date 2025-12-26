package com.example.cultivate.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.cultivate.data.room.entity.User

@Dao
interface userDao {
    @Insert
    suspend fun saveUsers(user: User)
}