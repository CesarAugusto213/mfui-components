package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

/**
 * Campo numérico para documentos (DNI/RUC/pasaporte/etc.) con una acción
 * opcional a la derecha (ej. un botón de búsqueda). A diferencia del
 * original de MypeFact, no asume qué tipos de documento existen ni sus
 * longitudes (eso era específico de Perú/SUNAT) — el caller decide
 * [requiredLength] y arma su propio [action] (botón, validación y mensajes
 * de error incluidos).
 */
@Composable
fun FieldSearchNumDoc(
    modifier: Modifier = Modifier,
    title: String,
    number: String,
    requiredLength: Int,
    maxLength: Int = 20,
    onChangeValue: (String) -> Unit,
    showError: Boolean = number.isEmpty() || number.length < requiredLength,
    action: (@Composable RowScope.() -> Unit)? = null
) {
    val textState = remember { mutableStateOf(number) }
    textState.value = number

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = Fonts.textRegular(fontSize = 16.sp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (action != null) 8f else 1f)
                    .background(white),
                value = textState.value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        textState.value = newValue

                        if (newValue.length <= maxLength) {
                            onChangeValue.invoke(newValue)
                        }
                    }
                },
                minLines = 1,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            action?.let {
                Row(modifier = Modifier.weight(4f)) { it() }
            }
        }

        if (showError) {
            Text(
                text = stringResource(R.string.uikit_field_required_error),
                style = Fonts.textRegular(12.sp, red200)
            )
        }
    }
}

@Preview
@Composable
private fun FieldSearchNumDocPreview() {
    FieldSearchNumDoc(
        title = "Número de documento",
        number = "12345678",
        requiredLength = 8,
        onChangeValue = {}
    )
}
