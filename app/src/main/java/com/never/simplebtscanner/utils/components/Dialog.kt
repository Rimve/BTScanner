package com.never.simplebtscanner.utils.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object Dialog {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WithTextField(
        title: String?,
        textFieldValue: String?,
        placeHolder: (@Composable () -> Unit)? = null,
        onValueChange: (String) -> Unit,
        onConfirm: () -> Unit,
        confirmButtonLabel: String,
        onDismiss: () -> Unit,
        dismissButtonLabel: String
    ) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                if (title != null) {
                    Text(text = title)
                }
            },
            text = {
                TextField(
                    value = textFieldValue.orEmpty(),
                    onValueChange = { term ->
                        onValueChange(term)
                    },
                    placeholder = { placeHolder?.invoke() },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text(text = confirmButtonLabel)
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(text = dismissButtonLabel)
                }
            }
        )
    }
}
