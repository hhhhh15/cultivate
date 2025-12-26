@file:JvmName("BindingAdapters")
package com.example.cultivate.adapter

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.cultivate.R
import java.time.LocalDate

//10.13顶层函数，不是类，所以需要这个@file注解，这样才能扫描编译到这个文件
//这个@BindingAdapter是绑定的是单个组件view和这个vm中设置的livedata变量的，所以
//10.17方法中的这个参数类型是String是因为这个xml中设置的@{viewModel.difficulty}中的变量就是这个类型
//数据改变组件状态
    @BindingAdapter("difficulty")
        fun setDataInViewStatus(group:RadioGroup,data:String?){
            val button =when(data){
                "easy"-> R.id.radio_easy
                "medium"->R.id.radio_medium
                "hard"->R.id.radio_hard
                else->-1
            }
            //选中button的id，调用这个RadioGroup的check方法
            if(button!=-1){
                group.check(button)
            }else{
                data==null
                group.clearCheck()
            }
        }
//组件获取数据，为啥不直接吧这个数据传递给viewmodel啊，
// ——因为这个return是把数据传递给这个databinding框架了，因为databinding不是绑定了这个
    @InverseBindingAdapter(attribute = "difficulty",event="difficultyAttrChanged")
        fun getDataFromView(group:RadioGroup):String{
            return when(group.checkedRadioButtonId){
                R.id.radio_easy->"easy"
                R.id.radio_medium->"medium"
                R.id.radio_hard->"hard"
                else->""
            }
        }

    @BindingAdapter("difficultyAttrChanged")
    fun setGroupListener(group:RadioGroup,listener:InverseBindingListener?){
        if (listener == null) return
        group.setOnCheckedChangeListener{
        //这个重写的listener的方法里面执行的这句代码listener?.onChange()啥意思？
            _,_->listener?.onChange()
        }
    }

//时间绑定
    @BindingAdapter("startTimeText")
    fun setStartTimeText(view: TextView, value: LocalDate?) {
        view.text = value?.toString() ?: ""
    }

    @InverseBindingAdapter(attribute = "startTimeText",event="startTimeTextAttrChanged")
    fun getStartTimeText(view: TextView): LocalDate? {
        val text = view.text?.toString()
        return if (text.isNullOrBlank()) null else LocalDate.parse(text)
    }

    @BindingAdapter("deadlineTimeText")
    fun setDeadlineTimeText(view: TextView, value: LocalDate?) {
        view.text = value?.toString() ?: ""
    }

    @InverseBindingAdapter(attribute = "deadlineTimeText",event="startTimeTextAttrChanged")
    fun getDeadlineTimeText(view: TextView): LocalDate? {
        val text = view.text?.toString()
        return if (text.isNullOrBlank()) null else LocalDate.parse(text)
    }

    @BindingAdapter("startTimeTextAttrChanged")
    fun setStartTimeListener(view: TextView, listener: InverseBindingListener?) {
        if (listener != null) {
            view.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    listener.onChange()
                }
            }
        }
    }


    //第二个组件Spinner下拉组件
//在普通使用该组件的时候，需要使用这个ArrayAdapter适配器，器构造函数val adapter = ArrayAdapter(
//    this,                                    // 上下文 Context（当前 Activity）
//    android.R.layout.simple_spinner_item,    // 每个下拉项的布局样式
//    subjects                                 // 数据列表
//)，adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//上面的哪个属性是设置下拉时显示样式
//2.Spinner本身就有一个属性时adapter马，继承AdapterView<SpinnerAdapter>
//3.想要获取到选中项的数据，监听器调用onItemSelectedListener，这个是接口，有两个方法需要重写，onItemSelected，onNothingSelected
//4.所以要使用这个组件，需要创建适配器绑定数据，所以有两个参数，一个是列表数据，一个是选中发数据
//普通使用就是一个数据绑定适配器，一个是设置监听器使用上面的方法，除此之外，其他的提取数据就是databinding框架的
    @BindingAdapter("entries")
    fun bindSpinnerList(spinner:Spinner,entries:List<String>){
        entries?.let {
            val adapter=ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item,entries)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter=adapter
        }
    }
//10.17既然用indexof（）方法获取到postion的话，那list数据从哪里获取？——从spinner.adapter这个属性获取
@BindingAdapter("selectedItem")
    fun setSpinner(spinner:Spinner,data:String?){
        if(data !=null && spinner.adapter!=null){
//因为spinner.adapter 返回的是 SpinnerAdapter 接口类型，所以不能使用这句话:val position=spinner.adapter.getPosition
        // 因为 ArrayAdapter 是 SpinnerAdapter 的一个实现类，且有这个方法fun getPosition(item: T): Int，所以创建这个类别的类
            val adapter=spinner.adapter as ArrayAdapter<String>
            val position =adapter.getPosition(data)
            if(position>=0 && spinner.selectedItemPosition!=position){
                spinner.setSelection(position,false)
            }
        }
    }
//回流，是从view回到viewmodel啊，真不错
@InverseBindingAdapter(attribute="selectedItem",event="selectedSpinnerAttChanged")
    fun getSpinnerData(spinner:Spinner):String{
        return spinner.selectedItem as String
    }
//设置监听器,我是真不知道这个设置的监听器，todo里面结局就一句话？listener.onChange()
@BindingAdapter("selectedSpinnerAttChanged")
    fun setSpinnerListener(spinner:Spinner,listener: InverseBindingListener?){
        if(listener==null) return
        spinner.onItemSelectedListener =object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                listener.onChange()  // ✅ 这里只负责通知 databinding 触发反向同步
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

@BindingAdapter("divide")
    fun setData(group:RadioGroup,data:String?){
        val button =when(data){
            "auto"-> R.id.radio_divide_by_system
            "manual"->R.id.radio_divide_by_yourself
            else->-1
        }
        if(button!=-1){
            group.check(button)
        }else{
            data==null
            group.clearCheck()
        }
    }

@InverseBindingAdapter(attribute = "divide",event="divideAttrChanged")
fun getData(group:RadioGroup):String{
    return when(group.checkedRadioButtonId){
        R.id.radio_divide_by_system->"auto"
        R.id.radio_divide_by_yourself->"manual"
        else->""
    }
}

@BindingAdapter("divideAttrChanged")
fun setListener(group:RadioGroup,listener:InverseBindingListener){
    if (listener == null) return
    group.setOnCheckedChangeListener{
            _,_->listener?.onChange()
    }
}