package com.megaache.composeScanAuth.ui.scanner

import com.huawei.hms.ml.scan.HmsScan

/**
 *  events that are sent by ScannerScreen to the ViewModel
 */
sealed class ScannerUIEvent {

    /**
     * sent to ViewModel when the UI receives the ScanResult from the Scan kit RemoteView
     */
    class OnScanResult(val result: Array<HmsScan>) : ScannerUIEvent()

    /**
     * copy the current QR originalValue to the clipboard, when the user click the copy button
     */
    object OnCopy : ScannerUIEvent()
}