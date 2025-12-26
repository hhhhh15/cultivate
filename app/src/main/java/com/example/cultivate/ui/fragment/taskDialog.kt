package com.example.cultivate.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.cultivate.databinding.TasklistItemDialogBinding
import com.example.cultivate.utils.pickTool
import com.example.cultivate.viewmodel.taskDialogViewModel


class taskDialog: DialogFragment() {
    private lateinit var _binding:TasklistItemDialogBinding
    protected val binding get() = _binding
    private lateinit var hhh: List<View>
    private lateinit var imageActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val TAG:String="taskDialog"
    private val vm: taskDialogViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding =TasklistItemDialogBinding.inflate(layoutInflater)

         hhh= listOf(binding.modifyTaskLayout,binding.setTimeLayout)

        val dialog=Dialog(requireContext())
        dialog.setContentView(binding.root)

        showLayout()
        initLauncher()

        binding.editCardBackground.setOnClickListener {
            checkPermission()
        }
        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        binding.setTaskTotalTime.setOnClickListener{
            pickTool.showTimePicker(requireContext()){ time->
                val timeStr = time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
                binding.setTaskTotalTime.setText(timeStr)
                vm.setTaskItemTime(time)
            }
        }
        //暂时让弹窗里面的确定，就是当修改完这个subtask，不保存到room党重，那么设置背景图片就不能成功
//        binding.btnsave.setOnClickListener{
//            val task=vm.selectedTask.value
//            if (task != null) {
//                Log.d("taskDialog", "taskDialog中修改taskTimeConsume后调用这个保存数据的方$task: ")
//                vm.updateTaskToRoom(task)
//                dismiss()
//            } else {
//                Log.e(TAG, "task is null，保存失败")
//            }
//        }

        return dialog // 放在最后
    }

    fun showOnly(layouts:List<View>, showview:View){
        layouts.forEach {
            it.visibility=
                if (it==showview) View.VISIBLE else View.GONE
        }
    }


    fun showLayout(){
       val clickcontainer=View.OnClickListener {
           when(it){
               binding.btnModifyTask->showOnly(hhh,binding.modifyTaskLayout)
               binding.btnSetTime->showOnly(hhh,binding.setTimeLayout)
           }
       }
        binding.btnModifyTask.setOnClickListener(clickcontainer)
        binding.btnSetTime.setOnClickListener(clickcontainer)
    }

    fun initLauncher(){
         imageActivityLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        { result->
            if (result.resultCode == Activity.RESULT_OK) {  //查看这个activity是否真的存在
                val data = result.data
                val imageUri: Uri? = data?.data
                if (imageUri != null) {
                    vm.setImageUri(imageUri)
                    Log.d(TAG, "initLauncher: $imageUri")
                } else {
                    Log.e(TAG, "initLauncher: Image URI is null")
                }
            }
        }

         permissionLauncher=registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted:Boolean->
            if (isGranted){
                //申请成功，打开系统相册
                openPhoto()
            }else{
                Log.e("xxxerror", "onCreateDialog: 权限没有申请成功", )
            }
        }
    }

    fun openPhoto(){
        val intent=Intent(Intent.ACTION_PICK).apply {
            type="image/*"
        }
        imageActivityLauncher.launch(intent)
    }

    fun checkPermission(){
        //根据不同版本获取不同的权限
        val permission= if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            android.Manifest.permission.READ_MEDIA_IMAGES
        }else{
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        Log.d(TAG, "checkPermission: ")
        //判断这个权限是否已经存在获取
        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            == PackageManager.PERMISSION_GRANTED)
        {
            openPhoto()
        }else{
            //再次申请权限
            permissionLauncher.launch(permission)
        }
    }








}