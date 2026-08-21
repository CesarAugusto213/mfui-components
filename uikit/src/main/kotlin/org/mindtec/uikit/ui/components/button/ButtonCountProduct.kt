package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import org.mindtec.uikit.utils.Fonts

@Composable
fun ButtonCountProduct(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String = stringResource(R.string.uikit_items_label),
    quantity: Int = 0,
    total: String = "S/ 0",
    icon: Painter = painterResource(id = R.drawable.uikit_ic_arrow_right),
    containerColor: Color = UiKitTheme.colors.primary,
    contentColor: Color = white,
    borderColor: Color = UiKitTheme.colors.primary,
    boxColor: Color = UiKitTheme.colors.primaryPressed,
    showIcon: Boolean = true,
    showPrice: Boolean = true
) {
    val isClickable = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            if (isClickable.value) {
                isClickable.value = false
                onClick()
                coroutineScope.launch {
                    kotlinx.coroutines.delay(500)
                    isClickable.value = true
                }
            }
        },
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(4.dp),
        enabled = isClickable.value
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .widthIn(min = 30.dp)
                    .background(boxColor, shape = RoundedCornerShape(8.dp))
                    .padding(6.dp, 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = quantity.toString(),
                    style = Fonts.textRegular(16.sp, contentColor, TextAlign.Center)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                modifier = Modifier.weight(1f),
                text = description,
                style = Fonts.textRegular(16.sp, contentColor)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showPrice) {
                    Text(
                        text = total,
                        style = Fonts.textRegular(16.sp, contentColor)
                    )
                }
                if (showIcon) {
                    Image(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(contentColor)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCountButton() {
    ButtonCountProduct(
        onClick = {},
        quantity = 10,
        total = "S/ 50",
        containerColor = black,
        contentColor = white,
        borderColor = black,
        boxColor = gray,
        showIcon = false
    )
}
