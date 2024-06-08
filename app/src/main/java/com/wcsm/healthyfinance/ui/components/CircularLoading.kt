package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wcsm.healthyfinance.ui.theme.Primary

@Composable
fun CircularLoading(modifier: Modifier = Modifier, size: Dp) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size),
            color = Primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircularLoadingPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularLoading(size = 32.dp)
    }
}