package com.hcpark.news.presentation.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hcpark.news.presentation.theme.colorScheme

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: String?,
    color: Color = colorScheme.onSurfaceVariant,
    contentAlignment: Alignment = Alignment.TopCenter,
    decoration: @Composable (defaultMessage: @Composable () -> Unit) -> Unit = { it() }
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .padding(horizontal = 16.dp),
        contentAlignment = contentAlignment
    ) {
        decoration {
            Text(
                text = message ?: "Unknown error",
                color = color
            )
        }
    }
}
