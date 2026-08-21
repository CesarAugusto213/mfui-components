package org.mindtec.uikit.utils

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Recorta ceros de más para mostrar montos limpios (ej. 12.500 -> 12.5,
 * 12.00 -> 12). Usado por los componentes de listas de productos para
 * formatear precios y totales.
 */
fun BigDecimal.restrictDigits(digits: Int = 2): BigDecimal {
    val newValue = this.stripTrailingZeros()
    return newValue.setScale(digits, RoundingMode.DOWN)
}
