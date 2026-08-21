package org.mindtec.uikit.ui.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun SearchButton(
    modifier: Modifier = Modifier,
    textButton: String = stringResource(R.string.uikit_choose_your_client),
    iconButton: Painter? = painterResource(R.drawable.uikit_ic_client),
    colorButton: Color = white,
    colorText: Color = black,
    useBtn: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = colorText,
            containerColor = colorButton
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = { onClick.invoke() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                iconButton?.let {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = it,
                        contentDescription = null
                    )
                }
                Text(
                    text = textButton,
                    style = Fonts.textRegular(16.sp, colorText)
                )
            }

            if (useBtn) {
                Image(
                    painter = painterResource(id = R.drawable.uikit_ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    colorFilter = ColorFilter.tint(UiKitTheme.colors.primary),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchButtonPreview() {
    SearchButton(
        textButton = "Buscar Clientes",
        colorButton = white,
        colorText = black,
        useBtn = true,
        onClick = {}
    )
}
