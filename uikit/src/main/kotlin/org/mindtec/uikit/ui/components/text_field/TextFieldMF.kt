package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import org.mindtec.uikit.R
import org.mindtec.uikit.utils.Fonts

@Composable
fun TextFieldMF(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    value: String = "",
    onChangeValue: (String) -> Unit = {},
    lines: Int = 1,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    titleFontSize: TextUnit = 16.sp,
    isNumericKeyboard: Boolean = false,
    isDoneButton: Boolean = false,
    showError: Boolean = false,
    disabled: Boolean = false,
    onDone: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = Fonts.textRegular(titleFontSize)
            )
            description?.let {
                Text(
                    text = it,
                    style = Fonts.textRegular(12.sp, grayPlaceholder)
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { newValue ->
                if (!disabled) {
                    if (isNumericKeyboard) {
                        if (newValue.all { it.isDigit() || it == '.' } && newValue.count { it == '.' } <= 1) {
                            onChangeValue.invoke(newValue)
                        }
                    } else {
                        onChangeValue.invoke(newValue)
                    }
                }
            },
            minLines = lines,
            maxLines = lines,
            singleLine = lines == 1,
            enabled = enabled && !disabled,
            readOnly = readOnly || disabled,
            keyboardOptions = if (isNumericKeyboard) {
                KeyboardOptions(keyboardType = KeyboardType.Number)
            } else if (isDoneButton) {
                KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            } else {
                KeyboardOptions.Default
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onDone.invoke()
                }
            )
        )

        if (showError) {
            Text(
                text = stringResource(R.string.uikit_field_required_error),
                style = Fonts.textRegular(12.sp, red200)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldMFPreview() {
    TextFieldMF(
        modifier = Modifier.padding(16.dp),
        title = "Nombre",
        value = "Valor por defecto",
        onChangeValue = {},
        showError = true
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldMFPreviewWithoutValue() {
    TextFieldMF(
        modifier = Modifier.padding(16.dp),
        title = "Nombre",
        onChangeValue = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldMFPreviewWithoutValueDisabled() {
    TextFieldMF(
        modifier = Modifier.padding(16.dp),
        title = "Nombre",
        onChangeValue = {},
        disabled = true
    )
}
