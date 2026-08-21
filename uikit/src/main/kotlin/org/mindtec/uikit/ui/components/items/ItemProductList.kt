package org.mindtec.uikit.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.model.UiKitProduct
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import org.mindtec.uikit.utils.restrictDigits

@Composable
fun ItemProductList(
    item: UiKitProduct,
    onClickDelete: () -> Unit,
    disableTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = white),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.description.uppercase(),
                style = Fonts.textRegular(14.sp, neutral700)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Cant: ${item.quantity}",
                style = Fonts.textLight(12.sp, neutral800)
            )
        }

        if (!disableTotal) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "S/ ${item.unitPrice.multiply(item.quantity).restrictDigits()}",
                    style = Fonts.textRegular(14.sp, neutral700)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Precio U. S/ ${item.unitPrice.restrictDigits()}",
                    style = Fonts.textLight(12.sp, neutral800)
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.uikit_ic_delete),
            contentDescription = "Eliminar",
            colorFilter = ColorFilter.tint(Color.Red),
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = {
                    onClickDelete.invoke()
                })
        )
    }
}
