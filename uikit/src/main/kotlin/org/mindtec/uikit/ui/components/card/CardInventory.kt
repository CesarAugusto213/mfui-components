package org.mindtec.uikit.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallIconButton
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun CardInventory(
    title: String,
    stock: String,
    subtitleText: String,
    onInputInventory: () -> Unit,
    onMoveInventory: () -> Unit,
    onAdjustmentInventory: () -> Unit,
    inputIcon: Painter = painterResource(R.drawable.uikit_ic_input_inventory),
    moveIcon: Painter = painterResource(R.drawable.uikit_ic_move_inventory),
    adjustmentIcon: Painter = painterResource(R.drawable.uikit_ic_adjustment_inventory)
) {
    val textArray = subtitleText.split("-")

    Card(border = BorderStroke(0.5.dp, grayPlaceholder)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(white)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = Fonts.textBold(16.sp, neutral700)
                )

                Card {
                    Box(
                        modifier = Modifier
                            .background(UiKitTheme.colors.primary)
                            .widthIn(min = 70.dp)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stock,
                            style = Fonts.textRegular(14.sp, white)
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = textArray.first().trim(),
                        style = Fonts.textRegular(14.sp, darkButton)
                    )

                    Text(
                        text = textArray.last().trim(),
                        style = Fonts.textRegular(14.sp, darkButton)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SmallIconButton(
                        icon = inputIcon,
                        background = white,
                        iconColor = neutral700,
                        stroke = grayPlaceholder,
                        onClick = { onInputInventory.invoke() }
                    )

                    SmallIconButton(
                        icon = moveIcon,
                        background = white,
                        iconColor = neutral700,
                        stroke = grayPlaceholder,
                        onClick = { onMoveInventory.invoke() }
                    )

                    SmallIconButton(
                        icon = adjustmentIcon,
                        background = white,
                        iconColor = neutral700,
                        stroke = grayPlaceholder,
                        onClick = { onAdjustmentInventory.invoke() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CardInventoryPreview() {
    CardInventory(
        title = "Product Name",
        stock = "1234.43",
        subtitleText = "Subtitle Text",
        onInputInventory = {},
        onMoveInventory = {},
        onAdjustmentInventory = {}
    )
}
