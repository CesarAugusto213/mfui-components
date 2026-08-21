package org.mindtec.uikit.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.mindtec.uikit.ui.components.text_field.TextFieldMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun ConfirmDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    showTextField: Boolean = false,
    titleTextField: String = stringResource(R.string.uikit_reason_label),
    valueTextField: String = "",
    showErrorTextField: Boolean = false
) {
    var textFieldValue by remember { mutableStateOf(valueTextField) }

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = white)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = Fonts.textRegular(
                        fontSize = 20.sp,
                        color = neutral800,
                        textAlign = TextAlign.Center
                    )
                )

                if (showTextField) {
                    TextFieldMF(
                        title = titleTextField,
                        onChangeValue = {
                            textFieldValue = it
                        },
                        value = textFieldValue,
                        showError = showErrorTextField
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SmallOutlinedButtonMF(
                        modifier = Modifier
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
                            .height(50.dp)
                            .weight(1f),
                        text = stringResource(R.string.uikit_confirm),
                        textColor = white,
                        textSize = 18.sp,
                        border = purple300,
                        background = purple300,
                        onClick = { onConfirmation.invoke(textFieldValue) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ConfirmDialogPreview() {
    ConfirmDialog(
        title = "Confirmar cambios",
        onDismissRequest = {},
        onConfirmation = {}
    )
}
