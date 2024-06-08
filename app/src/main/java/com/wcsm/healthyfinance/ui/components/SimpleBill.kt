package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.ExpensePrimary
import com.wcsm.healthyfinance.ui.theme.HealthyFinanceTheme
import com.wcsm.healthyfinance.ui.theme.Primary

@Composable
fun SimpleBill(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val alpha = if(selected) 1f else 0.3f

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(BackgroundContainer.copy(alpha = alpha))
            .clickable {
                onClick()
            }
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = color,
            modifier = Modifier.alpha(alpha = alpha)
        )
        Text(
            text = "Selecionar",
            color = Color.White,
            modifier = Modifier
                .alpha(alpha = alpha)
                .drawBehind {
                    val strokeWidthPx = 1.dp.toPx()
                    val verticalOffset = size.height - 2.sp.toPx()
                    drawLine(
                        color = Color.White,
                        strokeWidth = strokeWidthPx,
                        start = Offset(0f, verticalOffset),
                        end = Offset(size.width, verticalOffset)
                    )
                },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleBillPreview() {
    HealthyFinanceTheme {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundColor)
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SimpleBill(
                title = "Receita",
                color = ExpensePrimary,
                selected = false,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                onClick = {}
            )

            Spacer(modifier = Modifier.width(16.dp))

            SimpleBill(
                title = "Gastos",
                color = Primary,
                selected = true,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                onClick = {}
            )
        }
    }
}