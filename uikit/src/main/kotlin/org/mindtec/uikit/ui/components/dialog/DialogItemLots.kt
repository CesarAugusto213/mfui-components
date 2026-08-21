package org.mindtec.uikit.ui.components.dialog

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.CloseButton
import org.mindtec.uikit.ui.components.WidgetCounter
import org.mindtec.uikit.ui.components.button.ButtonMF
import org.mindtec.uikit.ui.components.button.OutlinedButtonMF
import org.mindtec.uikit.ui.components.items.ItemEmptyList
import org.mindtec.uikit.ui.components.items.RadioButtonWithBorder
import org.mindtec.uikit.ui.components.model.UiKitLotGroup
import org.mindtec.uikit.ui.components.model.UiKitSeries
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.shape.CircleShape
import java.math.BigDecimal

/**
 * Representa la estructura jerárquica de lotes y series para la UI, para
 * el modo [LotsSelectionType.LOTS_WITH_SERIES].
 */
data class LotWithSeries(
    val lot: UiKitLotGroup,
    val series: List<UiKitSeries>,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = false,
    var selectedSeries: List<UiKitSeries> = emptyList()
) {
    val totalSeries: Int get() = series.size
    val selectedCount: Int get() = selectedSeries.size
    val hasFullSelection: Boolean get() = selectedCount == totalSeries && totalSeries > 0
}

/**
 * Resultado de la selección del usuario.
 */
data class LotsSelectionResult(
    val selectedSeries: List<UiKitSeries> = emptyList(),
    val selectedLots: List<UiKitLotGroup> = emptyList(),
    val selectionType: LotsSelectionType
) {
    val totalQuantity: Int get() = when (selectionType) {
        LotsSelectionType.SERIES_ONLY, LotsSelectionType.LOTS_WITH_SERIES -> selectedSeries.size
        LotsSelectionType.LOTS_ONLY -> selectedLots.sumOf { it.selectedQuantity }
    }
}

/**
 * Tipos de selección disponibles en el diálogo.
 */
enum class LotsSelectionType {
    SERIES_ONLY,        // Solo series
    LOTS_ONLY,          // Solo lotes
    LOTS_WITH_SERIES    // Lotes con series
}

