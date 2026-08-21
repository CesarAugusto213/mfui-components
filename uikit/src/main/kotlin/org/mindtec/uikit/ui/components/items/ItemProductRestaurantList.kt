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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.model.UiKitComment
import org.mindtec.uikit.ui.components.model.UiKitProduct
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import org.mindtec.uikit.utils.restrictDigits
import java.math.BigDecimal

@Composable
fun ItemProductRestaurantList(
    item: UiKitProduct,
    comments: List<UiKitComment> = emptyList(),
    showQuantity: Boolean = true,
    commentIndex: Int? = null, // Índice del comentario (null si no hay comentarios)
    onClickDelete: (String, Int?) -> Unit // itemId, commentIndex
) {
    val quantity = if (showQuantity) item.quantity else BigDecimal.ONE
    val subtotal = item.unitPrice.multiply(quantity)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Fila principal del producto
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    text = item.description,
                    style = Fonts.textRegular(14.sp, neutral700)
                )
                if (showQuantity && quantity > BigDecimal.ONE) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cant: ${quantity.restrictDigits()}",
                        style = Fonts.textLight(12.sp, neutral800)
                    )
                }
            }

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                text = "S/ ${subtotal.restrictDigits()}",
                style = Fonts.textRegular(14.sp, neutral700, TextAlign.End)
            )

            Image(
                painter = painterResource(R.drawable.uikit_ic_delete),
                contentDescription = "Eliminar",
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onClickDelete.invoke(item.id, commentIndex)
                    }
            )
        }

        // Mostrar comentarios si los hay
        if (comments.isNotEmpty()) {
            val commentsText = comments.joinToString { it.description }

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "Indicaciones: $commentsText",
                style = Fonts.textRegular(12.sp, neutral400)
            )
        }
    }
}
