package com.example.cultivate.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingFragment<SB:ViewBinding>:Fragment() {
    private var _binding: SB? = null
    protected val binding get() = _binding!!

    abstract  fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?):SB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 避免内存泄漏
    }
}