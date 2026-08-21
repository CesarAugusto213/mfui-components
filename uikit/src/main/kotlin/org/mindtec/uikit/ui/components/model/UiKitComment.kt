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
    val comments: List<UiKitComment> = emptyList()
)