/**
 * Selector de lotes/series de un producto, con 3 modos según cómo el
 * producto rastrea su stock. A diferencia del original (que recibía y
 * devolvía un `ItemProductsUiState` completo del producto), acá el
 * diálogo solo se ocupa de la selección misma — la app ya sabe para qué
 * producto lo abrió, así que [onConfirmation]/[onDismissRequest] no
 * necesitan recibirlo de vuelta.
 *
 * @param initialSelectedSeries Series ya elegidas previamente (ej. al
 * reabrir el diálogo para editar una selección).
 * @param initialSelectedLots Lotes ya elegidos previamente.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DialogItemLots(
    show: Boolean,
    selectionType: LotsSelectionType,
    itemLots: List<UiKitSeries>,
    lotsGroup: List<UiKitLotGroup>,
    lotsWithSeries: List<LotWithSeries>,
    initialSelectedSeries: List<UiKitSeries> = emptyList(),
    initialSelectedLots: List<UiKitLotGroup> = emptyList(),
    onConfirmation: (LotsSelectionResult) -> Unit,
    onDismissRequest: () -> Unit
) {
    val scrollState = rememberScrollState()
    var searchText by remember { mutableStateOf("") }

    if (show) {
        var selectedSeries by remember { mutableStateOf(initialSelectedSeries) }
        var selectedLots by remember { mutableStateOf(initialSelectedLots) }
        var expandedLots by remember { mutableStateOf(setOf<Int>()) }
        var lotsWithSeriesState by remember(lotsWithSeries) {
            mutableStateOf(
                lotsWithSeries.map { lotWithSeries ->
                    val previouslySelectedSeries = selectedSeries.filter { series ->
                        lotWithSeries.series.any { it.id == series.id }
                    }
                    val previouslySelectedLot = selectedLots.find { it.id == lotWithSeries.lot.id }
                    val isLotSelected = previouslySelectedLot != null &&
                            previouslySelectedSeries.size == lotWithSeries.series.size

                    lotWithSeries.copy(
                        selectedSeries = previouslySelectedSeries,
                        isSelected = isLotSelected
                    )
                }
            )
        }

        val searchEnabled = when (selectionType) {
            LotsSelectionType.SERIES_ONLY -> itemLots.size > 5
            LotsSelectionType.LOTS_ONLY -> lotsGroup.size > 5
            LotsSelectionType.LOTS_WITH_SERIES -> lotsWithSeries.size > 3 ||
                    lotsWithSeries.sumOf { it.series.size } > 5
        }

        Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
            Card {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(white)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (selectionType) {
                                LotsSelectionType.SERIES_ONLY -> stringResource(R.string.uikit_select_series)
                                LotsSelectionType.LOTS_ONLY -> stringResource(R.string.uikit_select_lots)
                                LotsSelectionType.LOTS_WITH_SERIES -> stringResource(R.string.uikit_select_lots_and_series)
                            },
                            style = Fonts.textBold(16.sp)
                        )

                        if (searchEnabled) {
                            LotsSearchField(
                                value = searchText,
                                placeholder = stringResource(R.string.uikit_search_placeholder),
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
                                val scrollProgress = if (maxScroll > 0) scrollState.value / maxScroll else 0f
                                val thumbOffsetPx =
                                    (containerHeightPx - thumbHeightPx) * scrollProgress
                                val isAllContentDisplay = scrollState.maxValue == 0

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .verticalScroll(scrollState)
                                        .padding(end = if (!isAllContentDisplay) 6.dp else 0.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    when (selectionType) {
                                        LotsSelectionType.SERIES_ONLY -> {
                                            SeriesOnlyContent(
                                                series = itemLots,
                                                searchText = searchText,
                                                selectedSeries = selectedSeries,
                                                onSeriesSelectionChanged = {
                                                    selectedSeries = it
                                                }
                                            )
                                        }

                                        LotsSelectionType.LOTS_ONLY -> {
                                            LotsOnlyContent(
                                                lots = lotsGroup,
                                                searchText = searchText,
                                                selectedLots = selectedLots,
                                                onLotsQuantityChanged = {
                                                    selectedLots = it
                                                }
                                            )
                                        }

                                        LotsSelectionType.LOTS_WITH_SERIES -> {
                                            LotsWithSeriesContent(
                                                lotsWithSeries = lotsWithSeriesState,
                                                searchText = searchText,
                                                expandedLots = expandedLots,
                                                onExpandToggle = { lotId ->
                                                    expandedLots =
                                                        if (expandedLots.contains(lotId)) {
                                                            expandedLots - lotId
                                                        } else {
                                                            expandedLots + lotId
                                                        }
                                                },
                                                onLotSelectionChanged = { lotId, isSelected ->
                                                    lotsWithSeriesState =
                                                        lotsWithSeriesState.map { lotWithSeries ->
                                                            if (lotWithSeries.lot.id == lotId) {
                                                                if (isSelected) {
                                                                    selectedSeries = selectedSeries + lotWithSeries.series

                                                                    val lotToAdd = lotWithSeries.lot.copy(selectedQuantity = lotWithSeries.series.size)
                                                                    selectedLots = selectedLots.filter { it.id != lotId } + lotToAdd

                                                                    lotWithSeries.copy(
                                                                        isSelected = true,
                                                                        selectedSeries = lotWithSeries.series
                                                                    )
                                                                } else {
                                                                    selectedSeries = selectedSeries - lotWithSeries.series.toSet()

                                                                    selectedLots = selectedLots.filter { it.id != lotId }

                                                                    lotWithSeries.copy(
                                                                        isSelected = false,
                                                                        selectedSeries = emptyList()
                                                                    )
                                                                }
                                                            } else lotWithSeries
                                                        }
                                                },
                                                onSeriesSelectionChanged = { lotId, series, isSelected ->
                                                    selectedSeries = if (isSelected) {
                                                        selectedSeries + series
                                                    } else {
                                                        selectedSeries - series
                                                    }

                                                    lotsWithSeriesState =
                                                        lotsWithSeriesState.map { lotWithSeries ->
                                                            if (lotWithSeries.lot.id == lotId) {
                                                                val newSelectedSeries =
                                                                    if (isSelected) {
                                                                        lotWithSeries.selectedSeries + series
                                                                    } else {
                                                                        lotWithSeries.selectedSeries - series
                                                                    }
                                                                val isLotFullySelected =
                                                                    newSelectedSeries.size == lotWithSeries.series.size

                                                                if (newSelectedSeries.isNotEmpty()) {
                                                                    val lotToUpdate = lotWithSeries.lot.copy(selectedQuantity = newSelectedSeries.size)
                                                                    selectedLots = selectedLots.filter { it.id != lotId } + lotToUpdate
                                                                } else {
                                                                    selectedLots = selectedLots.filter { it.id != lotId }
                                                                }

                                                                lotWithSeries.copy(
                                                                    selectedSeries = newSelectedSeries,
                                                                    isSelected = isLotFullySelected
                                                                )
                                                            } else lotWithSeries
                                                        }
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

                        val totalSelected = when (selectionType) {
                            LotsSelectionType.SERIES_ONLY, LotsSelectionType.LOTS_WITH_SERIES -> selectedSeries.size
                            LotsSelectionType.LOTS_ONLY -> selectedLots.sumOf { it.selectedQuantity }
                        }

                        Text(
                            text = when (selectionType) {
                                LotsSelectionType.SERIES_ONLY, LotsSelectionType.LOTS_WITH_SERIES ->
                                    stringResource(R.string.uikit_selected_series, totalSelected)
                                LotsSelectionType.LOTS_ONLY ->
                                    stringResource(R.string.uikit_selected_quantity, totalSelected)
                            },
                            style = Fonts.textRegular(14.sp, UiKitTheme.colors.primary)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButtonMF(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.uikit_cancel),
                                onClick = { onDismissRequest.invoke() }
                            )

                            ButtonMF(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.uikit_confirm),
                                color = UiKitTheme.colors.primary,
                                enabled = totalSelected > 0,
                                onClick = {
                                    onConfirmation(
                                        LotsSelectionResult(
                                            selectedSeries = selectedSeries,
                                            selectedLots = selectedLots,
                                            selectionType = selectionType
                                        )
                                    )
                                }
                            )
                        }
                    }

                    CloseButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        action = { onDismissRequest.invoke() }
                    )
                }
            }
        }
    }
}

@Composable
private fun LotsSearchField(
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
            Box(
                modifier = Modifier
                    .clickable { onClear.invoke() }
                    .background(grayTextField, CircleShape)
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp),
                    imageVector = Icons.Default.Clear,
                    tint = white,
                    contentDescription = "Clear"
                )
            }
        }
    )
}

@Composable
private fun SeriesOnlyContent(
    series: List<UiKitSeries>,
    searchText: String,
    selectedSeries: List<UiKitSeries>,
    onSeriesSelectionChanged: (List<UiKitSeries>) -> Unit
) {
    val filteredSeries = if (searchText.isNotBlank()) {
        series.filter { it.series.contains(searchText, ignoreCase = true) }
    } else {
        series
    }

    if (filteredSeries.isEmpty()) {
        ItemEmptyList()
    } else {
        filteredSeries.forEach { seriesItem ->
            RadioButtonWithBorder(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.uikit_series_label, seriesItem.series),
                isSelected = selectedSeries.contains(seriesItem),
                onClick = {
                    val newSelection = if (selectedSeries.contains(seriesItem)) {
                        selectedSeries - seriesItem
                    } else {
                        selectedSeries + seriesItem
                    }
                    onSeriesSelectionChanged(newSelection)
                }
            )
        }
    }
}

@Composable
private fun LotsOnlyContent(
    lots: List<UiKitLotGroup>,
    searchText: String,
    selectedLots: List<UiKitLotGroup>,
    onLotsQuantityChanged: (List<UiKitLotGroup>) -> Unit
) {
    val filteredLots = if (searchText.isNotBlank()) {
        lots.filter { it.code.contains(searchText, ignoreCase = true) }
    } else {
        lots
    }

    if (filteredLots.isEmpty()) {
        ItemEmptyList()
    } else {
        filteredLots.forEach { lotGroup ->
            val currentLot = selectedLots.find { it.id == lotGroup.id }
            val currentQuantity = currentLot?.selectedQuantity ?: 0

            LotQuantitySelector(
                lotsGroup = lotGroup.copy(selectedQuantity = currentQuantity),
                onQuantityChanged = { newQuantity ->
                    val newSelection = selectedLots.toMutableList()
                    val existingIndex = newSelection.indexOfFirst { it.id == lotGroup.id }

                    if (newQuantity > 0) {
                        val updatedLot = lotGroup.copy(selectedQuantity = newQuantity)
                        if (existingIndex != -1) {
                            newSelection[existingIndex] = updatedLot
                        } else {
                            newSelection.add(updatedLot)
                        }
                    } else {
                        if (existingIndex != -1) {
                            newSelection.removeAt(existingIndex)
                        }
                    }

                    onLotsQuantityChanged(newSelection)
                }
            )
        }
    }
}

@Composable
private fun LotsWithSeriesContent(
    lotsWithSeries: List<LotWithSeries>,
    searchText: String,
    expandedLots: Set<Int>,
    onExpandToggle: (Int) -> Unit,
    onLotSelectionChanged: (Int, Boolean) -> Unit,
    onSeriesSelectionChanged: (Int, UiKitSeries, Boolean) -> Unit
) {
    val filteredLotsWithSeries = if (searchText.isNotBlank()) {
        lotsWithSeries.mapNotNull { lotWithSeries ->
            val lotMatches = lotWithSeries.lot.code.contains(searchText, ignoreCase = true)
            val filteredSeries = lotWithSeries.series.filter {
                it.series.contains(searchText, ignoreCase = true)
            }

            if (lotMatches) {
                lotWithSeries
            } else if (filteredSeries.isNotEmpty()) {
                lotWithSeries.copy(series = filteredSeries)
            } else {
                null
            }
        }
    } else {
        lotsWithSeries
    }

    if (filteredLotsWithSeries.isEmpty()) {
        ItemEmptyList()
        return
    }

    filteredLotsWithSeries.forEach { lotWithSeries ->
        val lot = lotWithSeries.lot
        val isExpanded = expandedLots.contains(lot.id)
        val hasFullSelection = lotWithSeries.hasFullSelection

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandToggle(lot.id) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.uikit_lot_label, lot.code),
                    style = Fonts.textBold(16.sp)
                )
                Text(
                    text = stringResource(
                        R.string.uikit_lot_series_summary,
                        lotWithSeries.totalSeries,
                        lotWithSeries.selectedCount
                    ),
                    style = Fonts.textRegular(12.sp, gray)
                )
                Text(
                    text = stringResource(R.string.uikit_lot_due_date, lot.dateOfDue),
                    style = Fonts.textRegular(12.sp, gray)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = hasFullSelection,
                    onCheckedChange = { isChecked ->
                        onLotSelectionChanged(lot.id, isChecked)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = UiKitTheme.colors.primary,
                        uncheckedColor = gray,
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = if (isExpanded) "▼" else "▲",
                    style = Fonts.textRegular(16.sp, UiKitTheme.colors.primary),
                    modifier = Modifier.clickable { onExpandToggle(lot.id) }
                )
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                lotWithSeries.series.forEach { series ->
                    val isSeriesSelected = lotWithSeries.selectedSeries.contains(series)
                    RadioButtonWithBorder(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.uikit_series_label, series.series),
                        secondLabel = stringResource(R.string.uikit_series_date, series.date),
                        isSelected = isSeriesSelected,
                        onClick = {
                            onSeriesSelectionChanged(lot.id, series, !isSeriesSelected)
                        }
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = gray.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun LotQuantitySelector(
    lotsGroup: UiKitLotGroup,
    onQuantityChanged: (Int) -> Unit
) {
    val selectedQuantity = lotsGroup.selectedQuantity

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = if (selectedQuantity > 0) UiKitTheme.colors.primary.copy(alpha = 0.1f) else Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = if (selectedQuantity > 0) UiKitTheme.colors.primary else Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(R.string.uikit_lot_label, lotsGroup.code),
            style = Fonts.textBold(16.sp, if (selectedQuantity > 0) UiKitTheme.colors.primary else Color.DarkGray)
        )
        Text(
            text = stringResource(R.string.uikit_lot_available_summary, lotsGroup.quantity.toString(), lotsGroup.dateOfDue),
            style = Fonts.textRegular(12.sp, Color.Gray)
        )

        Spacer(modifier = Modifier.height(8.dp))

        WidgetCounter(
            enabled = true,
            height = 38.dp,
            quantity = BigDecimal(selectedQuantity),
            stock = lotsGroup.quantity,
            stockControl = true,
            isZero = true,
            showInputDialog = { /* No necesario para este caso */ },
            onClickedMinus = { newQuantity ->
                onQuantityChanged.invoke(newQuantity.toInt())
            },
            onClickedPlus = { newQuantity ->
                onQuantityChanged.invoke(newQuantity.toInt())
            }
        )
    }
}

