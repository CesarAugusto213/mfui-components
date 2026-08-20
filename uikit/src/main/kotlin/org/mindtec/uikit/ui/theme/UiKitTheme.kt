package org.mindtec.uikit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

/**
 * Envoltorio mínimo sobre MaterialTheme para los componentes de este kit.
 * A diferencia del MyAppTheme original de MypeFact, este no fuerza una
 * tipografía propietaria (esa dependía de un font resource del app host).
 * Cada proyecto consumidor puede pasar su propia [typography] si quiere
 * una fuente custom, o dejar el default de Compose.
 */
@Composable
fun UiKitTheme(
    typography: Typography = Typography(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = typography,
        content = content
    )
}
