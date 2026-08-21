package org.mindtec.uikit.ui.components.dialog

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.components.model.UiKitCashPayment
import org.mindtec.uikit.ui.components.model.UiKitPaymentDestination
import org.mindtec.uikit.ui.components.model.UiKitPaymentMethod
import org.mindtec.uikit.ui.components.select.SelectSmallMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import org.mindtec.uikit.utils.restrictDigits
import java.math.BigDecimal
import java.math.RoundingMode

@SuppressLint("DefaultLocale")
private fun calculateDistributedPayments(
    payments: List<UiKitCashPayment>,
    total: BigDecimal
): List<UiKitCashPayment> {
    if (payments.isEmpty()) return emptyList()

    val numberOfPayments = payments.size
    val baseAmount = total.divide(BigDecimal(numberOfPayments), 2, RoundingMode.HALF_DOWN)

    val totalAllocated = baseAmount.multiply(BigDecimal(numberOfPayments))
    val remainder = total.subtract(totalAllocated)

    return payments.mapIndexed { index, payment ->
        val amount = if (index == numberOfPayments - 1) {
            baseAmount.add(remainder)
        } else {
            baseAmount
        }

        payment.copy(
            payWith = amount.restrictDigits().toString()
        )
    }
}

/**
 * Diálogo de pago en efectivo con soporte para varias formas de pago
 * repartidas por el total. Reemplaza `PaymentMethodEntity`/
 * `PaymentDestinationsEntity`/`CashState` por sus equivalentes genéricos
 * (`UiKit*`) y recibe el estado inicial directo por parámetro en vez de
 * un `DataPaymentMethodUiState` envolvente.
 */
