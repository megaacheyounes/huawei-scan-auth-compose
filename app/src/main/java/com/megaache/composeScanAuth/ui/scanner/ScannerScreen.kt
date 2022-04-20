package com.megaache.composeScanAuth.ui.scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.megaache.composeScanAuth.ui.scanner.components.ScannerUI

@Composable
fun ScannerScreen(
    navController: NavController,
    viewModel: ScannerViewModel = hiltViewModel()
) {

    //this is the result of the scan, contains originalValue of a QR code
    //changes everytime you scan a new QR code
    val result = viewModel.result.collectAsState().value

    ScannerUI(
        modifier = Modifier,
        result = result,
        onCopy = {
            viewModel.onEvent(ScannerUIEvent.OnCopy)
        },
        onScanResult = {
            viewModel.onEvent(ScannerUIEvent.OnScanResult(it))
        }
    )

}


