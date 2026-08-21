package org.mindtec.uikit.ui.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
fun CardListClient(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    editIcon: Painter,
    deleteIcon: Painter,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderGrayCard,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = title,
                    style = Fonts.textRegular(16.sp)
                )

                Text(
                    text = subtitle,
                    style = Fonts.textRegular(14.sp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    modifier = Modifier.scale(.9f),
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = blue300
                    )
                )

                Spacer(modifier = Modifier.width(32.dp))

                Image(
                    painter = editIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 8.dp)
                        .clickable { onEditClick?.invoke() },
                    colorFilter = ColorFilter.tint(black)
                )

                Image(
                    painter = deleteIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { onDeleteClick?.invoke() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardListClientPreview() {
    CardListClient(
        title = "Client Title",
        subtitle = "Client Subtitle",
        checked = false,
        onCheckedChange = {},
        editIcon = painterResource(R.drawable.uikit_ic_edit),
        deleteIcon = painterResource(R.drawable.uikit_ic_delete),
        onEditClick = { },
        onDeleteClick = { }
    )
}
