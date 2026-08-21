package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
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

@Composable
fun TextFieldMonth(
    modifier: Modifier = Modifier,
    title: String,
    defaultValue: String = "",
    onChangeValue: (String) -> Unit,
    icon: Painter,
    isDisabledMonthFuture: Boolean = false,
    enabled: Boolean = false,
    readOnly: Boolean = true
) {
    var showMonthPicker by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(defaultValue) }
    var selectedYear: Int by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var selectedMonth: Int by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH)) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM", Locale.getDefault()) }

    val onMonthSelected: (Int, Int) -> Unit = { year, month ->
        selectedYear = year
        selectedMonth = month
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
        }
        text = dateFormat.format(calendar.time)
        onChangeValue(text)
        showMonthPicker = false
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
                .clickable { showMonthPicker = true },
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

    if (showMonthPicker) {
        MonthPickerDialog(
            isDisabledMonthFuture = isDisabledMonthFuture,
            onDismissRequest = { showMonthPicker = false },
            onMonthSelected = onMonthSelected,
            initialYear = selectedYear,
            initialMonth = selectedMonth
        )
    }
}

@Composable
fun MonthPickerDialog(
    isDisabledMonthFuture: Boolean,
    onDismissRequest: () -> Unit,
    onMonthSelected: (Int, Int) -> Unit,
    initialYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    initialMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
) {
    var selectedYear: Int by remember { mutableIntStateOf(initialYear) }
    var selectedMonth: Int by remember { mutableIntStateOf(initialMonth) }
    val months = stringArrayResource(R.array.uikit_month_abbreviations)

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onMonthSelected(selectedYear, selectedMonth)
            }) {
                Text(stringResource(R.string.uikit_apply))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.uikit_cancel))
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Image(
                            painter = painterResource(id = R.drawable.uikit_ic_arrow_left),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            colorFilter = ColorFilter.tint(UiKitTheme.colors.primary),
                        )
                    }
                    Text(
                        text = selectedYear.toString(),
                        color = UiKitTheme.colors.primary,
                    )
                    IconButton(onClick = { selectedYear++ }) {
                        Image(
                            painter = painterResource(id = R.drawable.uikit_ic_arrow_right),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            colorFilter = ColorFilter.tint(UiKitTheme.colors.primary),
                        )
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    itemsIndexed(months) { index, month ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        index > initialMonth && isDisabledMonthFuture -> grayCard.copy(alpha = 0.8f)
                                        index == selectedMonth -> UiKitTheme.colors.primary.copy(alpha = 0.2f)
                                        else -> Color.Transparent
                                    }
                                )
                                .border(
                                    width = if (index == selectedMonth) 2.dp else 0.dp,
                                    color = if (index == selectedMonth) UiKitTheme.colors.primary else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    if (!(index > initialMonth && isDisabledMonthFuture)) {
                                        selectedMonth = index
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = month,
                                fontSize = 16.sp,
                                color = if (index == selectedMonth) UiKitTheme.colors.primary else black
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldMonthPreview() {
    TextFieldMonth(
        title = "Mes y Año",
        onChangeValue = { },
        icon = painterResource(id = R.drawable.uikit_ic_arrow_down)
    )
}

@Preview(showBackground = true)
@Composable
private fun MonthPickerDialogPreview() {
    MonthPickerDialog(
        isDisabledMonthFuture = false,
        onDismissRequest = { },
        onMonthSelected = { _, _ -> },
        initialYear = 2024,
        initialMonth = 0
    )
}
