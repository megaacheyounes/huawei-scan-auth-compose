package com.megaache.composeScanAuth.ui.login.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.megaache.composeScanAuth.R
import com.megaache.composeScanAuth.ui.login.LoginViewModel


@Preview
@ExperimentalAnimationApi
@Composable
fun LoginUI(
    modifier: Modifier = Modifier,
    loginState: LoginViewModel.LoginState = LoginViewModel.LoginState(error = "login failed "),
    usernameState: LoginViewModel.TextFieldState = LoginViewModel.TextFieldState(error = "invalid username"),
    passwordState: LoginViewModel.TextFieldState = LoginViewModel.TextFieldState(error = "invalid pw"),
    onUsernameChange: (username: String) -> Unit = {},
    onPasswordChange: (password: String) -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onHuaweiSignInClicked: () -> Unit = {}
) {

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {

        //draw the main card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = " Login to continue",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(16.dp))

                //draw the username field and an error text that hidden initially
                Column(modifier = Modifier.fillMaxWidth()) {

                    TextField(
                        value = usernameState.value,
                        onValueChange = onUsernameChange,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "username"
                            )
                        }
                    )

                    AnimatedVisibility(
                        visible = !usernameState.isValid,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = usernameState.error,
                            color = Color.Red,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                //draw the password field and an error text that hidden initially
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = passwordState.value,
                        singleLine = true,
                        onValueChange = onPasswordChange,
                        placeholder = {
                            Text(
                                text = "password"
                            )
                        }
                    )

                    AnimatedVisibility(
                        visible = !passwordState.isValid,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = passwordState.error,
                            color = Color.Red
                        )
                    }
                }


                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = onLoginClicked,
                    modifier = Modifier.fillMaxWidth(),

                    ) {
                    Text(
                        text = "Login"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                //loading spinner
                AnimatedVisibility(
                    visible = loginState.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp)
                ) {
                    CircularProgressIndicator()
                }

                //login error
                AnimatedVisibility(
                    visible = loginState.isFailed,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = loginState.error,
                        color = Color.Red
                    )
                }


                //draw a divider with text in the middle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp, 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Divider(
                        modifier = Modifier.weight(1f),
                        thickness = 2.dp
                    )
                    Text(
                        text = "Or sign in with",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(8.dp, 0.dp)
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        thickness = 2.dp
                    )
                }


                //huawei login button
                FloatingActionButton(
                    onClick = onHuaweiSignInClicked,
                    backgroundColor = colorResource(id = R.color.hwid_auth_button_color_red)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hwid_auth_button_white),
                        contentDescription = "huawei logo"
                    )
                }

            }

        }//card

    }
}