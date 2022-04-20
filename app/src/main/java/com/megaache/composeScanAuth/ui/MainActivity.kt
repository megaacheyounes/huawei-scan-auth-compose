package com.megaache.composeScanAuth.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.MaterialTheme
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.megaache.composeScanAuth.ui.login.LoginScreen
import com.megaache.composeScanAuth.ui.scanner.ScannerScreen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // the app assumes the user always grant permissions

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.LoginScreen.routeName
                ) {

                    composable(
                        route = Screen.LoginScreen.routeName,
                    ) {
                        LoginScreen(navController = navController)
                    }

                    composable(
                        route = Screen.ScannerScreen.routeName
                    ) {
                        ScannerScreen(
                            navController = navController
                        )
                    }
                }

            }
        }
    }

    // Use the onRequestPermissionsResult function to receive the permission verification result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check whether requestCode is set to the value of CAMERA_REQ_CODE during permission application,
        // and then check whether the permission is enabled.
        if (requestCode == 1 && grantResults.isNotEmpty() &&
            !(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)
        ) {
            Toast.makeText(
                this,
                "This demo is not designed for Monkeys \uD83C\uDF4C ?",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}