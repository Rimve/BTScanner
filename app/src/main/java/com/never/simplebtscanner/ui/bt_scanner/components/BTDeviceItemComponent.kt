package com.never.simplebtscanner.ui.bt_scanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.never.simplebtscanner.R
import com.never.simplebtscanner.ui.bt_scanner.utils.domain.BTDeviceDomain

@Composable
fun BTDeviceItemComponent(btDeviceDomain: BTDeviceDomain) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Bluetooth device icon",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = btDeviceDomain.name.orEmpty(),
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = btDeviceDomain.macAddress,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Preview
@Composable
private fun BTDeviceItemComponentPreview() {
    BTDeviceItemComponent(
        BTDeviceDomain("", "00:00:00:00:00:00")
    )
}
