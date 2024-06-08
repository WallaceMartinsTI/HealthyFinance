package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.ExpensePrimary
import com.wcsm.healthyfinance.ui.theme.HealthyFinanceTheme
import com.wcsm.healthyfinance.ui.theme.InvestimentPrimary
import com.wcsm.healthyfinance.ui.theme.Primary
import com.wcsm.healthyfinance.ui.util.toBRL

@Composable
fun BillContainer(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    value: Double,
    arrowEndIcon: Boolean? = null,
    showRadioButton: Boolean = false,
    isRadioButtonSelected: Boolean = false,
    onClick: () -> Unit
) {
    val alpha = if(isSelected) 1f else 0.3f
    val color: Color = when(title) {
        "Receita" -> Primary
        "Gastos" -> ExpensePrimary
        "Investimento" -> InvestimentPrimary
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BackgroundContainer)
            .clickable { onClick() }
            .then(modifier),
    ) {
        Text(
            text = title.uppercase(),
            color = color,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(0.65f)
                .alpha(alpha = alpha)
        )

        Text(
            text = value.toBRL(),
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .alpha(alpha = alpha)
        )

        if(arrowEndIcon != null) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Arrow down icon",
                tint = Color.White
            )
        }

        if (showRadioButton) {
            Icon(
                imageVector = if (isRadioButtonSelected) Icons.Filled.RadioButtonChecked
                else Icons.Filled.RadioButtonUnchecked,
                contentDescription = if (isRadioButtonSelected) "Selected" else "Not selected",
                tint = Primary,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252525)
@Composable
fun BillContainerPreview() {
    HealthyFinanceTheme {
        BillContainer(
            modifier = Modifier.padding(8.dp),
            title = "Receita",
            isSelected = true,
            value = 15250.82,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252525)
@Composable
fun BillContainerPreviewWithRadioButton() {
    HealthyFinanceTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                BillContainer(
                    modifier = Modifier.padding(8.dp),
                    title = "Receita",
                    isSelected = false,
                    value = 15250.82,
                    showRadioButton = true,
                    onClick = {}
                )

                BillContainer(
                    modifier = Modifier.padding(8.dp),
                    title = "Gastos",
                    isSelected = true,
                    value = 500.00,
                    showRadioButton = true,
                    isRadioButtonSelected = true,
                    onClick = {}
                )

                BillContainer(
                    modifier = Modifier.padding(8.dp),
                    title = "Investimento",
                    isSelected = false,
                    value = 5291395850.25,
                    showRadioButton = true,
                    onClick = {}
                )
            }
        }
    }
}