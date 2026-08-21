package org.mindtec.uikit.ui.components.select

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.R
import org.mindtec.uikit.utils.Fonts

@Composable
fun SelectDialogMF(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    enabled: Boolean = true,
    selectedOption: String?,
    onClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style =
                Fonts.textRegular(16.sp)
            )

            description?.let {
                Text(
                    text = it,
                    style = Fonts.textRegular(16.sp, grayPlaceholder)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (enabled) {
                        onClick?.invoke()
                    }
                },
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(width = 1.dp, color = grayTextField)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = selectedOption ?: "",
                    style = Fonts.textLight(18.sp)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(24.dp)
                        .width(1.dp)
                        .background(grayTextField)
                )
                Icon(
                    painter = painterResource(id = R.drawable.uikit_ic_arrow_down),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectDialogMFPreview() {
    SelectDialogMF(
        modifier = Modifier.padding(16.dp),
        title = "Select Item",
        selectedOption = "F001",
        onClick = {}
    )
}
