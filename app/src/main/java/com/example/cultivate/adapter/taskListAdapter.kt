package com.example.cultivate.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cultivate.data.room.entity.Subtask
import com.example.cultivate.databinding.TasklistItemBinding

class taskListAdapter(
//    private val taskList: MutableList<Task>,//而且我这里设置了taskList的来源是Task类啊，是从数据库中提取的
    private val taskList:MutableList<Subtask>,
    private val onclick: onClickTransferEvent
) : RecyclerView.Adapter<taskListAdapter.TaskViewHolder>() {
    private val TAG="taskListAdapter"

    // ViewHolder 内部类
    class TaskViewHolder(val binding: TasklistItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TasklistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        val taskId=task.id


        holder.binding.setTotalTime.text = "计划用时:${task.setTotalTime}"

        holder.binding.editcard.setOnClickListener {
            Log.d("任务卡打开编译弹窗", "TaskAdapter 的 onBindViewHolder")
            onclick.onClickEditCard(task)

        }
        //跳转页面
        holder.binding.btnTime.setOnClickListener{
//            Log.d(TAG, "onBindViewHolder: 查看这个点击的item的id：$taskId")
            onclick.onClickTime(task)
        }
        // 设置背景图片
        val uri = task.imageUri
        if (uri != null && uri.toString().isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(task.imageUri)
                .into(holder.binding.taskItemImageView)
        }

    }

    override fun getItemCount(): Int = taskList.size

    fun updateTask(updatetask: Subtask){
        //task已经是在tasklist中copy更新过了
        val index=taskList.indexOfFirst {
            it.id==updatetask.id
        }
        taskList[index]=updatetask
        notifyItemChanged(index)
    }
    fun updateData(newList: List<Subtask>) {
        taskList.clear()
        taskList.addAll(newList)
        notifyDataSetChanged()
    }

}
