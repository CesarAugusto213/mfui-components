package org.mindtec.uikit.ui.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.UiKitTheme
import org.mindtec.uikit.utils.Fonts

@Composable
fun RadioButtonWithBorder(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    label: String,
    secondLabel: String? = null,
    textSize: TextUnit = 16.sp
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (isSelected) UiKitTheme.colors.primary else Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(
                    width = 2.dp,
                    color = if (isSelected) UiKitTheme.colors.primary else Color.LightGray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = UiKitTheme.colors.primary,
                            shape = CircleShape
                        )
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = label,
                style = Fonts.textRegular(
                    fontSize = textSize,
                    color = if (isSelected) UiKitTheme.colors.primary else Color.DarkGray
                )
            )

            secondLabel?.let {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = it,
                    style = Fonts.textLight(
                        fontSize = 10.sp,
                        color = if (isSelected) UiKitTheme.colors.primary else Color.DarkGray
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRadioButtonWithBorder() {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(3) { index ->
            RadioButtonWithBorder(
                modifier = Modifier
                    .padding(bottom = 1.dp)
                    .fillMaxWidth(),
                isSelected = selectedIndex == index,
                onClick = { selectedIndex = if (selectedIndex == index) null else index },
                label = "Item ${index + 1}"
            )
        }
    }
}
