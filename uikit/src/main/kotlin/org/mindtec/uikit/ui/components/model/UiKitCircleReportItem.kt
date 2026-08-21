package org.mindtec.uikit.ui.components.model

import androidx.compose.ui.graphics.Color
import java.math.BigDecimal

/**
 * Reemplaza a `ItemsCircleReport` para [org.mindtec.uikit.ui.components.items.ItemCircleReport].
 * El `Modifier` que traía el original se sacó del modelo — ahora es un
 * parámetro normal del composable.
 */
data class UiKitCircleReportItem(
    val title: String,
    val percentage: BigDecimal,
    val primaryColor: Color,
    val primaryText: String,
    val secondaryColor: Color,
    val secondaryText: String,
    val number: String
)
