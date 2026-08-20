package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun IconButtonMF(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    text: String? = null,
    heightSize: Dp = 56.dp,
    onClick: () -> Unit,
    buttonColor: Color = purple300,
    iconColor: Color = white,
    textColor: Color = white
) {
    Button(
        modifier = modifier
            .height(heightSize)
            .clip(RectangleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = white,
            disabledContainerColor = buttonColor.copy(alpha = 0.5f),
            disabledContentColor = white.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, buttonColor),
        enabled = enabled,
        onClick = onClick
    ) {
        if (icon != null) {
            Image(
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconColor),
            )
        } else if (text != null) {
            Text(
                text = text,
                color = textColor,
                style = Fonts.textBold(16.sp, textColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IconButtonTextPreview() {
    IconButtonMF(
        modifier = Modifier.padding(16.dp),
        text = "Verificar",
        onClick = {}
    )
}
