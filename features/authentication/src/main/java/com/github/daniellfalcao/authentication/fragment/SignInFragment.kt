package com.github.daniellfalcao.authentication.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.daniellfalcao.common_ui.ui.binding.withBinding
import com.github.daniellfalcao.common_ui.ui.fragment.ParrotFragment
import com.github.daniellfalcao.darkblueparrot.authentication.databinding.FragmentSignInBinding

class SignInFragment : ParrotFragment<FragmentSignInBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            val navController = findNavController()
            toolbar.apply {
                setupWithNavController(navController)
            }
            usernameField.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setAutofillHints(View.AUTOFILL_HINT_USERNAME)
                }
            }
            passwordField.apply {
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
        val username = binding?.usernameField?.editText?.text?.toString().orEmpty()
        val password = binding?.passwordField?.editText?.text?.toString().orEmpty()
        outState.apply {
            putString(STATE_USERNAME_FIELD, username)
            putString(STATE_PASSWORD_FIELD, password)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) return
        val username = savedInstanceState.getString(STATE_USERNAME_FIELD, "")
        val password = savedInstanceState.getString(STATE_PASSWORD_FIELD, "")
        withBinding {
            usernameField.editText?.setText(username)
            passwordField.editText?.setText(password)
        }
    }

    companion object {
        const val STATE_USERNAME_FIELD = "STATE_USERNAME_INPUT"
        const val STATE_PASSWORD_FIELD = "STATE_PASSWORD_INPUT"
    }

}