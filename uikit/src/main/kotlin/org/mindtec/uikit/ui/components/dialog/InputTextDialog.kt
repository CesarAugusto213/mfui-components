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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import java.math.BigDecimal
import java.math.RoundingMode

private fun String.toBigDecimalOrZero(): BigDecimal = try {
    BigDecimal(this).setScale(2, RoundingMode.HALF_UP)
} catch (_: Exception) {
    BigDecimal.ZERO
}

@Composable
fun InputTextDialog(
    description: String,
    stock: BigDecimal,
    stockRestriction: Boolean,
    value: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (BigDecimal) -> Unit
) {
    val context = LocalContext.current.applicationContext
    val keyboardController = LocalSoftwareKeyboardController.current
    var tempQuantity by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(0, value.length)
            )
        )
    }
    val focusRequester = remember { FocusRequester() }
    val invalidQuantityMessage = stringResource(R.string.uikit_invalid_quantity)
    val exceedsStockMessage = stringResource(R.string.uikit_quantity_exceeds_stock)

    fun confirmIfValid() {
        if (tempQuantity.text.isEmpty()) {
            Toast.makeText(context, invalidQuantityMessage, Toast.LENGTH_SHORT).show()
        } else if (tempQuantity.text.toBigDecimal() > stock && stockRestriction) {
            Toast.makeText(context, exceedsStockMessage, Toast.LENGTH_SHORT).show()
        } else {
            onConfirmation.invoke(tempQuantity.text.toBigDecimalOrZero())
            onDismissRequest.invoke()
        }
    }

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

                Text(
                    text = description,
                    style = Fonts.textRegular(14.sp)
                )

                Spacer(modifier = Modifier.size(12.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = tempQuantity,
                    onValueChange = { input ->
                        if (input.text.all { it.isDigit() || it == '.' } && input.text.count { it == '.' } <= 1) {
                            tempQuantity = input
                        }
                    },
                    minLines = 1,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { confirmIfValid() }
                    )
                )

                Spacer(modifier = Modifier.size(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        text = stringResource(R.string.uikit_cancel),
                        textSize = 16.sp,
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
                        textSize = 16.sp,
                        border = darkButton,
                        background = darkButton,
                        onClick = { confirmIfValid() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InputTextDialogPreview() {
    InputTextDialog(
        description = "Ingrese la cantidad:",
        stock = BigDecimal(1),
        stockRestriction = false,
        value = "222",
        onDismissRequest = {},
        onConfirmation = {}
    )
}
