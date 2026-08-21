package org.mindtec.uikit.ui.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun CardListSimple(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    editIcon: Painter,
    deleteIcon: Painter? = null,
    options: Boolean = true,
    trailingText: String? = null,
    isActive: Boolean = true,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                modifier = Modifier.weight(3f),
                text = if (!subtitle.isNullOrEmpty()) "$title - $subtitle" else title,
                style = Fonts.textRegular(16.sp, if (isActive) black else red200)
            )

            if (options) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp)
                            .clickable { onEditClick?.invoke() },
                        painter = editIcon,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(black)
                    )

                    deleteIcon?.let {
                        Image(
                            modifier = Modifier
                                .size(25.dp)
                                .clickable { onDeleteClick?.invoke() },
                            painter = it,
                            contentDescription = null
                        )
                    }
                }
            } else if (trailingText != null) {
                Text(
                    text = trailingText,
                    style = Fonts.textRegular(16.sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardListSimplePreview() {
    CardListSimple(
        title = "Client Title",
        editIcon = painterResource(R.drawable.uikit_ic_edit),
        deleteIcon = painterResource(R.drawable.uikit_ic_delete)
    )
}

@Preview(showBackground = true)
@Composable
private fun CardListSimpleSubtitlePreview() {
    CardListSimple(
        title = "Client Title",
        subtitle = "Client Subtitle",
        editIcon = painterResource(R.drawable.uikit_ic_edit),
        deleteIcon = painterResource(R.drawable.uikit_ic_delete)
    )
}

@Preview(showBackground = true)
@Composable
private fun CardListSimpleOptionsPreview() {
    CardListSimple(
        title = "Client Title",
        editIcon = painterResource(R.drawable.uikit_ic_edit),
        options = false,
        trailingText = "Disponible: 10.00"
    )
}
