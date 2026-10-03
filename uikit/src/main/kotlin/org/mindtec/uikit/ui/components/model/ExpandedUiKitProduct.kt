package org.mindtec.uikit.ui.components.model

/**
 * Un producto expandido a una unidad puntual (o a un grupo de unidades sin
 * indicaciones), con su comentario asociado. Lo produce
 * `RestaurantListUtils.expandProductsWithComments` y lo consume
 * `TableRestaurantProductList`. Reemplaza a `ExpandedProductItem`.
 */
data class ExpandedUiKitProduct(
    val product: UiKitProduct,
    val itemIndex: Int,
    val comments: List<UiKitComment> = emptyList(),
    val isIndividualItem: Boolean = true,
    /** Complementos de esta unidad (solo en items individuales). */
    val accessories: List<UiKitAccessory> = emptyList()
)
