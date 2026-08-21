package org.mindtec.uikit.ui.components.model

/**
 * Opción simple id/descripción para selects genéricos (ej. unidades de
 * medida, tipos de afectación IGV) donde no vale la pena definir un modelo
 * dedicado. Usado por [org.mindtec.uikit.ui.components.dialog.DialogFormFreeProduct].
 */
data class UiKitOption(
    val id: String,
    val description: String
)
