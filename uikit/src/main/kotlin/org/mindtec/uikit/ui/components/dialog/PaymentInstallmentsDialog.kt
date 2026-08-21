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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import org.mindtec.uikit.ui.components.model.UiKitInstallment
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import org.mindtec.uikit.utils.restrictDigits
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"

@SuppressLint("DefaultLocale")
private fun calculateDistributedAmounts(
    installments: List<UiKitInstallment>,
    total: BigDecimal
): List<UiKitInstallment> {
    if (installments.isEmpty()) return emptyList()

    val numberOfInstallments = installments.size
    val baseAmount = total.divide(BigDecimal(numberOfInstallments), 2, RoundingMode.HALF_DOWN)

    val totalAllocated = baseAmount.multiply(BigDecimal(numberOfInstallments))
    val remainder = total.subtract(totalAllocated)

    return installments.mapIndexed { index, installment ->
        val amount = if (index == numberOfInstallments - 1) {
            baseAmount.add(remainder)
        } else {
            baseAmount
        }

        installment.copy(
            amount = amount.restrictDigits().toString()
        )
    }
}

/**
 * Diálogo de pago a crédito en cuotas. Reemplaza `QuotasState` por
 * [UiKitInstallment] y recibe el estado inicial directo por parámetro
 * (antes venía envuelto en un `DataPaymentMethodUiState`).
 */
@Composable
fun PaymentInstallmentsDialog(
    title: String,
    initialInstallments: List<UiKitInstallment>? = null,
    total: BigDecimal?,
    methodLabel: String,
    dateFormatPattern: String = DEFAULT_DATE_FORMAT,
    saveInstallmentsPayment: (List<UiKitInstallment>, String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    val context = LocalContext.current.applicationContext
    val dateFormat = remember(dateFormatPattern) { SimpleDateFormat(dateFormatPattern, Locale.getDefault()) }

    fun nextMonthDate(installmentIndex: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, installmentIndex + 1)
        return dateFormat.format(calendar.time)
    }

    var installmentsList by remember {
        val initial = initialInstallments ?: listOf(
            UiKitInstallment(
                date = nextMonthDate(0),
                amount = "0.00"
            )
        )
        mutableStateOf(calculateDistributedAmounts(initial, total ?: BigDecimal.ZERO))
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
                            color = purple300,
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(darkButton)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.uikit_date),
                        style = Fonts.textSemiBold(14.sp, white),
                        textAlign = TextAlign.Center
                    )

                    VerticalDivider(
                        modifier = Modifier.height(14.dp),
                        color = white,
                        thickness = 1.dp
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.uikit_payment_amount),
                        style = Fonts.textSemiBold(14.sp, white),
                        textAlign = TextAlign.Center
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(top = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(installmentsList) { index, installment ->
                        SimpleInstallmentRow(
                            installment = installment,
                            canDelete = index > 0 && installmentsList.size > 1,
                            dateFormat = dateFormat,
                            onDateChange = { newDate ->
                                installmentsList = installmentsList.mapIndexed { i, item ->
                                    if (i == index) item.copy(date = newDate) else item
                                }
                            },
                            onDelete = {
                                if (index > 0) {
                                    val newList =
                                        installmentsList.filterIndexed { i, _ -> i != index }
                                    installmentsList = calculateDistributedAmounts(
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
                        val newList = installmentsList + UiKitInstallment(
                            date = nextMonthDate(installmentsList.size),
                            amount = "0.00"
                        )

                        installmentsList =
                            calculateDistributedAmounts(newList, total ?: BigDecimal.ZERO)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = purple300
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(R.string.uikit_add_installment),
                        style = Fonts.textRegular(14.sp, purple300)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .height(50.dp)
                            .weight(2f),
                        text = stringResource(R.string.uikit_close),
                        textSize = 18.sp,
                        border = darkButton,
                        textColor = darkButton,
                        onClick = { onDismissRequest.invoke() })

                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .height(50.dp)
                            .weight(2f),
                        text = stringResource(R.string.uikit_save),
                        textColor = white,
                        textSize = 18.sp,
                        border = purple300,
                        background = purple300,
                        onClick = {
                            val hasEmptyFields = installmentsList.any {
                                it.date.isNullOrEmpty() || it.amount.isNullOrEmpty() || it.amount == "0.00"
                            }

                            if (hasEmptyFields) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.uikit_message_dialog_alert_incomplete),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                saveInstallmentsPayment.invoke(installmentsList, methodLabel)
                                onConfirmation.invoke()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleInstallmentRow(
    installment: UiKitInstallment,
    canDelete: Boolean,
    dateFormat: SimpleDateFormat,
    onDateChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleDateField(
            modifier = Modifier.weight(1f),
            value = installment.date ?: "",
            dateFormat = dateFormat,
            onValueChange = onDateChange
        )

        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            value = installment.amount ?: "",
            onValueChange = { },
            readOnly = true,
            singleLine = true,
            leadingIcon = {
                Text(
                    text = "S/",
                    style = Fonts.textRegular(14.sp, purple300),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        )

        if (canDelete) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onDelete.invoke()
                    },
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = red200
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleDateField(
    modifier: Modifier = Modifier,
    value: String,
    dateFormat: SimpleDateFormat,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current.applicationContext

    var showDatePicker by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(value) }

    LaunchedEffect(value) {
        text = value
    }

    val onDateSelected: (Long) -> Unit = { selectedDateMillis ->
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = selectedDateMillis

        val localCalendar = Calendar.getInstance()
        localCalendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        text = dateFormat.format(localCalendar.time)
        onValueChange(text)
        showDatePicker = false
    }

    OutlinedTextField(
        modifier = modifier
            .height(56.dp)
            .clickable { showDatePicker = true },
        value = text,
        onValueChange = { },
        enabled = false,
        readOnly = true,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = grayTextField,
            disabledContainerColor = white,
            disabledTextColor = grayTextField
        )
    )

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val currentDateMillis = calendar.timeInMillis

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDateMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= currentDateMillis
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    if (datePickerState.selectedDateMillis != null) {
                        onDateSelected(datePickerState.selectedDateMillis!!)
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.uikit_select_date_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Text(stringResource(R.string.uikit_apply))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.uikit_close))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
