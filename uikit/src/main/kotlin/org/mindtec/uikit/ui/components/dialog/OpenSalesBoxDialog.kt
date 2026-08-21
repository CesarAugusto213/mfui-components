package org.mindtec.uikit.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

private fun String.isValidDecimalInput(): Boolean =
    isEmpty() || matches(Regex("^\\d*\\.?\\d*$"))

/**
 * Diálogo con dos modos: pide un monto inicial (cuando [readOnlyMessage] es
 * `null`) o solo muestra un mensaje de confirmación (cuando no lo es) — el
 * mismo componente cubre "abrir caja" (con monto) y "cerrar caja" (sin
 * monto) del original.
 */
@Composable
fun OpenSalesBoxDialog(
    title: String,
    inputLabel: String,
    currencyLabel: String,
    readOnlyMessage: String? = null,
    buttonClose: String,
    buttonSave: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String?) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf(TextFieldValue("0", TextRange(0, "0".length))) }
    val focusRequester = remember { FocusRequester() }

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = Fonts.textBold(14.sp, neutral700, TextAlign.Center)
                )

                if (readOnlyMessage == null) {
                    LaunchedEffect(Unit) {
                        try {
                            focusRequester.requestFocus()
                        } catch (_: IllegalStateException) {
                            // El diálogo pudo cerrarse antes de que el campo terminara de adjuntarse.
                        }
                        keyboardController?.show()
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = inputLabel,
                                style = Fonts.textRegular(16.sp)
                            )

                            Text(
                                text = "($currencyLabel)",
                                style = Fonts.textBold(16.sp)
                            )
                        }

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            value = text,
                            onValueChange = {
                                if (it.text.isValidDecimalInput()) {
                                    text = it
                                }
                            },
                            singleLine = true,
                            leadingIcon = {
                                Box(
                                    contentAlignment = Alignment.CenterStart,
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .size(24.dp)
                                ) {
                                    Text(text = "S/", modifier = Modifier.padding(start = 4.dp))
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                } else {
                    Text(
                        text = readOnlyMessage,
                        style = Fonts.textRegular(14.sp, grayPlaceholder, TextAlign.Center)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .height(50.dp)
                            .weight(2f),
                        text = buttonClose,
                        textSize = 18.sp,
                        border = darkButton,
                        textColor = darkButton,
                        onClick = { onDismissRequest.invoke() })

                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .height(50.dp)
                            .weight(3f),
                        text = buttonSave,
                        textColor = white,
                        textSize = 18.sp,
                        border = darkButton,
                        background = darkButton,
                        onClick = { onConfirmation.invoke(text.text) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OpenSalesBoxDialogPreview() {
    OpenSalesBoxDialog(
        title = "Abrir caja",
        inputLabel = "Monto inicial",
        currencyLabel = "Efectivo",
        buttonClose = "No",
        buttonSave = "Sí",
        onDismissRequest = {},
        onConfirmation = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CloseSalesBoxDialogPreview() {
    OpenSalesBoxDialog(
        title = "Cerrar caja",
        inputLabel = "Monto inicial",
        currencyLabel = "Efectivo",
        readOnlyMessage = "¿Estás seguro de cerrar la caja?",
        buttonClose = "No",
        buttonSave = "Sí",
        onDismissRequest = {},
        onConfirmation = {}
    )
}
