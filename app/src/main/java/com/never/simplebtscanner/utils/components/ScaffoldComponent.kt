package com.never.simplebtscanner.utils.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldComponent(
    title: String? = null,
    onSearch: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    snackbarMessage: String? = null,
    snackbarDismissed: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit)
) {
    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage != null) {
            when (snackbarHostState.showSnackbar(message = snackbarMessage)) {
                SnackbarResult.ActionPerformed -> snackbarDismissed()
                SnackbarResult.Dismissed -> snackbarDismissed()
            }
        }
    }

    Scaffold(
        topBar = {
            if (title != null) {
                AppTopBar.Primary(
                    title = title,
                    onBack = onBack,
                    onSearch = onSearch
                )
            }
        },
        bottomBar = {
            if (bottomBar != null) {
                bottomBar()
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    Snackbar(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(snackbarData.visuals.message)
                        }
                    }
                }
            )
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
