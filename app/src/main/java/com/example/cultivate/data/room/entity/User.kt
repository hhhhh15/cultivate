package com.example.cultivate.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
    var id:Int
)