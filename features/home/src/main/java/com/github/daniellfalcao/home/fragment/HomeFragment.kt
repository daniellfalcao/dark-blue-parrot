package com.github.daniellfalcao.home.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.daniellfalcao.common_ui.ui.fragment.ParrotFragment
import com.github.daniellfalcao.darkblueparrot.home.databinding.FragmentHomeBinding

class HomeFragment : ParrotFragment<FragmentHomeBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = binding ?: FragmentHomeBinding.inflate(inflater, container, false)
    }

}