// ==================== PREVIEWS DEL DIÁLOGO COMPLETO ====================
@Preview(name = "Diálogo - Solo Series", showBackground = true)
@Composable
private fun PreviewDialogSeriesOnly() {
    val mockSeries = listOf(
        UiKitSeries(id = 1, series = "SERIE-001-ABC", date = "2026-01-05"),
        UiKitSeries(id = 2, series = "SERIE-002-DEF", date = "2026-01-06"),
        UiKitSeries(id = 3, series = "SERIE-003-GHI", date = "2026-01-07"),
        UiKitSeries(id = 4, series = "SERIE-004-JKL", date = "2026-01-08")
    )

    DialogItemLots(
        show = true,
        selectionType = LotsSelectionType.SERIES_ONLY,
        itemLots = mockSeries,
        lotsGroup = emptyList(),
        lotsWithSeries = emptyList(),
        onConfirmation = { },
        onDismissRequest = { }
    )
}

@Preview(name = "Diálogo - Solo Lotes", showBackground = true)
@Composable
private fun PreviewDialogLotsOnly() {
    val mockLots = listOf(
        UiKitLotGroup(id = 47, code = "LOTE-A", quantity = BigDecimal("11.0000"), dateOfDue = "2026-05-23"),
        UiKitLotGroup(id = 48, code = "LOTE-B", quantity = BigDecimal("25.0000"), dateOfDue = "2026-04-30", selectedQuantity = 2),
        UiKitLotGroup(id = 49, code = "LOTE-C", quantity = BigDecimal("8.0000"), dateOfDue = "2026-06-18")
    )

    DialogItemLots(
        show = true,
        selectionType = LotsSelectionType.LOTS_ONLY,
        itemLots = emptyList(),
        lotsGroup = mockLots,
        lotsWithSeries = emptyList(),
        onConfirmation = { },
        onDismissRequest = { }
    )
}

