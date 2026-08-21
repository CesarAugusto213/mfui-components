package org.mindtec.uikit.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.model.UiKitDashboardCard
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun ItemReportDashboard(item: UiKitDashboardCard) {
    Card(
        colors = CardDefaults.cardColors(containerColor = blue300),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconReportDashboard(item.icon)

                item.trailingIcon?.let {
                    IconIncomeIndicator(it)
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

            Text(
                text = item.title,
                style = Fonts.textRegular(16.sp, white)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = item.value,
                style = Fonts.textRegular(16.sp, white)
            )
        }
    }
}

@Composable
private fun IconReportDashboard(icon: Painter) {
    Card(
        colors = CardDefaults.cardColors(containerColor = blue200),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(white)
            )
        }
    }
}

@Composable
private fun IconIncomeIndicator(icon: Painter) {
    Box(
        modifier = Modifier
            .height(28.dp)
            .width(62.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 0.dp,
                    topEnd = 8.dp
                )
            )
            .background(neutral100),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = icon,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ItemReportDashboardPreview() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(4) {
            ItemReportDashboard(
                UiKitDashboardCard(
                    icon = painterResource(R.drawable.uikit_ic_image),
                    trailingIcon = painterResource(R.drawable.uikit_ic_image),
                    title = "Boletas y Facturas",
                    value = "s/ 1400"
                )
            )
        }
    }
}
