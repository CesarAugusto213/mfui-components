package org.mindtec.uikit.ui.components.card

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.sp
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    badgeColor: Color,
    badgeText: String,
    headerText: String,
    footerLeftText: String,
    footerRightText: String,
    onFooterLeftClick: () -> Unit,
    onFooterRightClick: () -> Unit,
    expandIcon: Painter = painterResource(R.drawable.uikit_ic_arrow_down),
    collapseIcon: Painter = painterResource(R.drawable.uikit_ic_arrow_up),
    content: @Composable () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    val backgroundColor = if (isExpanded) grayCard else white

    Column(
        modifier = modifier
            .border(
                width = .5.dp,
                color = grayPlaceholder,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = headerText,
                style = Fonts.textRegular(16.sp, gray)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(color = badgeColor, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badgeText,
                    style = Fonts.textRegular(14.sp, white)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    painter = if (isExpanded) collapseIcon else expandIcon,
                    contentDescription = null
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        if (isExpanded) {
            content()
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = footerLeftText,
                style = Fonts.textRegular(16.sp, blue300),
                modifier = Modifier.clickable(onClick = onFooterLeftClick)
            )
            Text(
                text = footerRightText,
                style = Fonts.textRegular(16.sp, blue300),
                modifier = Modifier.clickable(onClick = onFooterRightClick)
            )
        }
    }
}

@Composable
fun InfoRow(
    leftText: String,
    rightText: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(1f),
            text = leftText,
            style = Fonts.textRegular(16.sp, grayInactive)
        )
        Text(
            modifier = Modifier.weight(1f),
            text = rightText,
            style = Fonts.textRegular(fontSize = 16.sp, textAlign = TextAlign.End)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardListPreview() {
    CardList(
        badgeColor = red200,
        badgeText = "Cerrado",
        headerText = "Estado",
        footerLeftText = "Acción 1",
        footerRightText = "Acción 2",
        onFooterLeftClick = { },
        onFooterRightClick = { }
    ) {
        Column {
            InfoRow(leftText = "Abrio caja", rightText = "Julio Martinez")
            InfoRow(leftText = "Cerro caja", rightText = "Julio Martinez")
            InfoRow(leftText = "Fecha", rightText = "12/12/2021")
            InfoRow(leftText = "Hora", rightText = "12:00")
        }
    }
}
