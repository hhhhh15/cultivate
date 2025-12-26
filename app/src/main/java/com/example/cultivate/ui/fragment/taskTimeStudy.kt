package com.example.cultivate.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cultivate.MyApplication
import com.example.cultivate.databinding.StudyroomBinding
import com.example.cultivate.ui.activity.activityForService
import com.example.cultivate.ui.base.BaseViewBindingFragment
import com.example.cultivate.viewmodel.studyTimeViewModel

class taskTimeStudy: BaseViewBindingFragment<StudyroomBinding>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): StudyroomBinding {
        return StudyroomBinding.inflate(inflater,container,false)
    }

    private val vm: studyTimeViewModel by lazy {
        (requireActivity().application as MyApplication)
            .getAppViewModel(studyTimeViewModel::class.java)
    }

    private var startTime:Int?=null
// Fragment
//val vm = ViewModelProvider(requireActivity()).get(studyTimeViewModel::class.java)
    private  val TAG="taskTimeStudyFragment"

    companion object{
        fun newInstance(taskId:Int):taskTimeStudy{
            val hh=taskTimeStudy()
            val args=Bundle().apply {
                putInt("taskId",taskId)
            }
            hh.arguments=args
            return  hh
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskid=arguments?.getInt("taskId")?:1
        Log.d(TAG, "onViewCreated: $taskid")

        Log.d("9.23", "3.0fragment的onViewCreated中查看vm里面的taskid变量是否有值${vm.taskId.value}: ")
        vm.getTotalTime()

        //9.23这里明明有设置了观察则模式，观察这两个变量来修改ui，但是我检查了，计时器是一直运行的，说明这个值timeRemaining也是在变化的，但是ui不行，
        // 还是因为这个作用域不行啊，没有共享这个vm。有没有好的解决方法啊

        vm.timeRemaining.observe(viewLifecycleOwner){
            Log.d("9.23", "8.0.onViewCreated: fragment中查看剩余时间($it)是否修改")
            binding.StudyTimerView.updateTimeRemaining(it)
        }

        //9.23有一个隐患就是这个vm.totaltime注意要小心不要变化，
        vm.totalTime.observe(viewLifecycleOwner){
            Log.d("9.23", "4.在fragment中检测到totaltime值改变了，则获取到这个值$it ")
            startTime=vm.totalTime.value
            val time=startTime
            if (time!=null && time>0 ) {
                Log.d("9.23", "5.已经拿到totaltime，fragment中准备将这个putExtra传递到activity中并启动activity。？？不知道这里的ui是怎么显示的: ")
                val intent = Intent(requireContext(), activityForService::class.java).apply {
                    putExtra("startTime",time)
                }
                binding.StudyTimerView.setFixedTime(time)
                startActivity(intent)
            }
        }

    }
}