package com.github.daniellfalcao.authentication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.daniellfalcao.common_ui.model.Parrot
import com.github.daniellfalcao.common_ui.ui.binding.withBinding
import com.github.daniellfalcao.common_ui.ui.fragment.ParrotFragment
import com.github.daniellfalcao.darkblueparrot.authentication.databinding.FragmentSignUpBinding

class SignUpFragment : ParrotFragment<FragmentSignUpBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = binding ?: FragmentSignUpBinding.inflate(inflater, container, false).apply {
            toolbar.apply {
                setupWithNavController(findNavController())
            }
            doneButton.apply {
                setOnClickListener {
                    // call view model
                    SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(
                        username = usernameField.editText?.text?.toString().orEmpty(),
                        password = passwordField.editText?.text?.toString().orEmpty()
                    ).also { action ->
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        withBinding {
            outState.apply {
                putSerializable(STATE_PARROT_SELECTOR, parrotSelector.selectedParrot)
                putString(STATE_USERNAME_FIELD, usernameField.editText?.text?.toString().orEmpty())
                putString(STATE_PASSWORD_FIELD, passwordField.editText?.text?.toString().orEmpty())
                putString(STATE_BIRTHDAY_FIELD, passwordField.editText?.text?.toString().orEmpty())
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) return
        withBinding {
            (savedInstanceState.getSerializable(STATE_PARROT_SELECTOR) as? Parrot?)?.let {
                parrotSelector.selectParrot(it)
            }
            usernameField.editText?.setText(savedInstanceState.getString(STATE_USERNAME_FIELD, ""))
            passwordField.editText?.setText(savedInstanceState.getString(STATE_PASSWORD_FIELD, ""))
            birthdayField.editText?.setText(savedInstanceState.getString(STATE_BIRTHDAY_FIELD, ""))
        }
    }

    companion object {
        const val STATE_PARROT_SELECTOR = "STATE_PARROT_SELECTOR"
        const val STATE_USERNAME_FIELD = "STATE_USERNAME_INPUT"
        const val STATE_PASSWORD_FIELD = "STATE_PASSWORD_INPUT"
        const val STATE_BIRTHDAY_FIELD = "STATE_BIRTHDAY_FIELD"
    }

}