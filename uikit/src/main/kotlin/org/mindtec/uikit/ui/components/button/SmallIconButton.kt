package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*

@Composable
fun SmallIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter,
    sizeButton: Dp = 55.5.dp,
    background: Color = UiKitTheme.colors.primary,
    iconColor: Color = white,
    stroke: Color = UiKitTheme.colors.primary,
    alignment: Alignment = Alignment.BottomEnd,
    onClick: () -> Unit,
) {
    Box(modifier = modifier, contentAlignment = alignment) {
        Card(
            modifier = Modifier.size(sizeButton),
            colors = CardDefaults.cardColors(
                containerColor = background,
                disabledContainerColor = background.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, stroke),
            shape = RoundedCornerShape(4.dp),
            enabled = enabled,
            onClick = {
                onClick.invoke()
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(sizeButton * 0.5f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SmallIconButtonPreview() {
    SmallIconButton(
        icon = painterResource(id = R.drawable.uikit_ic_arrow_down),
        onClick = { }
    )
}
