package org.mindtec.uikit.ui.components.model

import java.math.BigDecimal

/**
 * Reemplaza a `ProductEntity` (Room) para `ItemProduct` — el item de un
 * catálogo/grilla de selección de productos (distinto de [UiKitProduct],
 * que representa una línea ya agregada a un pedido/documento).
 */
data class UiKitCatalogProduct(
    val id: String,
    val description: String,
    val imageUrl: String? = null,
    val stock: BigDecimal = BigDecimal.ZERO,
    val currencySymbol: String = "S/",
    val category: String? = null,
    val calculateQuantity: Boolean = false
)
