package org.mindtec.uikit.ui.components.dialog

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.components.select.SelectMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun <T> StateTypeDialog(
    title: String,
    selectLabel: String = stringResource(R.string.uikit_state_type_select_label),
    options: List<T>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    label: String,
    optionLabel: (T) -> String,
    onOptionSelected: (T) -> Unit = {}
) {
    var currentLabel by remember { mutableStateOf(label) }

    LaunchedEffect(label) {
        currentLabel = label
    }

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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = Fonts.textRegular(16.sp, neutral700, TextAlign.Center)
                )

                Spacer(modifier = Modifier.size(14.dp))

                SelectMF(
                    title = selectLabel,
                    options = options,
                    selectedOption = currentLabel,
                    onOptionSelected = onOptionSelected,
                    optionLabel = {
                        optionLabel.invoke(it)
                    }
                )

                Spacer(modifier = Modifier.size(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        text = stringResource(R.string.uikit_cancel),
                        textSize = 16.sp,
                        border = darkButton,
                        textColor = darkButton,
                        onClick = { onDismissRequest.invoke() }
                    )

                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        text = stringResource(R.string.uikit_confirm),
                        textColor = white,
                        textSize = 16.sp,
                        border = darkButton,
                        background = darkButton,
                        onClick = { onConfirmation.invoke() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StateTypeDialogPreview() {
    val options = listOf("Registrado", "Enviado", "Aceptado", "Observado")
    StateTypeDialog(
        title = "Actualizar estado",
        options = options,
        label = "Aceptado",
        optionLabel = { it },
        onDismissRequest = {},
        onConfirmation = {},
        onOptionSelected = {}
    )
}
