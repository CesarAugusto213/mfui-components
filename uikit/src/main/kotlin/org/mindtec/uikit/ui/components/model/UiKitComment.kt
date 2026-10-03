package org.mindtec.uikit.ui.components.model

/**
 * Una indicación/nota de cocina (ej. "sin cebolla", "para llevar").
 * Reemplaza a `RestaurantNote`/`RestaurantNoteResponse` del proyecto
 * original — ahí eran dos clases casi idénticas (una para la respuesta de
 * red, otra para el estado de UI); acá es una sola.
 */
data class UiKitComment(
    val id: Int,
    val description: String,
    val categoryId: Int,
    val isSelected: Boolean = false
)

/**
 * Las indicaciones seleccionadas para una unidad puntual de un producto
 * (ej. la unidad #2 de 3 "sin cebolla"). Reemplaza a `ProductCommentGroup`.
 */
data class UiKitCommentGroup(
    val itemIndex: Int,
    val comments: List<UiKitComment> = emptyList(),
    /** Complementos (con precio) elegidos para esta unidad. */
    val accessories: List<UiKitAccessory> = emptyList()
) {
    val hasSelection: Boolean get() = comments.isNotEmpty() || accessories.isNotEmpty()
}

/** Complemento con precio que se puede sumar a una unidad (ej. "Extra queso + S/ 2.00"). */
data class UiKitAccessory(
    val id: Int,
    val name: String,
    val price: java.math.BigDecimal
)
