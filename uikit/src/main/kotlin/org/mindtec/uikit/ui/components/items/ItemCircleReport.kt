package org.mindtec.uikit.ui.components.items

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.ui.components.model.UiKitCircleReportItem
import org.mindtec.uikit.ui.theme.*
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun ItemCircleReport(
    modifier: Modifier = Modifier,
    item: UiKitCircleReportItem
) {
    Column(modifier = modifier) {
        Text(
            text = item.title,
            style = TextStyle(
                color = neutral800,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )
        Spacer(modifier = Modifier.size(16.dp))
        CardCircleReport(item)
    }
}

@Composable
private fun CardCircleReport(item: UiKitCircleReportItem) {
    Card(
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 260.dp)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(
                    modifier = Modifier
                        .size(125.dp)
                        .padding(16.dp)
                ) {
                    val sweepAngle =
                        BigDecimal(360).multiply(item.percentage.divide(BigDecimal(100), 8, RoundingMode.HALF_UP))

                    drawArc(
                        color = item.secondaryColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 32.dp.toPx())
                    )
                    drawArc(
                        color = item.primaryColor,
                        startAngle = 270f,
                        sweepAngle = sweepAngle.toFloat(),
                        useCenter = false,
                        style = Stroke(width = 32.dp.toPx())
                    )
                    drawCircle(
                        color = shadowCircle,
                        radius = 30.dp.toPx(),
                        center = center,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
                Text(
                    text = "${item.percentage}%", style = TextStyle(
                        color = item.primaryColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Text(
                text = item.number, style = TextStyle(
                    color = item.primaryColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Column(horizontalAlignment = Alignment.Start) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(item.primaryColor)
                    )

                    Text(
                        text = item.primaryText,
                        color = item.primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(item.secondaryColor)
                    )

                    Text(
                        text = item.secondaryText,
                        color = item.secondaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemCircleReportPreview() {
    ItemCircleReport(
        item = UiKitCircleReportItem(
            title = "Cuentas por Cobrar",
            percentage = BigDecimal(80),
            primaryText = "Margen o utilidad bruta",
            primaryColor = red200,
            secondaryText = "Coste de producto",
            secondaryColor = pink400,
            number = "150"
        )
    )
}
