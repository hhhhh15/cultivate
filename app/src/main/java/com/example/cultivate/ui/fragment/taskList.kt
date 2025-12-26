package com.example.cultivate.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cultivate.MyApplication
import com.example.cultivate.R
import com.example.cultivate.adapter.onClickTransferEvent
import com.example.cultivate.adapter.taskListAdapter
import com.example.cultivate.data.retrofit.model.SubTaskDto
import com.example.cultivate.data.room.databaseInstance
import com.example.cultivate.data.room.entity.Subtask
import com.example.cultivate.data.room.entity.Task
import com.example.cultivate.data.room.entity.User
import com.example.cultivate.databinding.TasklistBinding
import com.example.cultivate.ui.activity.Live2DActivity
import com.example.cultivate.ui.base.BaseViewBindingFragment
import com.example.cultivate.viewmodel.TaskListViewModel
import com.example.cultivate.viewmodel.studyTimeViewModel
import com.example.cultivate.viewmodel.taskDialogViewModel

class taskList : BaseViewBindingFragment<TasklistBinding>() {
    private val dialogVm: taskDialogViewModel by activityViewModels()
    private val timeVm: studyTimeViewModel by lazy {
        (requireActivity().application as MyApplication)
            .getAppViewModel(studyTimeViewModel::class.java)
    }
    private val tasklistVm: TaskListViewModel by activityViewModels()
    private lateinit var adapter: taskListAdapter
//    private  var taskList: MutableList<Task> = mutableListOf()
    private  var taskList: MutableList<Subtask> = mutableListOf()
    private val  TAG="taskList"

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): TasklistBinding {
        return TasklistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        //spinner组件
        tasklistVm.loadSubTask()//从后端获取到数据
        Log.d(TAG, "onViewCreated: 看看后端返回的有数据没有${tasklistVm.yesterdaySubtask.value}")
        tasklistVm.yesterdaySubtask.observe(viewLifecycleOwner) {updateListBySpinner()}
        tasklistVm.todaySubtask.observe(viewLifecycleOwner) {updateListBySpinner()}
        tasklistVm.tomorrowSubtask.observe(viewLifecycleOwner) {updateListBySpinner()}
        tasklistVm.dayAfterTomorrowSubtask.observe(viewLifecycleOwner) {updateListBySpinner()}

        binding.taskDateSpinner.onItemSelectedListener =object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                updateListBySpinner()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
        dialogVm.selectedImageUri.observe(viewLifecycleOwner){uri->
            if(uri!=null && dialogVm.selectedTask.value!=null){
                val updatetask=dialogVm.selectedTask.value!!.copy(imageUri = uri)
                dialogVm.setTask(updatetask)//更新一下vm里面的task数据
                adapter.updateTask(updatetask)
            }
        }
        //重复写一个和上面一模一样的，只不过是观察设置时间的变了,然后也是更新这个task
        dialogVm.taskTimeConsume.observe(viewLifecycleOwner){time->
            if(time!=null && dialogVm.selectedTask.value!=null){
                val updatetask=dialogVm.selectedTask.value!!.copy(setTotalTime = time)
                dialogVm.setTask(updatetask)//更新一下vm里面的task数据
                adapter.updateTask(updatetask)
                Log.d("taskDialog", "onViewCreated:tasklist里面看看这个时间设置数据陈工妹 ")
            }
        }

        //12.10用不上了
//        binding.textUser.setOnClickListener{
//            dialogVm.saveUser(User(0))
//        }
//        binding.textTask.setOnClickListener{
//            dialogVm.saveTask(Task(
//                1, 1, "完成对项目开发", "计算机",
//                "熟练掌握对ui控件的代码编写",
//                null, null
//            ))
//        }


        binding.deleteDatabase.setOnClickListener{
            databaseInstance.deleteDatabase(requireContext())
        }

        binding.startActivity.setOnClickListener {
            val intent = Intent(requireContext(), Live2DActivity::class.java)
            startActivity(intent)
        }


    }

    private fun initRecyclerView() {
        // 2. 设置 RecyclerView 的布局
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 3. 设置适配器
         adapter = taskListAdapter(taskList,object:onClickTransferEvent{
            override fun onClickTime(task: Subtask) {
                Log.d(TAG, "onClickTime: 进去计时页面")
                val taskId=task.id
                Log.d(TAG, "0.tasklist中onClickTime: 设置这个点击的item的id到vm中：$taskId")
                timeVm.setTaskId(taskId)
                Log.d("9.23", "2.tasklist中onClickTime中准备跳转到taskTimeStudy的fragment页面: ")
                changePage(taskTimeStudy.newInstance(taskId))
            }

            override fun onClickEditCard(task: Subtask) {
                Log.d("taskDialog", "准备弹出弹窗")
                if (isAdded) { // 安全判断：当前 Fragment 是否已经 attach 到 Activity
                    try {
                        dialogVm.setTask(task)
                        taskDialog().show(parentFragmentManager, "taskDialog")
                    } catch (e: Exception) {
                        Log.e("taskDialog", "弹窗失败: ${e.message}", e)
                    }
                } else {
                    Log.e("taskDialog", "Fragment 还没 attach，不能弹窗")
                }
            }
        })
        binding.taskRecyclerView.adapter = adapter
    }


    fun changePage(target:Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.task_fragment_container,target)
            .addToBackStack(null)
            .commit()
    }
    private fun updateListBySpinner() {
        val pos = binding.taskDateSpinner.selectedItemPosition

        taskList = when (pos) {
            0 -> tasklistVm.yesterdaySubtask.value?.toMutableList()
            1 -> tasklistVm.todaySubtask.value?.toMutableList()
            2 -> tasklistVm.tomorrowSubtask.value?.toMutableList()
            3 -> tasklistVm.dayAfterTomorrowSubtask.value?.toMutableList()
            else -> mutableListOf()
        } ?: mutableListOf()
        
        adapter.updateData(taskList)
    }



    }
