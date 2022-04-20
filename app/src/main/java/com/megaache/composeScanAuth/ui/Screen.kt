package com.megaache.composeScanAuth.ui

sealed class Screen(val routeName: String) {
    object LoginScreen : Screen("login")
    object ScannerScreen : Screen("scanner")
}