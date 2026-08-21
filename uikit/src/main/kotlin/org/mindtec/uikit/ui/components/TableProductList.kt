package org.mindtec.uikit.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.items.ItemProductList
import org.mindtec.uikit.ui.components.model.UiKitProduct
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.restrictDigits
import java.math.BigDecimal

@Composable
fun TableProductList(
    modifier: Modifier = Modifier,
    items: List<UiKitProduct>,
    onClickDelete: (String) -> Unit,
    disableTotal: Boolean = false,
    showTotal: Boolean = true
) {
    val calculatedTotal = if (!disableTotal) {
        items.sumOf { it.unitPrice.multiply(it.quantity) }
    } else {
        items.sumOf { it.quantity }
    }

    // Envuelto en su propio Column para que la lista + el footer se apilen
    // sin importar en qué contenedor se use TableProductList (antes
    // dependía de que quien llamara ya estuviera dentro de un Column).
    Column {
        BoxItemsProducts(
            modifier = modifier,
            items = items,
            onClickDelete = onClickDelete,
            disableTotal = disableTotal
        )

        if (showTotal) {
            FooterTableProduct(
                calculatedTotal = calculatedTotal.restrictDigits(),
                disableTotal = disableTotal
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BoxItemsProducts(
    modifier: Modifier = Modifier,
    items: List<UiKitProduct>,
    onClickDelete: (String) -> Unit,
    disableTotal: Boolean
) {
    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val containerHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val thumbHeightPx = containerHeightPx * 0.15f
        val maxScroll = scrollState.maxValue.toFloat().coerceAtLeast(1f)
        val scrollProgress = scrollState.value / maxScroll
        val thumbOffsetPx = (containerHeightPx - thumbHeightPx) * scrollProgress
        val isAllContentDisplay = scrollState.maxValue == 0

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(end = if (!isAllContentDisplay) 6.dp else 0.dp)
        ) {
            items.forEachIndexed { index, item ->
                ItemProductList(
                    item,
                    onClickDelete = { onClickDelete(index.toString()) },
                    disableTotal = disableTotal
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = gray,
                    thickness = 0.5.dp
                )
            }
        }

        if (!isAllContentDisplay) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset { IntOffset(0, thumbOffsetPx.toInt()) }
                    .width(4.dp)
                    .height(with(LocalDensity.current) { thumbHeightPx.toDp() })
                    .background(purple300, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
private fun FooterTableProduct(
    calculatedTotal: BigDecimal,
    disableTotal: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = lightGrayPlaceholder),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (!disableTotal) stringResource(id = R.string.uikit_total_to_pay) else stringResource(
                    R.string.uikit_total_units
                ),
                style = TextStyle(
                    color = neutral700,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Row {
                Text(
                    text = if (!disableTotal) "S/ $calculatedTotal" else calculatedTotal.toString(),
                    style = TextStyle(
                        color = neutral700,
                        fontSize = 16.sp,
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.size(24.dp))
            }
        }
    }
}
