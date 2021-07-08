package com.github.daniellfalcao.authentication.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.daniellfalcao.common_ui.ui.fragment.ParrotFragment
import com.github.daniellfalcao.darkblueparrot.authentication.databinding.FragmentSignUpBinding

class SignUpFragment : ParrotFragment<FragmentSignUpBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
    }

}