package com.github.daniellfalcao.authentication.fragment

import android.graphics.drawable.AnimationDrawable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.color
import androidx.navigation.fragment.findNavController
import com.github.daniellfalcao.common_ui.extension.compatColor
import com.github.daniellfalcao.common_ui.extension.compatDrawable
import com.github.daniellfalcao.common_ui.ui.fragment.ParrotFragment
import com.github.daniellfalcao.darkblueparrot.authentication.R
import com.github.daniellfalcao.darkblueparrot.authentication.databinding.FragmentPreSignBinding

class PreSignFragment : ParrotFragment<FragmentPreSignBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = binding ?: FragmentPreSignBinding.inflate(inflater, container, false).apply {
            logo.apply {
                val drawable = context.compatDrawable(
                    com.github.daniellfalcao.common_ui.R.drawable.img_parrot
                ) as? AnimationDrawable?
                setImageDrawable(drawable)
                drawable?.start()
            }
            signInButton.apply {
                setOnClickListener {
                    findNavController().navigate(com.github.daniellfalcao.darkblueparrot.R.id.action_preSignFragment_to_signInFragment)
                }
            }
            signUpButton.apply {
                setOnClickListener {
                    findNavController().navigate(com.github.daniellfalcao.darkblueparrot.R.id.action_preSignFragment_to_signUpFragment)
                }
                text = SpannableStringBuilder().apply {
                    append(getString(R.string.no_account))
                    color(requireContext().compatColor(com.github.daniellfalcao.darkblueparrot.R.color.primary)) {
                        append(" " + getString(R.string.sign_up))
                    }
                }
            }
        }
    }

}