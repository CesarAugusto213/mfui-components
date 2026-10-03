package org.mindtec.uikit.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mindtec.uikit.R
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
import org.mindtec.uikit.ui.components.items.ItemProductRestaurantList
import org.mindtec.uikit.ui.components.model.ExpandedUiKitProduct
import org.mindtec.uikit.ui.components.model.UiKitProduct
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.RestaurantListUtils.expandProductsWithComments
import org.mindtec.uikit.utils.restrictDigits
import java.math.BigDecimal

@Composable
fun TableRestaurantProductList(
    modifier: Modifier = Modifier,
    items: List<UiKitProduct>,
    onClickDelete: (String, Int?) -> Unit // itemId, commentIndex (null if no comments)
) {
    // Expandir productos según comentarios
    val expandedItems = expandProductsWithComments(items)

    // Calcular total basado en los productos originales (para mantener la lógica actual)
    val calculatedTotal = items.sumOf { item ->
        item.unitPrice.multiply(item.quantity) + item.commentGroups.sumOf { group ->
            group.accessories.fold(BigDecimal.ZERO) { acc, accessory -> acc + accessory.price }
        }
    }

    // Envuelto en su propio Column por la misma razón que TableProductList:
    // que la lista + el footer se apilen sin depender de que el caller ya
    // esté dentro de un Column.
    Column {
        BoxItemsRestaurant(
            modifier = modifier,
            expandedItems = expandedItems,
            onClickDelete = onClickDelete
        )

        FooterTableProduct(calculatedTotal.restrictDigits())
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun BoxItemsRestaurant(
    modifier: Modifier = Modifier,
    expandedItems: List<ExpandedUiKitProduct>,
    onClickDelete: (String, Int?) -> Unit // itemId, commentIndex (null if no comments)
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
            expandedItems.forEach { expandedItem ->
                // Unidad individual (con notas o complementos): se borra esa unidad exacta.
                val commentIndex = if (expandedItem.isIndividualItem) expandedItem.itemIndex else null

                ItemProductRestaurantList(
                    item = expandedItem.product,
                    comments = expandedItem.comments,
                    accessories = expandedItem.accessories,
                    showQuantity = !expandedItem.isIndividualItem,
                    commentIndex = commentIndex,
                    onClickDelete = onClickDelete
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
                    .background(grayPlaceholder, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
private fun FooterTableProduct(calculatedTotal: BigDecimal) {
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
                text = stringResource(R.string.uikit_total_to_pay),
                style = TextStyle(
                    color = neutral700,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Row {
                Text(
                    text = "S/ $calculatedTotal",
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
