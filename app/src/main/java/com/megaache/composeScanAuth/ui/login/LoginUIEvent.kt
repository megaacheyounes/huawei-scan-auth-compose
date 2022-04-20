package com.megaache.composeScanAuth.ui.login

import android.content.Intent
import com.huawei.hms.support.account.result.AuthAccount

/**
 * contains events send by the UI to the view model
 */
sealed class LoginUIEvent {
    /**
     * send by UI everytime the value of the password field is changed
     */
    class OnPasswordChanged(val password: String) : LoginUIEvent()

    /**
     * send by the UI everytime the value of the username field is changed
     */
    class OnUsernameChange(val username: String) : LoginUIEvent()

    /**
     * send by the UI when the login button is clicked
     */
    object OnLogin : LoginUIEvent()

    /**
     * sent when the UI receives the result of calling onActivityResult from Huawei Account kit
     */
    class OnHuaweiSignInResult(val intent: Intent? = null, val authAccount: AuthAccount? = null) :
        LoginUIEvent()

}

