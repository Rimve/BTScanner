package com.never.simplebtscanner.ui.bt_scanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.never.simplebtscanner.R
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain

@Composable
fun BTDeviceItemComponent(
    btDeviceDomain: BTDeviceDomain,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBTDeviceClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onBTDeviceClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "Bluetooth device icon",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(60.dp)
                    .padding(8.dp)
            ) {
                if (btDeviceDomain.name != null) {
                    Text(
                        text = btDeviceDomain.name,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = btDeviceDomain.macAddress,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onSaveClick() }) {
                Icon(
                    painter = getSaveIconByState(btDeviceDomain.isSaved),
                    tint = getIconColorByState(btDeviceDomain.isSaved),
                    contentDescription = "Save device icon",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun getSaveIconByState(isSaved: Boolean) = if (isSaved) {
    painterResource(id = R.drawable.ic_favorite_filled)
} else {
    painterResource(id = R.drawable.ic_favorite_empty)
}

@Composable
private fun getIconColorByState(isSaved: Boolean) = if (isSaved) {
    MaterialTheme.colorScheme.primary
} else {
    MaterialTheme.colorScheme.secondary
}

@Preview
@Composable
private fun BTDeviceItemComponentPreview() {
    BTDeviceItemComponent(
        btDeviceDomain = BTDeviceDomain(
            name = "",
            macAddress = "00:00:00:00:00:00",
            isSaved = false
        ),
        onSaveClick = {}
    )
}
