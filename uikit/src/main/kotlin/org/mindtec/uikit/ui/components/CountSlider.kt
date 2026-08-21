package org.mindtec.uikit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import kotlin.math.roundToInt

@Composable
fun CountSlider(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    valueRange: IntRange = 1..50,
    onValueChange: (Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = Fonts.textRegular(16.sp),
            )

            Text(
                text = value.toString(),
                style = Fonts.textRegular(16.sp, UiKitTheme.colors.primaryPressed)
            )
        }

        Card(
            modifier = Modifier.height(56.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            border = BorderStroke(1.dp, grayIndicator),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Slider(
                    value = value.toFloat(),
                    onValueChange = { newValue ->
                        onValueChange(newValue.roundToInt())
                    },
                    valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
                    steps = (valueRange.last - valueRange.first - 1).coerceAtLeast(0),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CountSliderPreview() {
    CountSlider(
        label = "Cantidad de mesas:",
        value = 10,
        onValueChange = {}
    )
}
