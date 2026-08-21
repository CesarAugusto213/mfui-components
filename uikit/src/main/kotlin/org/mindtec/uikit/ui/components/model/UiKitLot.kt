package org.mindtec.uikit.ui.components.model

import java.math.BigDecimal

/**
 * Reemplaza a `ItemLotsEntity` (Room) para `DialogItemLots` en modo
 * "series" ([LotsSelectionType.SERIES_ONLY] y [LotsSelectionType.LOTS_WITH_SERIES]).
 */
data class UiKitSeries(
    val id: Int,
    val series: String,
    val date: String
)

/**
 * Reemplaza a `LotsGroupEntity` (Room) para `DialogItemLots` en modo
 * "lotes" ([LotsSelectionType.LOTS_ONLY] y [LotsSelectionType.LOTS_WITH_SERIES]).
 *
 * @param quantity Cantidad disponible en el lote.
 * @param selectedQuantity Cantidad ya elegida (antes era `auxQuantity: String?`
 * — el diálogo la actualiza internamente mientras el usuario elige).
 */
data class UiKitLotGroup(
    val id: Int,
    val code: String,
    val quantity: BigDecimal,
    val dateOfDue: String,
    val selectedQuantity: Int = 0
)
