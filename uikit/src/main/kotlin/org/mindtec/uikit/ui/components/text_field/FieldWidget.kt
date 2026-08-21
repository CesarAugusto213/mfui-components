package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.WidgetCounter
import org.mindtec.uikit.ui.components.dialog.InputTextDialog
import org.mindtec.uikit.utils.Fonts
import java.math.BigDecimal

@Composable
fun FieldWidget(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String,
    quantity: BigDecimal,
    isZero: Boolean = false,
    onClickedMinus: (BigDecimal) -> Unit,
    onClickedPlus: (BigDecimal) -> Unit,
    onConfirmation: (BigDecimal) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = Fonts.textRegular(16.sp)
        )

        WidgetCounter(
            enabled = enabled,
            stockControl = false,
            quantity = quantity,
            isZero = isZero,
            height = 55.5.dp,
            showInputDialog = { showDialog = true },
            onClickedMinus = onClickedMinus,
            onClickedPlus = onClickedPlus
        )

        if (showDialog) {
            InputTextDialog(
                description = stringResource(R.string.uikit_enter_amount),
                stock = quantity,
                stockRestriction = false,
                value = quantity.toString(),
                onDismissRequest = { showDialog = false },
                onConfirmation = {
                    showDialog = false
                    onConfirmation.invoke(it)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FieldWidgetPreview() {
    FieldWidget(
        modifier = Modifier.padding(16.dp),
        title = "Cantidad",
        quantity = BigDecimal(3),
        onClickedMinus = {},
        onClickedPlus = {},
        onConfirmation = {}
    )
}
