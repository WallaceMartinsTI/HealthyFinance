package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.healthyfinance.ui.theme.HealthyFinanceTheme
import com.wcsm.healthyfinance.ui.theme.Primary

@Composable
fun AppTitle() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HEALTHY FINANCE",
            fontSize = 32.sp,
            color = Primary,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default

        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "CONTROLE SUAS FINANÇAS",
            color = Color.White

        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "COM FACILIDADE",
            color = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252525)
@Composable
fun AppTitlePreview() {
    HealthyFinanceTheme {
        AppTitle()
    }
}