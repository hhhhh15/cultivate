package com.example.cultivate.ui.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cultivate.R
import com.example.cultivate.databinding.TaskModuleBinding
import com.example.cultivate.ui.base.BaseViewBindingFragment
import com.example.cultivate.viewmodel.TopBarViewModel

class taskModule: BaseViewBindingFragment<TaskModuleBinding>() {
    private val vm:TopBarViewModel by activityViewModels()
    private val TAG="showskip"

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): TaskModuleBinding {
        return TaskModuleBinding.inflate(inflater, container, false)
    }
    @SuppressLint("FragmentBackPressedCallback")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jumpToSubPageOfTask()

        if (childFragmentManager.findFragmentById(R.id.task_fragment_container)==null){
            changeFragment(taskList())
        }
        Log.d("taskModule", "onViewCreated 调用")
        Log.d("TopBarTest", "Fragment ViewModel hash = ${vm.hashCode()}")
        vm.setTopBar("任务模块", R.drawable.add_24){
            // 点击事件在这里执行
            childFragmentManager.beginTransaction()
                .replace(R.id.task_fragment_container, TaskSubmitFragment())
                .addToBackStack(TaskSubmitFragment::class.java.simpleName)
                .commit()
        }


    }


    fun changeFragment(target:Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.task_fragment_container,target)
            .addToBackStack(target::class.java.simpleName)
            .commit() // 异步
        childFragmentManager.executePendingTransactions() // 强制立即执行，但仍可加栈

    }
    fun jumpToSubPageOfTask(){
        Log.d(TAG, "jumpToSubPageOfTask: 看看taskmodule中上面三个响应没")
        val clicklistener=View.OnClickListener {
            when(it){
                binding.menuTaskList-> changeFragment(taskList())
                binding.menuCalendar->changeFragment(taskCalendar())
                binding.menuDuration->changeFragment(taskDurationAnalysis())
            }
        }
        binding.menuCalendar.setOnClickListener(clicklistener)
        binding.menuTaskList.setOnClickListener(clicklistener)
        binding.menuDuration.setOnClickListener(clicklistener)
    }

}