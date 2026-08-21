package org.mindtec.uikit.ui.components.dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.components.text_field.TextFieldMF
import org.mindtec.uikit.ui.theme.darkButton
import org.mindtec.uikit.ui.theme.neutral700
import org.mindtec.uikit.ui.theme.white

/**
 * Diálogo para pedir un número de teléfono con validación de longitud fija
 * (ej. para compartir por WhatsApp). Reemplaza a `WhatsappDialog` — el
 * original tenía la validación de 9 dígitos y el mensaje de error
 * hardcodeados; acá [phoneLength] es un parámetro (9 sigue siendo el
 * default, como en Perú).
 */
@Composable
fun PhoneNumberDialog(
    title: String,
    fieldLabel: String = stringResource(R.string.uikit_phone_field_label),
    phoneLength: Int = 9,
    invalidPhoneMessage: String = stringResource(R.string.uikit_invalid_phone_message, phoneLength),
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    val context = LocalContext.current.applicationContext
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
                    .padding(20.dp),
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = neutral700,
                    )
                )

                Spacer(modifier = Modifier.size(12.dp))

                TextFieldMF(
                    title = fieldLabel,
                    isNumericKeyboard = true,
                    value = text,
                    onChangeValue = { text = it },
                    titleFontSize = 13.sp
                )
                Spacer(modifier = Modifier.size(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .height(50.dp)
                            .weight(1f),
                        text = stringResource(R.string.uikit_cancel),
                        textSize = 18.sp,
                        border = darkButton,
                        textColor = darkButton,
                        onClick = { onDismissRequest.invoke() }
                    )

                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .height(50.dp)
                            .weight(1f),
                        text = stringResource(R.string.uikit_confirm),
                        textColor = white,
                        textSize = 18.sp,
                        border = darkButton,
                        background = darkButton,
                        onClick = {
                            if (text.isNotEmpty() && text.length == phoneLength && text.all { it.isDigit() }) {
                                onConfirmation.invoke(text)
                                onDismissRequest.invoke()
                            } else {
                                Toast.makeText(context, invalidPhoneMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneNumberDialogPreview() {
    PhoneNumberDialog(
        title = "Enviar por WhatsApp",
        onDismissRequest = {},
        onConfirmation = {}
    )
}
