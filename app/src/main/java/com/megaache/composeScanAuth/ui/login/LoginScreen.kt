package com.megaache.composeScanAuth.ui.login

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.api.entity.common.CommonConstant
import com.megaache.composeScanAuth.core.registerForActivityResult
import com.megaache.composeScanAuth.ui.Screen
import com.megaache.composeScanAuth.ui.login.components.LoginUI

/**
 * automatically sign the user in if they have already authorized our app
 */
const val HUAWEI_AUTO_SIGN_IN = false

@ExperimentalAnimationApi
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    val loginState = viewModel.loginState.collectAsState().value
    val usernameState = viewModel.usernameState.collectAsState().value
    val passwordState = viewModel.passwordState.collectAsState().value

    //check if we have successful login using email/password or Huawei account kit
    if (loginState.isSuccess) {
        //showing the toast or navigating in a composable can lead to strange behaviour
        // that's why i have to run it inside LaunchedEffect
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Welcome ${loginState.username}", Toast.LENGTH_SHORT).show()

            //navigate to scanner screen and prevent user from going back to login screen
            navController.navigate(
                Screen.ScannerScreen.routeName,
                NavOptions.Builder()
                    .setPopUpTo(Screen.LoginScreen.routeName, true)
                    .build()
            )
        }
        return
    }


    //initialize huawei account kit
    val huaweiSignInIntent = remember {
        val mAuthParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .createParams()

        val mAuthService = AccountAuthManager.getService(context, mAuthParam)

        if (HUAWEI_AUTO_SIGN_IN)
            mAuthService.silentSignIn().addOnCompleteListener {
                if (it.isSuccessful) {
                    viewModel.onEvent(LoginUIEvent.OnHuaweiSignInResult(authAccount = it.result))
                }
            }
        mAuthService!!.signInIntent.apply {
            putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true)
        }
    }

    //create the activity for result launcher, will be invoked later with the [HuaweiSignInIntent]
    //when the user click the huawei sign in button
    val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.onEvent(LoginUIEvent.OnHuaweiSignInResult(intent = it.data))
        }

    //render the ui
    LoginUI(
        loginState = loginState,
        usernameState = usernameState,
        passwordState = passwordState,
        onUsernameChange = {
            viewModel.onEvent(LoginUIEvent.OnUsernameChange(it))
        },
        onPasswordChange = {
            viewModel.onEvent(LoginUIEvent.OnPasswordChanged(it))
        },
        onLoginClicked = {
            viewModel.onEvent(LoginUIEvent.OnLogin)
        },
        onHuaweiSignInClicked = {
            activityResultLauncher.launch(huaweiSignInIntent)
        }
    )


}
