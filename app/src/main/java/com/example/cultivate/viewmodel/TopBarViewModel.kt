package com.example.cultivate.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TopBarViewModel :ViewModel(){

    data class TopBarState(val title:String,val id:Int?,val clickAction: (() -> Unit)? = null)

    val topBarState= MutableLiveData<TopBarState>()

    //方法，设置修改topBarState的值
    fun setTopBar(title: String,id: Int?,clickAction: (() -> Unit)? = null){
        Log.d("TopBarTest", "ViewModel 收到设置请求，title=$title, id=$id")
        topBarState.value=TopBarState(title,id,clickAction)
    }
}