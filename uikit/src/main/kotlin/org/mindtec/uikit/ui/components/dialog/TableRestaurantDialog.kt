package org.mindtec.uikit.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.CloseButton
import org.mindtec.uikit.ui.components.TableRestaurantProductList
import org.mindtec.uikit.ui.components.button.ButtonMF
import org.mindtec.uikit.ui.components.model.UiKitProduct
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import java.math.BigDecimal

@Composable
fun TableRestaurantDialog(
    items: List<UiKitProduct>,
    icon: Painter? = null,
    titleButton: String = stringResource(R.string.uikit_add_product),
    onClickButton: () -> Unit,
    onDismissRequest: () -> Unit,
    onClickDelete: (String, Int?) -> Unit // itemId, commentIndex
) {
    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = white),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.uikit_table_detail_title),
                        style = Fonts.textRegular(14.sp, neutral800, TextAlign.Center)
                    )

                    TableRestaurantProductList(
                        modifier = Modifier.heightIn(max = 350.dp),
                        items = items,
                        onClickDelete = onClickDelete
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        if (items.isNotEmpty()) {
                            ButtonMF(
                                modifier = Modifier.height(50.dp),
                                text = titleButton,
                                color = UiKitTheme.colors.primary,
                                icon = icon,
                                onClick = {
                                    onClickButton.invoke()
                                }
                            )
                        } else {
                            ButtonMF(
                                modifier = Modifier.height(50.dp),
                                text = stringResource(R.string.uikit_close),
                                color = UiKitTheme.colors.primary,
                                onClick = {
                                    onDismissRequest.invoke()
                                }
                            )
                        }
                    }
                }

                CloseButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    action = onDismissRequest
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TableRestaurantDialogPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TableRestaurantDialog(
            items = listOf(
                UiKitProduct(
                    id = "1",
                    itemTypeId = "1",
                    description = "NOMBRE PRODUCTO",
                    quantity = BigDecimal.ONE,
                    unitPrice = BigDecimal("23")
                )
            ),
            onClickButton = {},
            onDismissRequest = {},
            onClickDelete = { _, _ -> }
        )
    }
}
