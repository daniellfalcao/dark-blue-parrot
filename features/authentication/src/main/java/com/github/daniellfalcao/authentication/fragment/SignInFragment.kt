package com.github.daniellfalcao.authentication.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.daniellfalcao.common_ui.ui.fragment.ParrotFragment
import com.github.daniellfalcao.darkblueparrot.authentication.databinding.FragmentSignInBinding

class SignInFragment : ParrotFragment<FragmentSignInBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentSignInBinding.inflate(inflater, container, container != null)
    }

}