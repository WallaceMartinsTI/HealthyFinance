package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.healthyfinance.R
import com.wcsm.healthyfinance.ui.theme.Primary

@Composable
fun MyTopAppBar(
    returnIcon: Boolean = false,
    onClick: () -> Unit = {}
) {
    val painterIcon = painterResource(id = R.drawable.app_icon_24)
    val imageVectorIcon = Icons.Default.KeyboardArrowLeft

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(returnIcon) {
            Icon(
                imageVector = imageVectorIcon,
                contentDescription = "Screen Icon",
                tint = Primary,
                modifier = Modifier
                    .size(70.dp)
                    .padding(8.dp)
                    .clickable {
                        onClick()
                    }
            )
        } else {
            Icon(
                painter = painterIcon,
                contentDescription = "Screen Icon",
                tint = Primary,
                modifier = Modifier
                    .size(70.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252525)
@Composable
fun MyTopAppBarArrowLeftPreview() {
    MyTopAppBar()
}

@Preview(showBackground = true, backgroundColor = 0xFF252525)
@Composable
fun MyTopAppBarLogoPreview() {
    MyTopAppBar(returnIcon = true)
}