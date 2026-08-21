package org.mindtec.uikit.ui.components.alerts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

/**
 * @param image ícono grande arriba del texto (antes se llamaba `imageResourceId`).
 * @param buttonIcon ícono opcional dentro del botón de confirmar (antes se llamaba `icon`) —
 * se renombraron al convertirlos de `Int` a `Painter` para que dejen de confundirse entre sí.
 */
@Composable
fun AlertDialog(
    description: String,
    descriptionColor: TextStyle = Fonts.textRegular(18.sp, gray, TextAlign.Center),
    buttonIcon: Painter? = null,
    subDescription: String? = null,
    subDescriptionColor: TextStyle = Fonts.textBold(18.sp, red200, TextAlign.Center),
    image: Painter,
    imageColor: Color = purple300,
    onExtraRequest: () -> Unit = {},
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    showConfirmationButton: Boolean = false,
    buttonExtra: String? = null,
    buttonSaveColor: Color = darkButton,
    buttonSave: String = stringResource(R.string.uikit_save)
) {
    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = white),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 28.dp,
                    bottom = 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(imageColor),
                    modifier = Modifier.size(64.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = description,
                        style = descriptionColor
                    )

                    subDescription?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = it,
                            style = subDescriptionColor
                        )
                    }
                }
                if (showConfirmationButton) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        buttonExtra?.let {
                            SmallOutlinedButtonMF(
                                text = it,
                                textColor = white,
                                textSize = 18.sp,
                                border = darkButton,
                                background = darkButton,
                                onClick = { onExtraRequest.invoke() }
                            )
                        }
                        SmallOutlinedButtonMF(
                            text = buttonSave,
                            icon = buttonIcon,
                            textColor = white,
                            textSize = 18.sp,
                            border = buttonSaveColor,
                            background = buttonSaveColor,
                            onClick = { onConfirmation.invoke() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExitAlertPreview() {
    AlertDialog(
        description = "Se generó la guía correctamente",
        image = painterResource(id = R.drawable.uikit_ic_arrow_down),
        onDismissRequest = {},
        onConfirmation = {},
        showConfirmationButton = false
    )
}

@Preview(showBackground = true)
@Composable
private fun ExitAlertConfirmPreview() {
    AlertDialog(
        description = "¿Deseas continuar?",
        image = painterResource(id = R.drawable.uikit_ic_arrow_down),
        imageColor = purple300,
        onDismissRequest = {},
        onConfirmation = {},
        showConfirmationButton = true,
        buttonSave = "Ok"
    )
}

@Preview(showBackground = true)
@Composable
private fun ExitAlertSuccessPreview() {
    AlertDialog(
        description = "Registro realizado de forma exitosa",
        image = painterResource(id = R.drawable.uikit_ic_arrow_down),
        imageColor = green200,
        onDismissRequest = {},
        onConfirmation = {},
        buttonSave = "Ok",
        showConfirmationButton = true
    )
}
