package com.github.daniellfalcao.authentication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.daniellfalcao.authentication.viewmodel.SignUpViewModel
import com.github.daniellfalcao.authentication.viewmodel.state.FieldState
import com.github.daniellfalcao.authentication.viewmodel.state.SignUpState
import com.github.daniellfalcao.authentication.viewmodel.state.UsernameFieldState
import com.github.daniellfalcao.common_ui.extension.compatColorStateList
import com.github.daniellfalcao.common_ui.extension.compatDrawable
import com.github.daniellfalcao.common_ui.extension.observe
import com.github.daniellfalcao.common_ui.extension.toUnit
import com.github.daniellfalcao.common_ui.ui.binding.withBinding
import com.github.daniellfalcao.common_ui.ui.fragment.ParrotFragment
import com.github.daniellfalcao.darkblueparrot.authentication.R
import com.github.daniellfalcao.darkblueparrot.authentication.databinding.FragmentSignUpBinding
import com.github.daniellfalcao.domain._module.exception.ServiceUnavailableException
import com.github.daniellfalcao.domain._module.exception.UnexpectedException
import com.github.daniellfalcao.domain.user.exception.InvalidUsernameException
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.domain.user.model.UserDTO.Parrot
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date
import java.util.concurrent.TimeUnit


class SignUpFragment : ParrotFragment<FragmentSignUpBinding>() {

    private val viewModel by viewModel<SignUpViewModel>()

    private var usernameObserverJob: Job? = null
    private var passwordObserverJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.usernameState.observe(this@SignUpFragment) {
                handleUsernameState(it)
            }
            viewModel.passwordState.observe(this@SignUpFragment) {
                handlePasswordState(it)
            }
            viewModel.screenState.observe(this@SignUpFragment) {
                handleScreenState(it)
            }
        }
    }

    override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = binding ?: FragmentSignUpBinding.inflate(inflater, container, false).apply {
            toolbar.apply {
                setupWithNavController(findNavController())
            }
            usernameField.apply {
                editText?.doOnTextChanged { text, _, _, _ ->
                    viewModel.resetUsernameState()
                    usernameObserverJob?.cancel()
                    usernameObserverJob = lifecycleScope.launch {
                        if (text?.toString().orEmpty().isNotBlank()) {
                            delay(TimeUnit.SECONDS.toMillis(2))
                            viewModel.validateUsername(text?.toString().orEmpty())
                        }
                    }
                }
            }
            passwordField.apply {
                editText?.doOnTextChanged { text, _, _, _ ->
                    viewModel.resetPasswordState()
                    passwordObserverJob?.cancel()
                    passwordObserverJob = lifecycleScope.launch {
                        if (text?.toString().orEmpty().isNotBlank()) {
                            delay(TimeUnit.SECONDS.toMillis(1))
                            viewModel.validatePassword(text?.toString().orEmpty())
                        }
                    }
                }
            }
            birthdayField.apply {
                editText?.setOnClickListener {
                    val picker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(getString(R.string.birthday_date_picker_title))
                        .build()
                    picker.show(childFragmentManager, "")
                    picker.addOnPositiveButtonClickListener { selection ->
                        val date = Date(selection.plus(TimeUnit.HOURS.toMillis(12)))
                            .let { UserDTO.birthdayDateFormat.format(it) }
                        editText?.setText(date)
                    }
                }
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

    private fun handleUsernameState(state: UsernameFieldState) = withBinding {

        fun TextInputLayout.setupAsInitialState() {
            error = null
            isErrorEnabled = false
            isEndIconVisible = false
            context.compatColorStateList(
                com.github.daniellfalcao.darkblueparrot.R.color.input_outlined_stoke_color
            )?.let {
                setBoxStrokeColorStateList(it)
            }
            hintTextColor = context.compatColorStateList(
                com.github.daniellfalcao.darkblueparrot.R.color.primary
            )
        }

        fun TextInputLayout.setupAsErrorState(message: String) {
            isErrorEnabled = true
            error = message
            errorIconDrawable = context.compatDrawable(R.drawable.ic_close)
        }

        fun TextInputLayout.setupAsSuccessState() {
            isErrorEnabled = false
            error = null
            endIconDrawable = context.compatDrawable(R.drawable.ic_check)
            setEndIconTintList(
                context.compatColorStateList(com.github.daniellfalcao.darkblueparrot.R.color.green)
            )
            isEndIconVisible = true
            hintTextColor = context.compatColorStateList(
                com.github.daniellfalcao.darkblueparrot.R.color.green
            )
            context.compatColorStateList(
                R.color.input_outlined_green_color
            )?.let {
                setBoxStrokeColorStateList(it)
            }
        }

        when (state) {
            UsernameFieldState.Available -> {
                usernameProgress.isVisible = false
                usernameField.setupAsSuccessState()
            }
            is UsernameFieldState.Error -> {
                usernameProgress.isVisible = false
                when (state.exception) {
                    is InvalidUsernameException -> {
                        usernameField.setupAsErrorState(getString(R.string.username_input_error))
                    }
                    is ServiceUnavailableException,
                    is UnexpectedException -> {
                        usernameField.setupAsErrorState(state.exception.message)
                    }
                }
            }
            UsernameFieldState.Initial -> {
                usernameProgress.isVisible = false
                usernameField.setupAsInitialState()
            }
            UsernameFieldState.Loading -> {
                usernameProgress.isVisible = true
                usernameField.setupAsInitialState()
            }
            UsernameFieldState.Unavailable -> {
                usernameProgress.isVisible = false
                usernameField.setupAsErrorState(getString(R.string.username_unavailable))
            }
        }
    }.toUnit()

    private fun handlePasswordState(state: FieldState) = withBinding {
        when (state) {
            is FieldState.Error -> {
                passwordField.apply {
                    error = getString(R.string.password_input_error)
                    isErrorEnabled = true
                }
            }
            FieldState.Initial -> {
                passwordField.apply {
                    error = null
                    isErrorEnabled = false
                }
            }
        }
    }.toUnit()

    private fun handleScreenState(state: SignUpState) = withBinding {
        when (state) {
            is SignUpState.Error -> {

            }
            SignUpState.Initial -> {

            }
            SignUpState.Loading -> {

            }
            SignUpState.Success -> {

            }
        }
    }.toUnit()

    companion object {
        const val STATE_PARROT_SELECTOR = "STATE_PARROT_SELECTOR"
        const val STATE_USERNAME_FIELD = "STATE_USERNAME_INPUT"
        const val STATE_PASSWORD_FIELD = "STATE_PASSWORD_INPUT"
        const val STATE_BIRTHDAY_FIELD = "STATE_BIRTHDAY_FIELD"
    }

}