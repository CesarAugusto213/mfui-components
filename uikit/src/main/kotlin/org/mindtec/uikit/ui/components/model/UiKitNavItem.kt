package org.mindtec.uikit.ui.components.model

import androidx.compose.ui.graphics.painter.Painter

/**
 * Un ítem de [org.mindtec.uikit.ui.components.NavigationBottomBar].
 * La app arma su propia lista — no hay pantallas fijas.
 */
data class UiKitNavItem(
    val route: String,
    val label: String,
    val icon: Painter
)
