package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun SmallOutlinedButtonMF(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    text: String,
    textSize: TextUnit = 14.sp,
    background: Color? = null,
    border: Color = neutral400,
    textColor: Color = grayPlaceholder,
    onClick: () -> Unit
) {
    val colors = if (background != null) {
        ButtonDefaults.outlinedButtonColors(containerColor = background)
    } else {
        ButtonDefaults.outlinedButtonColors()
    }

    OutlinedButton(
        enabled = enabled,
        shape = MaterialTheme.shapes.extraSmall,
        onClick = { onClick.invoke() },
        modifier = modifier,
        colors = colors,
        border = BorderStroke(
            width = 1.dp,
            color = border
        )
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            icon?.let {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = it,
                    contentDescription = null,
                    tint = textColor
                )
            }

            Text(
                text = text,
                style = Fonts.textRegular(textSize, textColor),
            )
        }
    }
}

@Composable
fun SmallOutlinedButtonCustom(
    text: String,
    onClick: () -> Unit,
    selectedButton: String,
    lightBlueButton: Color,
    isSelected: Color,
    isNotSelected: Color,
    textNotSelected: Color
) {
    SmallOutlinedButtonMF(
        text = text,
        onClick = onClick,
        background = if (selectedButton == text) lightBlueButton else null,
        border = if (selectedButton == text) isSelected else isNotSelected,
        textColor = if (selectedButton == text) isSelected else textNotSelected
    )
}

@Preview(showBackground = true)
@Composable
private fun SmallOutlinedButtonMFPreview() {
    SmallOutlinedButtonMF(
        text = "Ver todos",
        onClick = {},
        background = skyBlue100,
        border = blue300,
        textColor = blue300
    )
}
