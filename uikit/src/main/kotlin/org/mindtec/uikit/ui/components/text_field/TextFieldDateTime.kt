package org.mindtec.uikit.ui.components.text_field

import android.app.TimePickerDialog
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.UiKitTheme
import org.mindtec.uikit.utils.Fonts
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private const val DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldDateTime(
    modifier: Modifier = Modifier,
    title: String,
    defaultValue: String = "",
    onChangeValue: (String) -> Unit,
    icon: Painter,
    enabled: Boolean = false,
    readOnly: Boolean = true,
    dateTimeFormatPattern: String = DEFAULT_DATE_TIME_FORMAT,
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(defaultValue) }

    val dateTimeFormat = remember(dateTimeFormatPattern) { SimpleDateFormat(dateTimeFormatPattern, Locale.getDefault()) }

    val localCalendar = remember { Calendar.getInstance() }

    val onDateSelected: (Long) -> Unit = { selectedDateMillis ->
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = selectedDateMillis

        localCalendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        showDatePicker = false
        showTimePicker = true
    }

    val onTimeSelected: (Int, Int) -> Unit = { hour, minute ->
        localCalendar.set(Calendar.HOUR_OF_DAY, hour)
        localCalendar.set(Calendar.MINUTE, minute)
        localCalendar.set(Calendar.SECOND, 0)

        text = dateTimeFormat.format(localCalendar.time)
        onChangeValue(text)
        showTimePicker = false
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = Fonts.textRegular(16.sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker = true
                },
            value = text,
            onValueChange = {
                text = it
                onChangeValue(it)
            },
            enabled = enabled,
            readOnly = readOnly,
            leadingIcon = {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = icon,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(UiKitTheme.colors.primary)
                )
            }
        )
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val currentDateMillis = calendar.timeInMillis

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDateMillis
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

    if (showTimePicker) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                onTimeSelected(hourOfDay, minute)
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldDateTimePreview() {
    TextFieldDateTime(
        title = "Fecha y Hora",
        onChangeValue = { },
        icon = painterResource(id = R.drawable.uikit_ic_arrow_down)
    )
}
