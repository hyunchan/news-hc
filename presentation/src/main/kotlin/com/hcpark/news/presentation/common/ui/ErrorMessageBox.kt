package com.hcpark.news.presentation.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorMessageBox(
    modifier: Modifier = Modifier,
    message: String?,
    contentAlignment: Alignment = Alignment.TopCenter
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .padding(horizontal = 16.dp),
        contentAlignment = contentAlignment
    ) {
        Text(text = message ?: "Unknown error")
    }
}