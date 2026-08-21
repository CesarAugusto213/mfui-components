package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun <T> ToggleSelector2(
    modifier: Modifier = Modifier,
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T) -> String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = label,
            style = Fonts.textRegular(16.sp)
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(neutral200),
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption

                Text(
                    text = optionLabel(option),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOptionSelected(option) }
                        .background(if (isSelected) UiKitTheme.colors.primary else neutral200)
                        .padding(vertical = 12.dp),
                    color = if (isSelected) white else black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun ToggleSelector2Preview() {
    val options = listOf("Opción 1", "Opción 2", "Opción 3")
    var selectedOption = options[0]

    ToggleSelector2(
        label = "Selecciona una opción",
        options = options,
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it },
        optionLabel = { it }
    )
}
