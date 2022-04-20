package com.megaache.composeScanAuth.ui.scanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huawei.hms.ml.scan.HmsScan
import com.megaache.composeScanAuth.ui.scanner.components.ScannerView

@Preview
@Composable
fun ScannerUI(
    modifier: Modifier = Modifier,
    result: String = "Wifi: X421324",
    onCopy: () -> Unit = {},
    onScanResult: (result: Array<HmsScan>) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {

        //full screen component that shows camera stream
        ScannerView(onScanResult = onScanResult)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Scan QR code",
                style = MaterialTheme.typography.body1,
                color = Color.White,
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(15.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { },

                elevation = 10.dp
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Result",
                        style = MaterialTheme.typography.body1,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    //result text is shown here
                    Text(
                        text = result,
                        style = MaterialTheme.typography.body2,
                        color = Color.Black
                    )

                    //spacer to push the copy button to the bottom of the card
                    Spacer(modifier = Modifier.weight(1f))

                    OutlinedButton(
                        onClick = onCopy,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Copy"
                        )
                    }

                }
            }//card

        }
    }//box
}