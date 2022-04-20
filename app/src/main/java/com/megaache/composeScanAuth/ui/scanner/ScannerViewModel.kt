package com.megaache.composeScanAuth.ui.scanner

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(private val application: Application) : ViewModel() {

    private val _result = MutableStateFlow("No QR detected!")
    val result: StateFlow<String> = _result

    fun onEvent(eventScanner: ScannerUIEvent) = viewModelScope.launch {
        when (eventScanner) {
            is ScannerUIEvent.OnScanResult -> {
                Log.d("scanner","onScanResult")
                if (eventScanner.result.isNotEmpty() && eventScanner.result[0].showResult.isNotBlank()) {
                    _result.value = eventScanner.result[0].originalValue
                    Log.d("scanner","onScanResult:  ${_result.value}")
                }
            }
            is ScannerUIEvent.OnCopy -> {
                val clipboard =  application . getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("QR code", _result.value)
                clipboard.setPrimaryClip(clip)
            }
        }
    }

}