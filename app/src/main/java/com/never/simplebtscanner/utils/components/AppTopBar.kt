package com.never.simplebtscanner.utils.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.never.simplebtscanner.R

object AppTopBar {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Primary(
        title: String,
        onSearch: (() -> Unit)? = null,
        onBack: (() -> Unit)? = null
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),
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
                        colors = IconButtonDefaults.iconButtonColors(),
                        onClick = { onBack() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = "Back button",
                        )
                    }
                }
            },
            actions = {
                if (onSearch != null) {
                    IconButton(
                        onClick = { onSearch() },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search button",
                        )
                    }
                }
            }
        )
    }
}
