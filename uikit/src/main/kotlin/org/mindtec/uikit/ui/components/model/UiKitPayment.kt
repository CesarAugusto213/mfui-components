package org.mindtec.uikit.ui.components.model

/**
 * Reemplaza a `PaymentMethodEntity` (Room) para `PaymentCashDialog`.
 */
data class UiKitPaymentMethod(
    val id: String,
    val description: String
)

/**
 * Reemplaza a `PaymentDestinationsEntity` (Room) para `PaymentCashDialog`.
 */
data class UiKitPaymentDestination(
    val id: String,
    val description: String
)

/**
 * Reemplaza a `CashState` del proyecto original — una fila de pago en
 * efectivo dentro de [PaymentCashDialog].
 */
data class UiKitCashPayment(
    val payment: UiKitPaymentMethod? = null,
    val destination: UiKitPaymentDestination? = null,
    val payWith: String? = null
)

/**
 * Reemplaza a `QuotasState` del proyecto original — una cuota dentro de
 * `PaymentInstallmentsDialog`.
 */
data class UiKitInstallment(
    val date: String? = null,
    val amount: String? = null
)
