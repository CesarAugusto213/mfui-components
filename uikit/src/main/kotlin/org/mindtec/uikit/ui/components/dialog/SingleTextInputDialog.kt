package org.mindtec.uikit.ui.components.dialog

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.theme.darkButton
import org.mindtec.uikit.ui.theme.white

/**
 * Diálogo genérico para pedir una línea de texto (email, un código, un
 * motivo corto, etc.) con confirmar/cancelar. Reemplaza a `SendEmailDialog`
 * — no tenía nada específico de email, así que se renombró.
 */
@Composable
fun SingleTextInputDialog(
    description: String,
    cancelButtonText: String = stringResource(R.string.uikit_cancel),
    confirmButtonText: String = stringResource(R.string.uikit_confirm),
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    var inputText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LaunchedEffect(Unit) {
                    try {
                        focusRequester.requestFocus()
                    } catch (_: IllegalStateException) {
                        // El diálogo pudo cerrarse antes de que el campo terminara de adjuntarse.
                    }
                    keyboardController?.show()
                }

                Text(text = description)
                Spacer(modifier = Modifier.size(12.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = inputText,
                    onValueChange = { inputText = it },
                    minLines = 1,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onConfirmation.invoke(inputText)
                            onDismissRequest.invoke()
                        }
                    )
                )
                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .height(50.dp)
                            .weight(1f),
                        text = cancelButtonText,
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
                        text = confirmButtonText,
                        textColor = white,
                        textSize = 18.sp,
                        border = darkButton,
                        background = darkButton,
                        onClick = {
                            onConfirmation.invoke(inputText)
                            onDismissRequest.invoke()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleTextInputDialogPreview() {
    SingleTextInputDialog(
        description = "Ingresa tu correo",
        onDismissRequest = {},
        onConfirmation = {}
    )
}
