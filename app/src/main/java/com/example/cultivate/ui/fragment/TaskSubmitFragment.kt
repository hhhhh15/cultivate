package com.example.cultivate.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cultivate.R
import com.example.cultivate.databinding.TasksubmitBinding

import com.example.cultivate.ui.base.BaseDataBindingFragment
import com.example.cultivate.viewmodel.TaskSubmitViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class TaskSubmitFragment : BaseDataBindingFragment<TasksubmitBinding>() {
    private val taskSubmitViewModel : TaskSubmitViewModel by viewModels()
    private val _subtask = mutableListOf<String>() //已经初始化了mutableList<String>，val是内容可变，引用不变
    val subtask: List<String> get()=_subtask
    private val TAG="TaskSubmitFragment"
    private var lastClickTime = 0L

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): TasksubmitBinding {
        return TasksubmitBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitVm=taskSubmitViewModel

        binding.addSubtask.setOnClickListener {
            DynamicLayout()
        }
        binding.saveAllSubtask.setOnClickListener {
            taskSubmitViewModel.setManualTask(getSubtaskData())//点保存，将manual的数据全传递
        }
        binding.btnSubmitTask.setOnClickListener {
            val now=System.currentTimeMillis()
            if((now-lastClickTime)<1000){//得和上一次点击间隔1s以上，否则弹窗警告
                AlertDialog.Builder(requireContext())
                    .setTitle("提示")
                    .setMessage("请勿频繁点击")
                    .setPositiveButton("确定") { dialog, _ -> dialog.dismiss() }
                    .show()
                return@setOnClickListener
            }
            lastClickTime=now//因为一直没有更新这个lastClickTime导致没有防抖动

            val taskDto=taskSubmitViewModel.prepareForPost()
            Log.d(TAG, "onViewCreated:btnSubmitTask.setOnClickListener中$taskDto ")
            if(taskDto==null){
                Toast.makeText(requireContext(), "请填写所有必填字段", Toast.LENGTH_SHORT).show()
                return@setOnClickListener//退出当前点击事件
            }
            //按要求填写好之后，调用上传到retrofit的方法
            viewLifecycleOwner.lifecycleScope.launch{
                val logMessage = try {
                    taskSubmitViewModel.submitTask(taskDto).await()
                } catch (e: Exception) {
                    Log.e(TAG, "提交任务失败", e)
                    e.message
                }
                Toast.makeText(requireContext(), logMessage, Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvStartTime.setOnClickListener{
            showDatePicker { date ->
                taskSubmitViewModel.startTime.value = date  // ✅ 更新 ViewModel 变量（会触发 @= 反向同步到UI）
            }
        }
        binding.tvEndTime.setOnClickListener{
            showDatePicker { date ->
                taskSubmitViewModel.endTime.value = date  // ✅ 更新 ViewModel 变量（会触发 @= 反向同步到UI）
            }
        }
        taskSubmitViewModel.errorMsg.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

    }

    //动态添加布局，得 把这个item弄一个xml布局才行，才能调用这个R.layout
    fun DynamicLayout(){
        val itemView = layoutInflater.inflate(R.layout.tasksubmit_item, binding.subtaskContainer, false)

        val deleteBtn=itemView.findViewById<Button>(R.id.btn_delete_subtask)
        deleteBtn.setOnClickListener{
            binding.subtaskContainer.removeView(itemView)
        }
        binding.subtaskContainer.addView(itemView)
    }

    fun getSubtaskData():List<String>{
        _subtask.clear()
        val count = binding.subtaskContainer.childCount
        for(i in 0 until count){
            val view = binding.subtaskContainer.getChildAt(i);
            val editText = view.findViewById<EditText>(R.id.edit_subtask_text)
            _subtask.add(editText.text.toString())
        }
        _subtask.removeAll { it.isEmpty() }
        Log.d(TAG, "getSubtaskData:${subtask.toString()} ")
        return subtask
    }
    private fun showDatePicker(onSelected: (LocalDate) -> Unit) {
        val today = LocalDate.now()
        val picker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val date = LocalDate.of(year, month + 1, day)
                onSelected(date)
            },
            today.year, today.monthValue - 1, today.dayOfMonth
        )
        picker.show()
    }




}

