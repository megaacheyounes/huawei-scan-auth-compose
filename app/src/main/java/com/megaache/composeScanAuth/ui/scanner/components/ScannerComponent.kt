package com.megaache.composeScanAuth.ui.scanner.components

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.ml.scan.HmsScan

/**
 * Scan kit RemoteView as a composable component
 * show live feed from the camera
 * [onScanResult] callback that fires when scan kit returns a QR code result
 */
@Composable
fun ScannerView(
    modifier: Modifier = Modifier,
    onScanResult: (result: Array<HmsScan>) -> Unit
) {

    val remoteView = rememberRemoteViewWithLifecycle(
        onScanResult = onScanResult
    )
    AndroidView(
        modifier = modifier,
        factory = { remoteView })
}

/**
 * creates a scan kit remoteView with proper lifecycle support without keeping a reference to an activity
 */
@Composable
fun rememberRemoteViewWithLifecycle(
    onScanResult: (result: Array<HmsScan>) -> Unit
): RemoteView {
    val context = LocalContext.current
    val resources = context.resources
    val activity = LocalContext.current as Activity
    val remoteView = remember {

        // 1. Obtain the screen resolution to calculate the size of the scanning box.
        val dm = resources.displayMetrics
        val density = dm.density
        val frameSizePx = 300

        // 2. Obtain the screen size.
        val mScreenWidth = resources.displayMetrics.widthPixels
        val mScreenHeight = resources.displayMetrics.heightPixels

        val scanFrameSize = (frameSizePx * density)

        // 3. Calculate the position of the scanning box. Currently, it is in the middle of the screen.
        // (Optional) Set the position of the scanning box. If no position is set, the scanning box will be in the middle of the screen by default.
        val rect = Rect()
        rect.left = (mScreenWidth / 2 - scanFrameSize / 2).toInt()
        rect.right = (mScreenWidth / 2 + scanFrameSize / 2).toInt()
        rect.top = (mScreenHeight / 2 - scanFrameSize / 2).toInt()
        rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2).toInt()

        // Initialize RemoteView and set a callback to receive the scanning result.
        val remoteView = RemoteView
            .Builder()
            .setContext(activity)
            .setBoundingBox(rect)
            .setFormat(HmsScan.ALL_SCAN_TYPE)
            .build()

        remoteView!!.apply {
            setOnResultCallback(onScanResult)
            onCreate(Bundle())
            onResume()
        }

        remoteView
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, remoteView) {
        // Make MapView follow the current lifecycle
        val lifecycleObserver = getRemoteViewLifecycleObserver(remoteView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return remoteView
}

/**
 * invoke Scan Kit remoteView lifecycle methods
 */
private fun getRemoteViewLifecycleObserver(remoteView: RemoteView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        remoteView.apply {
            when (event) {
                Lifecycle.Event.ON_CREATE -> onCreate(Bundle())
                Lifecycle.Event.ON_START -> onStart()
                Lifecycle.Event.ON_RESUME -> onResume()
                Lifecycle.Event.ON_PAUSE -> onPause()
                Lifecycle.Event.ON_STOP -> onStop()
                Lifecycle.Event.ON_DESTROY -> onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }