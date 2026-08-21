package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.launch
import org.mindtec.uikit.utils.Fonts

@Composable
fun ButtonMF(
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    text: String,
    color: Color = neutral900,
    textColor: Color = white,
    iconColor: Color = white,
    fontSize: TextUnit = 16.sp,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isClickable by remember { mutableStateOf(true) }

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.extraSmall,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = white,
            disabledContentColor = white,
            disabledContainerColor = neutral400
        ),
        enabled = enabled && isClickable,
        onClick = {
            if (isClickable) {
                isClickable = false
                onClick.invoke()

                coroutineScope.launch {
                    delay(500)
                    isClickable = true
                }
            }
        }
    ) {
        icon?.let {
            Icon(
                painter = it,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = iconColor
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = Fonts.textRegular(fontSize, textColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonWithoutIconMFPreview() {
    ButtonMF(
        modifier = Modifier.padding(16.dp),
        text = "Ingresar"
    ) {
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonWithIconMFPreview() {
    ButtonMF(
        modifier = Modifier.padding(16.dp),
        text = "Agregar producto",
        color = purple300,
        icon = painterResource(id = R.drawable.uikit_ic_arrow_down)
    ) {
    }
}
