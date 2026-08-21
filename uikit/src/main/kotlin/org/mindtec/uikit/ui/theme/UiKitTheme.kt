package org.mindtec.uikit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * Envoltorio sobre MaterialTheme para los componentes de este kit. Envolvé
 * tu app (o la parte que usa este kit) para que los componentes tomen
 * [colors] en vez de los morados por defecto:
 *
 * ```kotlin
 * UiKitTheme(colors = UiKitColors.fromPrimary(Color(0xFF2196F3))) {
 *     // tu contenido
 * }
 * ```
 *
 * Sin envolver nada, todo se ve exactamente igual que antes
 * ([UiKitColors.Default]). Tampoco fuerza una tipografía propietaria —
 * pasá tu propia [typography] si querés una fuente custom.
 */
@Composable
fun UiKitTheme(
    colors: UiKitColors = UiKitColors.Default,
    typography: Typography = Typography(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalUiKitColors provides colors) {
        MaterialTheme(
            typography = typography,
            content = content
        )
    }
}

/** Acceso a los colores de marca actuales — `UiKitTheme.colors.primary`. */
object UiKitTheme {
    val colors: UiKitColors
        @Composable
        @ReadOnlyComposable
        get() = LocalUiKitColors.current
}
