package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.R
import org.mindtec.uikit.utils.Fonts

@Composable
fun OutlinedButtonMF(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: Painter? = null,
    color: Color = neutral900,
    background: Color = white,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.extraSmall,
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) color else neutral900.copy(alpha = 0.5f)
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = background,
            contentColor = color,
            disabledContentColor = neutral900.copy(alpha = 0.5f),
            disabledContainerColor = grayTextField.copy(alpha = 0.5f)
        ),
        enabled = enabled,
        onClick = { onClick.invoke() },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Image(
                    painter = icon,
                    contentDescription = null
                )
            }
            if (text != null) {
                Text(
                    text = text,
                    style = Fonts.textRegular(
                        fontSize = 16.sp,
                        color = if (enabled) color else neutral900,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Composable
fun OutlinedButtonCustom(
    modifier: Modifier,
    text: String,
    selectedButton: String,
    lightBlueButton: Color,
    isSelected: Color,
    textNotSelected: Color,
    onClick: () -> Unit
) {
    OutlinedButtonMF(
        modifier = modifier,
        text = text,
        background = if (selectedButton == text) lightBlueButton else white,
        color = if (selectedButton == text) isSelected else textNotSelected,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun OutlinedButtonMFPreview() {
    OutlinedButtonMF(
        text = "Cancelar",
        icon = painterResource(id = R.drawable.uikit_ic_arrow_down)
    ) {}
}
