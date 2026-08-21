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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.ui.components.items.RadioButtonWithBorder
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun <T> SelectSmallMF(
    modifier: Modifier = Modifier,
    options: List<T>,
    selectedOption: String?,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    onClick: (() -> Unit)? = null
) {
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }
    var optionValue by remember { mutableStateOf(selectedOption) }

    LaunchedEffect(selectedOption) {
        optionValue = selectedOption
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = true },
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
                text = optionValue ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Fonts.textLight(11.sp)
            )
        }
    }

    if (expanded) {
        Dialog(onDismissRequest = { expanded = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = white,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .padding(16.dp)
                ) {
                    val containerHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
                    val thumbHeightPx = containerHeightPx * 0.15f
                    val maxScroll = scrollState.maxValue.toFloat().coerceAtLeast(1f)
                    val scrollProgress = scrollState.value / maxScroll
                    val thumbOffsetPx = (containerHeightPx - thumbHeightPx) * scrollProgress
                    val isAllContentDisplay = scrollState.maxValue == 0

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                            .padding(end = if (!isAllContentDisplay) 6.dp else 0.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        options.forEach { option ->
                            RadioButtonWithBorder(
                                modifier = Modifier.fillMaxWidth(),
                                label = optionLabel.invoke(option),
                                isSelected = optionValue == optionLabel.invoke(option),
                                onClick = {
                                    onOptionSelected(option)
                                    optionValue = optionLabel.invoke(option)
                                    expanded = false
                                    onClick?.invoke()
                                }
                            )
                        }
                    }

                    if (!isAllContentDisplay) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset { IntOffset(0, thumbOffsetPx.toInt()) }
                                .width(4.dp)
                                .height(with(LocalDensity.current) { thumbHeightPx.toDp() })
                                .background(purple300, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectSmallMFPreview() {
    SelectSmallMF(
        options = listOf("Option 1", "Option 2", "Option 3"),
        selectedOption = "Option 1",
        onOptionSelected = {},
        optionLabel = { it }
    )
}
