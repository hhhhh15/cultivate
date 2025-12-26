package com.example.cultivate.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseDataBindingFragment<VB : ViewDataBinding>  :Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract  fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?):VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}