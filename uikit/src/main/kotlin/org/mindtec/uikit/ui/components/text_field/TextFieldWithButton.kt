package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun TextFieldWithButton(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: Painter,
    onChangeValue: (String) -> Unit,
    onClickIcon: () -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    value: String? = null,
    showError: Boolean = false
) {
    var text by remember { mutableStateOf(value ?: "") }

    LaunchedEffect(value) {
        text = value ?: ""
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = Fonts.textRegular(fontSize = 16.sp)
            )

            description?.let {
                Text(
                    text = description,
                    style = Fonts.textRegular(
                        fontSize = 12.sp,
                        color = grayPlaceholder
                    )
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white),
                value = text,
                onValueChange = {
                    text = it
                    onChangeValue.invoke(it)
                },
                singleLine = true,
                enabled = enabled,
                readOnly = readOnly
            )

            CustomStyledButton(icon, onClickIcon)
        }
        if (showError) {
            Text(
                text = stringResource(R.string.uikit_field_required_error),
                style = Fonts.textRegular(12.sp, red200)
            )
        }
    }
}

@Composable
fun CustomStyledButton(icon: Painter, onClickIcon: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .background(
                color = neutral100,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 4.dp,
                    bottomEnd = 4.dp
                )
            )
            .border(
                width = 1.dp,
                color = blue300,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 4.dp,
                    bottomEnd = 4.dp
                )
            )
            .clickable {
                onClickIcon.invoke()
            }
    ) {
        Icon(
            painter = icon,
            tint = blue300,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldWithButtonPreview() {
    TextFieldWithButton(
        modifier = Modifier.padding(16.dp),
        title = "Nombre",
        value = "Nombre",
        icon = androidx.compose.ui.res.painterResource(id = R.drawable.uikit_ic_arrow_down),
        onClickIcon = {},
        onChangeValue = {},
        showError = true
    )
}
