package com.github.daniellfalcao.authentication.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.github.daniellfalcao.common_ui.ui.binding.withBinding
import com.github.daniellfalcao.common_ui.ui.fragment.ParrotFragment
import com.github.daniellfalcao.darkblueparrot.authentication.databinding.FragmentSignInBinding

class SignInFragment : ParrotFragment<FragmentSignInBinding>() {

    private val args: SignInFragmentArgs by navArgs()

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = binding ?: FragmentSignInBinding.inflate(inflater, container, false).apply {
            toolbar.apply {
                setupWithNavController(findNavController())
            }
            usernameField.apply {
                editText?.setText(args.username)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setAutofillHints(View.AUTOFILL_HINT_USERNAME)
                }
            }
            passwordField.apply {
                editText?.setText(args.password)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
                }
            }
            signInButton.apply {
                setOnClickListener {
                    // call view model
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        withBinding {
            outState.apply {
                putString(STATE_USERNAME_FIELD, usernameField.editText?.text?.toString().orEmpty())
                putString(STATE_PASSWORD_FIELD, passwordField.editText?.text?.toString().orEmpty())
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) return
        withBinding {
            usernameField.editText?.setText(savedInstanceState.getString(STATE_USERNAME_FIELD, ""))
            passwordField.editText?.setText(savedInstanceState.getString(STATE_PASSWORD_FIELD, ""))
        }
    }

    companion object {
        const val STATE_USERNAME_FIELD = "STATE_USERNAME_INPUT"
        const val STATE_PASSWORD_FIELD = "STATE_PASSWORD_INPUT"
    }

}