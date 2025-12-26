package com.example.cultivate.data.room

import android.content.Context
import androidx.room.Room


object databaseInstance{
    @Volatile
    private var INSTANCE:AppDatabase?=null
    fun getInstance(context: Context): AppDatabase{
        return INSTANCE?: synchronized(this){
            val instance= Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "shuca")
//                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
  }
    fun deleteDatabase(context:Context){
        INSTANCE?.close()  //不是空，就调用这个close方法
        INSTANCE=null
        context.deleteDatabase("shuca")
    }

}