package org.mindtec.uikit.ui.components.text_field

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldDate(
    modifier: Modifier = Modifier,
    title: String,
    defaultValue: String = "",
    isDateDocument: Boolean = true,
    disabledDays: Int = -4,
    onChangeValue: (String) -> Unit,
    icon: Painter,
    disableDialog: Boolean = false,
    enabled: Boolean = false,
    readOnly: Boolean = true,
    disableDaysFeature: Boolean = true,
    disableDaysFuture: Boolean = false,
    minSelectableDate: String? = null,
    maxSelectableDate: String? = null,
    dateFormatPattern: String = DEFAULT_DATE_FORMAT,
) {
    val context = LocalContext.current.applicationContext

    val dateFormat = remember(dateFormatPattern) { SimpleDateFormat(dateFormatPattern, Locale.getDefault()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(defaultValue) }

    LaunchedEffect(defaultValue) {
        text = defaultValue
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
        onChangeValue(text)
        showDatePicker = false
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = Fonts.textRegular(16.sp),
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (!disableDialog) {
                        showDatePicker = true
                    }
                },
            value = text,
            onValueChange = {
                text = it
                onChangeValue.invoke(it)
            },
            textStyle = Fonts.textRegular(16.sp),
            enabled = enabled,
            readOnly = readOnly,
            leadingIcon = {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = icon,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(purple300)
                )
            },
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = grayTextField,
                disabledContainerColor = white,
                disabledTextColor = grayTextField
            )
        )
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val currentDateMillis = calendar.timeInMillis

        if (disableDaysFeature) {
            calendar.add(Calendar.DAY_OF_MONTH, disabledDays)
        }

        val minDateMillis = calendar.timeInMillis

        fun parseToUtcDayMillis(dateStr: String): Long? {
            return try {
                val parsed = dateFormat.parse(dateStr) ?: return null
                val localCal = Calendar.getInstance().apply { time = parsed }
                Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                    set(localCal.get(Calendar.YEAR), localCal.get(Calendar.MONTH), localCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            } catch (e: Exception) { null }
        }

        val minCustomMillis = minSelectableDate?.let { parseToUtcDayMillis(it) }
        val maxCustomMillis = maxSelectableDate?.let { parseToUtcDayMillis(it) }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDateMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    if (minCustomMillis != null || maxCustomMillis != null) {
                        val aboveMin = minCustomMillis?.let { utcTimeMillis >= it } ?: true
                        val belowMax = maxCustomMillis?.let { utcTimeMillis <= it } ?: true
                        return aboveMin && belowMax
                    }
                    return if (disableDaysFeature) {
                        if (isDateDocument) {
                            utcTimeMillis in minDateMillis..currentDateMillis
                        } else {
                            utcTimeMillis >= minDateMillis
                        }
                    } else if (disableDaysFuture) {
                        utcTimeMillis <= currentDateMillis
                    } else {
                        true
                    }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TextFieldDatePreview() {
    TextFieldDate(
        title = "Fecha",
        onChangeValue = { },
        icon = androidx.compose.ui.res.painterResource(id = R.drawable.uikit_ic_arrow_down)
    )
}
