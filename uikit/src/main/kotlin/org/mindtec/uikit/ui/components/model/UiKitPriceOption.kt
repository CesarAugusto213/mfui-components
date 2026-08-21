package org.mindtec.uikit.ui.components.model

import java.math.BigDecimal

/**
 * Reemplaza a `ItemTypesEntity` (Room) para `ListPriceDialog`. El original
 * traía 3 precios fijos (`price1`/`price2`/`price3`) y un `priceDefault`
 * que elegía cuál mostrar; esa selección ahora la hace la app al mapear su
 * entidad — acá llega un único [price] ya resuelto.
 */
data class UiKitPriceOption(
    val id: String,
    val unitTypeId: String,
    val description: String,
    val quantityUnit: String,
    val price: BigDecimal
)
