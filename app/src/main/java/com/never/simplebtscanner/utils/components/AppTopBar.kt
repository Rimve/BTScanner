package com.never.simplebtscanner.utils.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.never.simplebtscanner.R

object AppTopBar {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Primary(title: String, onBack: (() -> Unit)? = null) {
        TopAppBar(
            title = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Left
                    )
                }
            },
            navigationIcon = {
                if (onBack != null) {
                    IconButton(
                        colors = IconButtonDefaults.filledIconButtonColors(),
                        onClick = { onBack() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = "Back button",
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors()
        )
    }
}
