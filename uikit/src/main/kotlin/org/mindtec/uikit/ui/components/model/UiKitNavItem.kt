package org.mindtec.uikit.ui.components.model

import androidx.compose.ui.graphics.painter.Painter

/**
 * Un ítem de [org.mindtec.uikit.ui.components.NavigationBottomBar].
 * Reemplaza al `NavigationBottomBarRouter` sellado del original (que traía
 * las 5 pantallas fijas de MypeFact hardcodeadas) — acá la app arma su
 * propia lista.
 */
data class UiKitNavItem(
    val route: String,
    val label: String,
    val icon: Painter
)
