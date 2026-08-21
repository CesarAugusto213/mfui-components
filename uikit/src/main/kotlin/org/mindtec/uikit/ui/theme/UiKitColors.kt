package org.mindtec.uikit.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

/**
 * Colores de marca del kit — el único grupo pensado para reemplazarse por
 * proyecto (el resto de la paleta en `Color.kt`, grises y colores de
 * estado, se queda fija a propósito: no suele tener sentido "temear" un
 * gris neutro o un rojo de error).
 *
 * Por defecto son los 5 tonos de morado que ya traía el kit
 * ([UiKitColors.Default]), pero cualquier app puede pasar los suyos —
 * a mano ([UiKitColors]) o generados desde un solo color con
 * [UiKitColors.fromPrimary].
 *
 * @param primary El color principal: fondo de botones/toolbar, bordes de
 * estado activo/seleccionado.
 * @param primaryContainer Fondo claro para badges/chips seleccionados.
 * @param primaryContainerAlt Variante del fondo claro (bordes de tabs con
 * contenido, por ejemplo).
 * @param secondary Acento secundario — texto/íconos que necesitan destacar
 * sin ser tan intensos como [primary].
 * @param primaryPressed Tono oscuro — estados presionados/activos fuertes.
 */
data class UiKitColors(
    val primary: Color,
    val primaryContainer: Color,
    val primaryContainerAlt: Color,
    val secondary: Color,
    val primaryPressed: Color
) {
    companion object {
        val Default = UiKitColors(
            primary = purple300,
            primaryContainer = purple100,
            primaryContainerAlt = purple150,
            secondary = purple200,
            primaryPressed = purple400
        )

        /**
         * Genera las 5 variantes a partir de un único color, mezclándolo
         * con blanco/negro (funciona para cualquier tono — azul, verde,
         * amarillo — sin volverse "sucio" como puede pasar mezclando por
         * luminosidad HSL). Si algún tono generado no queda como quieres,
         * sobrescríbelo a mano con `.copy(...)`.
         */
        fun fromPrimary(primary: Color): UiKitColors = UiKitColors(
            primary = primary,
            primaryContainer = primary.tint(0.85f),
            primaryContainerAlt = primary.tint(0.82f),
            secondary = primary.tint(0.15f),
            primaryPressed = primary.shade(0.25f)
        )
    }
}

private fun Color.tint(fraction: Float): Color =
    Color(ColorUtils.blendARGB(this.toArgb(), 0xFFFFFFFF.toInt(), fraction))

private fun Color.shade(fraction: Float): Color =
    Color(ColorUtils.blendARGB(this.toArgb(), 0xFF000000.toInt(), fraction))

val LocalUiKitColors = staticCompositionLocalOf { UiKitColors.Default }
