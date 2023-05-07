package com.never.simplebtscanner.utils.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldComponent(
    title: String?,
    onSearch: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit)
) {
    Scaffold(
        topBar = {
            if (title != null) {
                AppTopBar.Primary(
                    title = title,
                    onBack = onBack,
                    onSearch = onSearch
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}
