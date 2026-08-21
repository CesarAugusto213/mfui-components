package org.mindtec.uikit.ui.components.select

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.items.ItemEmptyList
import org.mindtec.uikit.ui.components.items.RadioButtonWithBorder
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun <T> SelectMF(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    options: List<T>,
    selectedOption: String?,
    enabled: Boolean = true,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    onClick: (() -> Unit)? = null,
    searchPlaceholder: String = stringResource(R.string.uikit_search_placeholder)
) {
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }
    var optionValue by remember { mutableStateOf(selectedOption) }
    var searchText by remember { mutableStateOf("") }

    val searchEnabled = options.size > 5

    val filteredOptions = if (searchEnabled && searchText.isNotBlank()) {
        options.filter { option ->
            optionLabel(option).contains(searchText, ignoreCase = true)
        }
    } else {
        options
    }

    LaunchedEffect(selectedOption) {
        optionValue = selectedOption
    }

    LaunchedEffect(expanded) {
        if (!expanded) {
            searchText = ""
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = Fonts.textRegular(16.sp)
            )

            description?.let {
                Text(
                    text = description,
                    style = Fonts.textRegular(12.sp, grayPlaceholder)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (enabled) {
                        expanded = true
                    }
                },
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            border = BorderStroke(width = 1.dp, color = grayTextField)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = optionValue ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Fonts.textLight(18.sp)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(24.dp)
                        .width(1.dp)
                        .background(grayTextField)
                )
                Icon(
                    painter = painterResource(id = R.drawable.uikit_ic_arrow_down),
                    contentDescription = null,
                    tint = black
                )
            }
        }

        if (expanded) {
            Dialog(onDismissRequest = {
                expanded = false
                searchText = "" // Limpiar búsqueda al cerrar
            }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = white),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Buscador (solo si está habilitado)
                        if (searchEnabled) {
                            SearchSection(
                                value = searchText,
                                placeholder = searchPlaceholder,
                                onValueChange = { searchText = it },
                                onClear = { searchText = "" }
                            )
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = white,
                            tonalElevation = 8.dp
                        ) {
                            BoxWithConstraints(
                                modifier = Modifier.heightIn(max = 400.dp)
                            ) {
                                val containerHeightPx =
                                    with(LocalDensity.current) { maxHeight.toPx() }
                                val thumbHeightPx = containerHeightPx * 0.15f
                                val maxScroll = scrollState.maxValue.toFloat().coerceAtLeast(1f)
                                val scrollProgress = scrollState.value / maxScroll
                                val thumbOffsetPx =
                                    (containerHeightPx - thumbHeightPx) * scrollProgress
                                val isAllContentDisplay = scrollState.maxValue == 0

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .verticalScroll(scrollState)
                                        .padding(end = if (!isAllContentDisplay) 6.dp else 0.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (filteredOptions.isEmpty() && searchEnabled && searchText.isNotBlank()) {
                                        ItemEmptyList()
                                    } else {
                                        filteredOptions.forEach { option ->
                                            RadioButtonWithBorder(
                                                modifier = Modifier.fillMaxWidth(),
                                                label = optionLabel.invoke(option),
                                                isSelected = optionValue == optionLabel.invoke(
                                                    option
                                                ),
                                                onClick = {
                                                    onOptionSelected(option)
                                                    optionValue = optionLabel.invoke(option)
                                                    expanded = false
                                                    searchText = ""
                                                    onClick?.invoke()
                                                }
                                            )
                                        }
                                    }
                                }

                                if (!isAllContentDisplay) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset { IntOffset(0, thumbOffsetPx.toInt()) }
                                            .width(4.dp)
                                            .height(with(LocalDensity.current) { thumbHeightPx.toDp() })
                                            .background(UiKitTheme.colors.primary, RoundedCornerShape(2.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchSection(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = Fonts.textRegular(14.sp, grayPlaceholder)
            )
        },
        singleLine = true,
        suffix = {
            Card(
                modifier = Modifier.clickable {
                    onClear.invoke()
                },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = grayTextField
                )
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.Clear,
                    tint = white,
                    contentDescription = "Clear"
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { /* El filtrado es automático */ }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectMFPreview() {
    SelectMF(
        title = "Select",
        options = listOf("Opción 1", "Opción 2", "Opción 3"),
        selectedOption = "Opción 1",
        onOptionSelected = {},
        optionLabel = { it }
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectMFWithSearchPreview() {
    SelectMF(
        title = "Select con búsqueda",
        options = listOf("ABC123", "XYZ567", "BNM902", "F5T787", "123456", "ASDASD", "A01125"),
        selectedOption = "ABC123",
        onOptionSelected = {},
        optionLabel = { it },
        searchPlaceholder = "Buscar código..."
    )
}
