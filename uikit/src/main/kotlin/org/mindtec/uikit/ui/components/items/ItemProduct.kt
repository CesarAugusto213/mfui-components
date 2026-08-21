package org.mindtec.uikit.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.WidgetCounter
import org.mindtec.uikit.ui.components.button.OutlineIconButtonMF
import org.mindtec.uikit.ui.components.model.UiKitCatalogProduct
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import java.math.BigDecimal

/**
 * Card de un producto dentro de una grilla de catálogo/selección (distinto
 * de [org.mindtec.uikit.ui.components.items.ItemProductList], que muestra
 * una línea ya agregada a un pedido). Reemplaza `ProductEntity` por
 * [UiKitCatalogProduct] — ver ese modelo para el mapeo desde tu entidad.
 *
 * Los íconos del footer (editar / comentario / granel / precio por lista)
 * son decorativos y vienen empaquetados en la librería, igual que en el
 * componente original — no se promovieron a parámetro porque el caller
 * nunca los elegía.
 */
@Composable
fun ItemProduct(
    modifier: Modifier = Modifier,
    product: UiKitCatalogProduct,
    currentCost: BigDecimal,
    isChecked: Boolean,
    quantity: BigDecimal,
    stockControl: Boolean,
    isEditPriceEnabled: Boolean,
    hasLots: Boolean = false,
    isNotRestaurant: Boolean = true,
    isNotCreateDispatch: Boolean = true,
    showInputDialog: () -> Unit,
    showListPrice: (String) -> Unit,
    showCommentDialog: (UiKitCatalogProduct, String) -> Unit,
    showPriceDialog: () -> Unit,
    showQuantityByPrice: () -> Unit,
    showItemLotsDialog: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onClickedMinus: (BigDecimal) -> Unit,
    onClickedPlus: (BigDecimal) -> Unit
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = grayCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 350.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ImageItemProduct(
                image = product.imageUrl,
                isChecked = isChecked,
                hasItemLots = hasLots,
                onCheckedChange = onCheckedChange,
                showItemLotsDialog = showItemLotsDialog
            )

            DetailItemProduct(product, isNotCreateDispatch, isNotRestaurant)

            WidgetCounter(
                enabled = !hasLots,
                stock = product.stock,
                quantity = quantity,
                stockControl = stockControl,
                showInputDialog = showInputDialog,
                onClickedMinus = onClickedMinus,
                onClickedPlus = onClickedPlus
            )

            if (isNotCreateDispatch) {
                FooterItemProduct(
                    isNotRestaurant = isNotRestaurant,
                    isEditPriceEnabled = isEditPriceEnabled,
                    product = product,
                    cost = currentCost,
                    showListPrice = showListPrice,
                    showCommentDialog = showCommentDialog,
                    showPriceDialog = showPriceDialog,
                    showQuantityByPrice = showQuantityByPrice
                )
            }
        }
    }
}

@Composable
private fun ImageItemProduct(
    image: String?,
    isChecked: Boolean,
    hasItemLots: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showItemLotsDialog: () -> Unit
) {
    Box(
        modifier = Modifier.clickable {
            if (hasItemLots) {
                showItemLotsDialog.invoke()
            } else {
                onCheckedChange.invoke(!isChecked)
            }
        }
    ) {
        if (image == null) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp),
                painter = painterResource(id = R.drawable.uikit_img_null),
                contentDescription = null
            )
        } else {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp),
                model = image,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.uikit_img_null),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(18.dp)
                .background(white)
        ) {
            Checkbox(
                checked = isChecked,
                colors = CheckboxDefaults.colors(
                    checkedColor = blue300,
                    uncheckedColor = blue300,
                    checkmarkColor = white
                ),
                onCheckedChange = {
                    if (hasItemLots) {
                        showItemLotsDialog.invoke()
                    } else {
                        onCheckedChange.invoke(it)
                    }
                }
            )
        }
    }
}

@Composable
private fun DetailItemProduct(product: UiKitCatalogProduct, isNotCreateDispatch: Boolean, isNotRestaurant: Boolean) {
    Text(
        text = product.description.uppercase(),
        minLines = 2,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = Fonts.textRegular(16.sp)
    )

    if (isNotCreateDispatch && isNotRestaurant) {
        Text(
            text = "Stock: ${product.stock}",
            style = Fonts.textSemiBold(16.sp)
        )
    }
}

@Composable
private fun FooterItemProduct(
    isNotRestaurant: Boolean,
    isEditPriceEnabled: Boolean,
    product: UiKitCatalogProduct,
    cost: BigDecimal,
    showListPrice: (String) -> Unit,
    showCommentDialog: (UiKitCatalogProduct, String) -> Unit,
    showPriceDialog: () -> Unit,
    showQuantityByPrice: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(2f),
            text = "${product.currencySymbol} $cost",
            style = Fonts.textSemiBold(fontSize = 16.sp)
        )

        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditPriceEnabled) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlineIconButtonMF(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        icon = painterResource(R.drawable.uikit_ic_edit),
                        onClick = {
                            showPriceDialog.invoke()
                        }
                    )
                }
            }

            if (!isNotRestaurant) {
                product.category?.let {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlineIconButtonMF(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            icon = painterResource(R.drawable.uikit_ic_comment),
                            onClick = {
                                showCommentDialog.invoke(product, it)
                            }
                        )
                    }
                }
            } else {
                if (product.calculateQuantity) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlineIconButtonMF(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            icon = painterResource(R.drawable.uikit_ic_granel),
                            onClick = {
                                showQuantityByPrice.invoke()
                            }
                        )
                    }
                } else {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlineIconButtonMF(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            icon = painterResource(R.drawable.uikit_ic_pay),
                            onClick = {
                                showListPrice.invoke(product.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemProductPreview() {
    Row {
        ItemProduct(
            modifier = Modifier.weight(1f),
            product = UiKitCatalogProduct(
                id = "1",
                description = "DESCRIPCION PRODUCTO",
                currencySymbol = "S/",
                calculateQuantity = false,
                category = "",
                stock = BigDecimal.ZERO
            ),
            currentCost = BigDecimal(12),
            isChecked = true,
            isEditPriceEnabled = true,
            hasLots = false,
            quantity = BigDecimal(1),
            stockControl = true,
            showInputDialog = {},
            showCommentDialog = { _, _ -> },
            showPriceDialog = {},
            showQuantityByPrice = {},
            showItemLotsDialog = {},
            showListPrice = {},
            onCheckedChange = {},
            onClickedPlus = {},
            onClickedMinus = {}
        )

        ItemProduct(
            modifier = Modifier.weight(1f),
            product = UiKitCatalogProduct(
                id = "2",
                description = "DESCRIPCION PRODUCTO",
                currencySymbol = "S/",
                calculateQuantity = true,
                category = "",
                stock = BigDecimal.ZERO
            ),
            currentCost = BigDecimal(12),
            isChecked = true,
            isEditPriceEnabled = true,
            hasLots = true,
            quantity = BigDecimal(1),
            stockControl = true,
            showInputDialog = {},
            showCommentDialog = { _, _ -> },
            showPriceDialog = {},
            showQuantityByPrice = {},
            showItemLotsDialog = {},
            showListPrice = {},
            onCheckedChange = {},
            onClickedPlus = {},
            onClickedMinus = {}
        )
    }
}
