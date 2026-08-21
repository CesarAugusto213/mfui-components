package org.mindtec.uikit.ui.components.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun SelectableCard(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) purple100 else white
        ),
        border = BorderStroke(
            1.dp,
            if (isSelected) purple300 else neutral400
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = Fonts.textLight(16.sp, neutral800)
            )
            Text(
                text = subTitle,
                style = Fonts.textLight(14.sp, neutral800)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectableListPreview() {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(4) { index ->
            SelectableCard(
                title = "Cliente EIRL",
                subTitle = "1234567890",
                isSelected = selectedIndex == index,
                onClick = {
                    selectedIndex = if (selectedIndex == index) null else index
                }
            )
        }
    }
}
