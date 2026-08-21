package org.mindtec.uikit.ui.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.tooling.preview.Preview
import org.mindtec.uikit.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun DataClient(
    modifier: Modifier = Modifier,
    userName: String,
    userAddress: String,
    onUserChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onEditUser: () -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = true
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        ),
        border = BorderStroke(.1.dp, gray)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = userName,
                onValueChange = { newValue ->
                    if (!readOnly) onUserChange(newValue)
                },
                enabled = false,
                readOnly = true,
                textStyle = Fonts.textRegular(15.sp)
            ) { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.uikit_ic_client),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 10.dp)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
                    IconButton(onClick = onEditUser) {
                        Icon(
                            painter = painterResource(id = R.drawable.uikit_ic_edit),
                            contentDescription = "Edit",
                            tint = purple300
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                thickness = 0.5.dp,
                color = gray.copy(alpha = 0.5f)
            )

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                value = userAddress,
                onValueChange = { newValue ->
                    onAddressChange(newValue)
                },
                enabled = enabled,
                readOnly = readOnly,
                textStyle =
                Fonts.textLight(11.sp)
            ) { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 4.dp)
                        .border(BorderStroke(0.dp, Color.Transparent)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        modifier = Modifier.size(14.dp),
                        painter = painterResource(id = R.drawable.uikit_ic_local),
                        contentDescription = null
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Transparent)
                    ) {
                        innerTextField()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DataClientPreview() {
    DataClient(
        userName = "Nombre del usuario",
        userAddress = "Benavides 247 - Miraflores",
        onUserChange = {},
        onAddressChange = {},
        onEditUser = {},
        enabled = true,
        readOnly = true
    )
}
