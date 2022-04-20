package com.megaache.composeScanAuth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huawei.hms.support.account.AccountAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {


    companion object {
        const val LOGIN_USERNAME = "test"
        const val LOGIN_PASSWORD = "123456"
    }

    data class TextFieldState(
        val value: String = "", //the value of the textField, can be changed by user
        val isValid: Boolean = true, //true if the value is valid
        val error: String = "" //error that will be shown on the UI, if the value is invalid
    )

    data class LoginState(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val username: String = "",
        val isFailed: Boolean = false,
        val error: String = ""
    )

    private val _usernameState = MutableStateFlow(
        TextFieldState(
            error = "username is not valid"
        )
    )
    val usernameState: StateFlow<TextFieldState> = _usernameState

    private val _passwordState = MutableStateFlow(
        TextFieldState(
            error = "password is not valid"
        )
    )
    val passwordState: StateFlow<TextFieldState> = _passwordState

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    private var password: String = ""
    private var username: String = ""

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.OnLogin -> {
                attemptLogin()
            }
            is LoginUIEvent.OnUsernameChange -> {
                onUsernameChanged(event.username)
            }
            is LoginUIEvent.OnPasswordChanged -> {
                onPasswordChanged(event.password)
            }
            is LoginUIEvent.OnHuaweiSignInResult -> {
                if (event.intent == null && event.authAccount == null) {
                    _loginState.value = LoginState(
                        isFailed = true,
                        error = "Huawei Sign in failed"
                    )
                    return
                }
                var authAccount = event.authAccount
                if (authAccount == null) {
                    val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(event.intent)
                    authAccount = authAccountTask.result
                }
                if (authAccount != null) {
                    _loginState.value = LoginState(
                        isSuccess = true,
                        username = authAccount.displayName
                    )
                }

            }//is LoginUIEvents.OnHuaweiSignInResult
        }
    }

    private fun onUsernameChanged(newUsername: String) {
        username = newUsername
        _usernameState.value = _usernameState.value.copy(
            isValid = isUsernameValid(),
            value = username
        )
    }

    private fun onPasswordChanged(newPassword: String) {
        password = newPassword
        _passwordState.value = _passwordState.value.copy(
            isValid = isPasswordValid(),
            value = password
        )
    }

    private fun isUsernameValid() = username.length in 3..30
    private fun isPasswordValid() = password.length >= 6

    private fun attemptLogin() = viewModelScope.launch {
        //check if user name is valid and inform the UI when its invalid
        if (!isUsernameValid()) {
            return@launch onUsernameChanged(username)
        }

        //check if the password is valid and inform the UI when its invalid
        if (!isPasswordValid()) {
            return@launch onPasswordChanged(password)
        }

        //tell UI to showing loading spinner (circle progress indicator)
        _loginState.value = LoginState(
            isLoading = true,
            isFailed = false
        )
        //simulate network request
        delay(500)
        if (username == LOGIN_USERNAME && password == LOGIN_PASSWORD) {
            _loginState.value = LoginState(
                isSuccess = true,
                username = username
            )
        } else {
            _loginState.value = LoginState(
                isFailed = true,
                error = "wrong username or password"
            )
        }
    }


}