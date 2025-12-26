package com.example.cultivate.utils

import androidx.databinding.InverseMethod
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Converter {
    @JvmStatic
    @InverseMethod("StringViewToIntVm")
    fun intVmToStringView(value: Int?):String=value?.toString()?:"" //默认值都设置完了

    @JvmStatic
    fun  StringViewToIntVm(value: String): Int?=value.toIntOrNull()

    //时间类型转换
    private val formatter = DateTimeFormatter.ofPattern(("yyyy/MM/dd"))
    @JvmStatic
    @InverseMethod("StringToLocalDate")
    fun localDateToString(date:LocalDate?):String{
        return date?.format(formatter)?:""
    }

    @JvmStatic
    fun StringToLocalDate(value :String) : LocalDate?{
        return try {
            LocalDate.parse(value,formatter)
        }catch (e: Exception){
            null //空字符串
        }
    }

}