@Composable
fun PaymentCashDialog(
    title: String,
    listPayment: List<UiKitPaymentMethod>?,
    listDestination: List<UiKitPaymentDestination>?,
    initialPayments: List<UiKitCashPayment>? = null,
    currentPaymentMethod: UiKitPaymentMethod?,
    currentPaymentDestinations: UiKitPaymentDestination?,
    total: BigDecimal?,
    methodLabel: String,
    saveCashPayment: (List<UiKitCashPayment>, String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    val context = LocalContext.current.applicationContext

    var paymentsList by remember {
        val initial = initialPayments ?: listOf(
            UiKitCashPayment(
                payment = currentPaymentMethod ?: listPayment?.firstOrNull(),
                destination = currentPaymentDestinations ?: listDestination?.firstOrNull(),
                payWith = "0.00"
            )
        )
        mutableStateOf(calculateDistributedPayments(initial, total ?: BigDecimal.ZERO))
    }

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = UiKitTheme.colors.primary,
                            shape = RoundedCornerShape(
                                8.dp,
                                8.dp,
                                0.dp,
                                0.dp
                            )
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        style = Fonts.textBold(16.sp, white, TextAlign.Center)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(darkButton)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.uikit_payment_method),
                            style = Fonts.textSemiBold(11.sp, white),
                            textAlign = TextAlign.Center
                        )

                        VerticalDivider(
                            modifier = Modifier.height(16.dp),
                            color = white,
                            thickness = 1.dp
                        )

                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.uikit_destination),
                            style = Fonts.textSemiBold(11.sp, white),
                            textAlign = TextAlign.Center
                        )

                        VerticalDivider(
                            modifier = Modifier.height(16.dp),
                            color = white,
                            thickness = 1.dp
                        )

                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.uikit_payment_amount),
                            style = Fonts.textSemiBold(11.sp, white),
                            textAlign = TextAlign.Center
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .height(if (paymentsList.size == 1) 80.dp else 200.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(paymentsList) { index, payment ->
                            PaymentRow(
                                payment = payment,
                                canDelete = index > 0 && paymentsList.size > 1,
                                listPayment = listPayment,
                                listDestination = listDestination,
                                onPaymentMethodChange = { newMethod ->
                                    paymentsList = paymentsList.mapIndexed { i, item ->
                                        if (i == index) item.copy(payment = newMethod) else item
                                    }
                                },
                                onDestinationChange = { newDestination ->
                                    paymentsList = paymentsList.mapIndexed { i, item ->
                                        if (i == index) item.copy(destination = newDestination) else item
                                    }
                                },
                                onAmountChange = { newAmount ->
                                    paymentsList = paymentsList.mapIndexed { i, item ->
                                        if (i == index) item.copy(payWith = newAmount) else item
                                    }
                                },
                                onDelete = {
                                    if (index > 0) {
                                        val newList =
                                            paymentsList.filterIndexed { i, _ -> i != index }
                                        paymentsList = calculateDistributedPayments(
                                            newList,
                                            total ?: BigDecimal.ZERO
                                        )
                                    }
                                }
                            )
                        }
                    }

                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val newList = paymentsList + UiKitCashPayment(
                                payment = listPayment?.firstOrNull(),
                                destination = listDestination?.firstOrNull(),
                                payWith = "0.00"
                            )

                            paymentsList =
                                calculateDistributedPayments(newList, total ?: BigDecimal.ZERO)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar",
                            tint = UiKitTheme.colors.primary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(R.string.uikit_add_payment),
                            style = Fonts.textRegular(14.sp, UiKitTheme.colors.primary)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = neutral100,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.uikit_total_to_pay_colon),
                                    style = Fonts.textSemiBold(14.sp, neutral700)
                                )
                                Text(
                                    text = "S/ ${total?.restrictDigits() ?: "0.00"}",
                                    style = Fonts.textSemiBold(14.sp, UiKitTheme.colors.primary)
                                )
                            }

                            val totalPayments = paymentsList.sumOf {
                                it.payWith?.toBigDecimalOrNull() ?: BigDecimal.ZERO
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.uikit_total_paying_colon),
                                    style = Fonts.textSemiBold(14.sp, neutral700)
                                )
                                Text(
                                    text = "S/ ${totalPayments.restrictDigits()}",
                                    style = Fonts.textSemiBold(
                                        14.sp,
                                        if (totalPayments.compareTo(total ?: BigDecimal.ZERO) == 0) UiKitTheme.colors.primary else red200
                                    )
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SmallOutlinedButtonMF(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f),
                            text = stringResource(R.string.uikit_close),
                            textSize = 16.sp,
                            border = neutral400,
                            textColor = neutral700,
                            onClick = { onDismissRequest.invoke() }
                        )

                        SmallOutlinedButtonMF(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f),
                            text = stringResource(R.string.uikit_save),
                            textColor = white,
                            textSize = 16.sp,
                            border = UiKitTheme.colors.primary,
                            background = UiKitTheme.colors.primary,
                            onClick = {
                                val hasEmptyFields = paymentsList.any {
                                    it.payment == null || it.destination == null ||
                                            it.payWith.isNullOrEmpty() || it.payWith == "0.00"
                                }

                                val totalPaymentsAmount = paymentsList.sumOf {
                                    it.payWith?.toBigDecimalOrNull() ?: BigDecimal.ZERO
                                }

                                if (hasEmptyFields) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.uikit_message_dialog_alert_incomplete),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (totalPaymentsAmount.compareTo(total ?: BigDecimal.ZERO) != 0) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.uikit_payment_total_mismatch),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    saveCashPayment.invoke(paymentsList, methodLabel)
                                    onConfirmation.invoke()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentRow(
    payment: UiKitCashPayment,
    canDelete: Boolean,
    listPayment: List<UiKitPaymentMethod>?,
    listDestination: List<UiKitPaymentDestination>?,
    onPaymentMethodChange: (UiKitPaymentMethod) -> Unit,
    onDestinationChange: (UiKitPaymentDestination) -> Unit,
    onAmountChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listPayment?.let { list ->
            SelectSmallMF(
                modifier = Modifier.weight(1f),
                options = list,
                selectedOption = list.first().description,
                onOptionSelected = onPaymentMethodChange,
                optionLabel = { it.description }
            )
        }

        val filteredDestinations = listDestination ?: emptyList()

        if (filteredDestinations.isNotEmpty()) {
            SelectSmallMF(
                modifier = Modifier.weight(1f),
                options = filteredDestinations,
                selectedOption = filteredDestinations.first().description,
                onOptionSelected = onDestinationChange,
                optionLabel = { it.description }
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(45.dp),
            value = payment.payWith ?: "",
            onValueChange = { newValue ->
                onAmountChange(newValue)
            },
            singleLine = true,
            textStyle = Fonts.textRegular(14.sp, neutral700, TextAlign.Center)
        )

        if (canDelete) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDelete.invoke() }
                    .padding(2.dp),
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = red200
            )
        }
    }
}
