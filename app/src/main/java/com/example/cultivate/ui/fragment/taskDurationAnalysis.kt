package com.example.cultivate.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cultivate.databinding.TasktimeanalysisBinding
import com.example.cultivate.ui.base.BaseViewBindingFragment

class taskDurationAnalysis : BaseViewBindingFragment<TasktimeanalysisBinding>(){
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?):TasktimeanalysisBinding{
        return TasktimeanalysisBinding.inflate(inflater,container,false)
    }
}