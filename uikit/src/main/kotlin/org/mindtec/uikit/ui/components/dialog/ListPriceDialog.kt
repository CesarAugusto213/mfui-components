package org.mindtec.uikit.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.model.UiKitPriceOption
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import java.math.BigDecimal

/**
 * Reemplaza a `PresentationData` — llevaba `ItemTypesEntity` +
 * originalPrice/originalUnitType, redundante ahora que la app ya tiene
 * esos dos últimos y [UiKitPriceOption] trae todo lo demás resuelto.
 */
data class SelectedPriceParam(
    val priceOption: UiKitPriceOption? = null,
    val priceNew: BigDecimal,
    val unitTypeId: String
)

/**
 * Diálogo de lista de precios (presentaciones) de un producto. Reemplaza
 * `ItemTypesEntity` por [UiKitPriceOption] — el precio a mostrar por fila
 * ya llega resuelto (el original elegía entre `price1`/`price2`/`price3`
 * según `priceDefault`; esa selección la hace ahora la app al mapear).
 *
 * A diferencia del original (que envolvía todo en un `ListPriceDialogUiState`
 * con un `ItemProductsUiState` adentro), acá el estado entra directo por
 * parámetros.
 */
@Composable
fun ListPriceDialog(
    show: Boolean,
    options: List<UiKitPriceOption>?,
    originalPrice: BigDecimal,
    originalUnitType: String,
    currentPrice: BigDecimal,
    onSelectItem: (SelectedPriceParam) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = white),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp, 15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(purple300)
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.uikit_label_list_price),
                            style = Fonts.textLight(15.sp, white, TextAlign.Center)
                        )
                    }

                    Spacer(modifier = Modifier.size(2.dp))

                    options?.let {
                        TableListPrice(
                            content = it,
                            originalPrice = originalPrice,
                            originalUnitType = originalUnitType,
                            currentPrice = currentPrice,
                            onSelectItem = onSelectItem,
                            onDismissRequest = onDismissRequest
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TableListPrice(
    content: List<UiKitPriceOption>,
    originalPrice: BigDecimal,
    originalUnitType: String,
    currentPrice: BigDecimal,
    onSelectItem: (SelectedPriceParam) -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PriceTableHeader()

        if (content.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(content) { row ->
                    PriceTableRow(
                        option = row,
                        originalPrice = originalPrice,
                        originalUnitType = originalUnitType,
                        currentPrice = currentPrice,
                        onSelectItem = onSelectItem,
                        onDismissRequest = onDismissRequest
                    )
                }
            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.uikit_label_single_price),
                style = Fonts.textLight(
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun PriceTableHeader() {
    val header = listOf(
        stringResource(R.string.uikit_table_header_und),
        stringResource(R.string.uikit_table_header_desc),
        stringResource(R.string.uikit_table_header_factor),
        stringResource(R.string.uikit_table_header_price)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        for (item in header) {
            Box(
                modifier = Modifier
                    .background(purple300)
                    .weight(2f)
                    .height(30.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    style = TextStyle(
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                        color = white
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .background(purple300)
                .weight(1.5f)
                .height(30.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.width(16.dp),
                painter = painterResource(id = R.drawable.uikit_ic_checkbox),
                contentDescription = "checkbox",
                tint = white
            )
        }
    }
}

@Composable
private fun PriceTableRow(
    option: UiKitPriceOption,
    originalPrice: BigDecimal,
    originalUnitType: String,
    currentPrice: BigDecimal,
    onSelectItem: (SelectedPriceParam) -> Unit,
    onDismissRequest: () -> Unit
) {
    val rows = listOf(
        "und" to option.unitTypeId,
        "desc" to option.description,
        "factor" to option.quantityUnit,
        "price" to option.price.toString()
    )

    var isSelected by remember { mutableStateOf(currentPrice.compareTo(option.price) == 0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(neutral200),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        for (item in rows) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(42.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.second,
                    style = Fonts.textLight(
                        fontSize = 12.sp,
                        color = if (item.first == "price") red200 else black,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1.5f)
                .height(42.dp)
                .padding(8.dp)
                .clickable {
                    isSelected = !isSelected
                    if (isSelected) {
                        onSelectItem.invoke(
                            SelectedPriceParam(
                                priceOption = option,
                                priceNew = option.price,
                                unitTypeId = option.unitTypeId
                            )
                        )
                    } else {
                        onSelectItem.invoke(
                            SelectedPriceParam(
                                priceOption = null,
                                priceNew = originalPrice,
                                unitTypeId = originalUnitType
                            )
                        )
                    }
                    onDismissRequest.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .border(
                        width = 1.dp,
                        color = black,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(
                                color = black,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
