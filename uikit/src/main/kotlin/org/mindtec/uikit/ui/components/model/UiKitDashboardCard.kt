package org.mindtec.uikit.ui.components.model

import androidx.compose.ui.graphics.painter.Painter

/**
 * Reemplaza a `ItemCardDashboard` para [org.mindtec.uikit.ui.components.items.ItemReportDashboard].
 *
 * @param trailingIcon Insignia opcional a la derecha del ícono principal
 * (en el original, `img` — ej. un logo de SUNAT para indicar el origen del
 * dato).
 */
data class UiKitDashboardCard(
    val icon: Painter,
    val trailingIcon: Painter? = null,
    val title: String,
    val value: String
)