@Preview(name = "Diálogo - Lotes con Series", showBackground = true)
@Composable
private fun PreviewDialogLotsWithSeries() {
    val mockLotsWithSeries = listOf(
        LotWithSeries(
            lot = UiKitLotGroup(id = 52, code = "LOTE-PRINCIPAL", quantity = BigDecimal("5.0000"), dateOfDue = "2026-08-27"),
            series = listOf(
                UiKitSeries(id = 99, series = "LP-SERIE-001", date = "2026-01-05"),
                UiKitSeries(id = 100, series = "LP-SERIE-002", date = "2026-01-05"),
                UiKitSeries(id = 101, series = "LP-SERIE-003", date = "2026-01-05")
            ),
            isExpanded = true
        ),
        LotWithSeries(
            lot = UiKitLotGroup(id = 47, code = "LOTE-SECUNDARIO", quantity = BigDecimal("3.0000"), dateOfDue = "2026-05-23"),
            series = listOf(
                UiKitSeries(id = 85, series = "LS-SERIE-001", date = "2026-01-05"),
                UiKitSeries(id = 86, series = "LS-SERIE-002", date = "2026-01-05")
            )
        )
    )

    DialogItemLots(
        show = true,
        selectionType = LotsSelectionType.LOTS_WITH_SERIES,
        itemLots = emptyList(),
        lotsGroup = emptyList(),
        lotsWithSeries = mockLotsWithSeries,
        onConfirmation = { },
        onDismissRequest = { }
    )
}
