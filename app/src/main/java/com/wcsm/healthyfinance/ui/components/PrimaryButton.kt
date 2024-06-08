package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.HealthyFinanceTheme
import com.wcsm.healthyfinance.ui.theme.Primary

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = BackgroundColor,
    textColor: Color = Primary,
    enabledText: String = "",
    disabledText: String = "",
    border: BorderStroke = BorderStroke(1.dp, Primary),
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        border = border,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor
        )
    ) {
        Text(
            text = if(enabled) enabledText else disabledText,
            color = textColor,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252525)
@Composable
fun PrimaryButtonPreview() {
    HealthyFinanceTheme {
        Column {
            Text(text = "HABILITADO", color = Color.White)
            PrimaryButton(enabledText = "TESTE", onClick = {}, enabled = true)

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "DESABILITADO", color = Color.White)
            PrimaryButton(disabledText = "TESTE", onClick = {}, enabled = false)
        }
    }
}