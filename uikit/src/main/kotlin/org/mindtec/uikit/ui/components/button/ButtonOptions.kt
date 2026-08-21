package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.neutral900

@Composable
fun ButtonOptions(
    textCancel: String? = stringResource(R.string.uikit_cancel),
    textSave: String = stringResource(R.string.uikit_save),
    iconCancel: Painter? = null,
    iconSave: Painter? = null,
    enabledFirst: Boolean = true,
    enabledSecond: Boolean = true,
    color: Color = neutral900,
    firstWeight: Float = 2f,
    secondWeight: Float = 3f,
    paddingHorizontal: Dp = 0.dp,
    onClickCancel: () -> Unit,
    onClickSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingHorizontal),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButtonMF(
            icon = iconCancel,
            text = textCancel,
            enabled = enabledFirst,
            modifier = Modifier.weight(firstWeight),
            onClick = {
                onClickCancel.invoke()
            }
        )

        ButtonMF(
            icon = iconSave,
            text = textSave,
            enabled = enabledSecond,
            color = color,
            modifier = Modifier.weight(secondWeight),
            onClick = {
                onClickSave.invoke()
            }
        )
    }
}
