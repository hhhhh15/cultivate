package com.example.cultivate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cultivate.databinding.GrownfragmentBinding
import com.example.cultivate.viewmodel.TopBarViewModel

class grownModule : Fragment() {
    private var _binding: GrownfragmentBinding? = null
    private val binding get() = _binding!!
    private val vm: TopBarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GrownfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.setTopBar("养成模块",null)

        // Live2DView 会自动初始化
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {

        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}