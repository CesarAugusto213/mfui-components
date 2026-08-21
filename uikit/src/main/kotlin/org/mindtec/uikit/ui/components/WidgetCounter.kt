package org.mindtec.uikit.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import java.math.BigDecimal

@Composable
fun WidgetCounter(
    enabled: Boolean = false,
    height: Dp = 38.dp,
    quantity: BigDecimal,
    stock: BigDecimal = BigDecimal(1),
    stockControl: Boolean,
    isZero: Boolean = false,
    showInputDialog: () -> Unit,
    onClickedMinus: (BigDecimal) -> Unit,
    onClickedPlus: (BigDecimal) -> Unit
) {
    val context = LocalContext.current.applicationContext
    var quantityLocal = quantity
    val belowMinMessage = stringResource(R.string.uikit_quantity_below_min, quantityLocal)
    val aboveStockMessage = stringResource(R.string.uikit_quantity_above_stock)

    Row(
        modifier = Modifier
            .background(
                color = if (enabled) white else borderGrayCard.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = grayPlaceholder,
                shape = RoundedCornerShape(8.dp)
            )
            .height(height)
            .fillMaxWidth()
    ) {
        if (enabled) {
            Text(
                text = "-",
                style = Fonts.textRegular(20.sp, grayPlaceholder),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp)
                    .weight(1f)
                    .clickable {
                        if (quantityLocal > BigDecimal(1) && !isZero) {
                            quantityLocal--
                            onClickedMinus.invoke(quantityLocal)
                        } else if (quantityLocal > BigDecimal(0) && isZero) {
                            quantityLocal--
                            onClickedMinus.invoke(quantityLocal)
                        } else {
                            Toast
                                .makeText(context, belowMinMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }

        Text(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable(enabled = enabled) {
                    showInputDialog.invoke()
                },
            text = quantityLocal.toString(),
            style = Fonts.textLight(fontSize = 22.sp, textAlign = TextAlign.Center)
        )

        if (enabled) {
            Text(
                text = "+",
                textAlign = TextAlign.End,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp)
                    .weight(1f)
                    .clickable {
                        if (quantityLocal < stock || !stockControl) {
                            quantityLocal++
                            onClickedPlus.invoke(quantityLocal)
                        } else {
                            Toast
                                .makeText(context, aboveStockMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WidgetCounterPreview() {
    WidgetCounter(
        enabled = false,
        height = 38.dp,
        quantity = BigDecimal(1),
        stock = BigDecimal(2.0),
        stockControl = true,
        showInputDialog = {},
        onClickedMinus = {},
        onClickedPlus = {}
    )
}
