package org.mindtec.uikit.ui.components.toolbar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.dialog.WarningDialog
import org.mindtec.uikit.ui.theme.*

@Composable
fun ToolBarMF(
    title: String,
    icon: Painter? = null,
    onShowDialog: Boolean = false,
    onClickIcon: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    onReturnScreen: () -> Unit = {},
    description: String = stringResource(id = R.string.uikit_exit_without_saving)
) {
    var showDialogChoose by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }

    // Row propia (en vez del TopAppBar de Material3) para garantizar que el
    // título quede centrado verticalmente sin importar la altura que se le
    // pida — el TopAppBar de M3 asume su altura por defecto (64.dp) para
    // calcular esa posición y con height(52.dp) el texto quedaba recortado.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(purple300),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            if (enabled && !onShowDialog) {
                onReturnScreen.invoke()
                enabled = false
            } else if (onShowDialog) {
                enabled = false
                showDialogChoose = true
            }
        }) {
            Icon(
                painter = painterResource(id = R.drawable.uikit_ic_arrow),
                contentDescription = null,
                tint = white
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = white
            )
        )

        icon?.let {
            IconButton(onClick = { onClickIcon.invoke() }) {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = white,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        }
    }

    BackHandler {
        if (onShowDialog) {
            showDialogChoose = true
        } else {
            onReturnScreen.invoke()
        }
    }

    if (showDialogChoose) {
        WarningDialog(
            description = description,
            onDismissRequest = {
                onDismissDialog.invoke()
                enabled = true
                showDialogChoose = false
            },
            onConfirmation = {
                onConfirmation.invoke()
                enabled = false
                showDialogChoose = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ToolBarMFPreview() {
    ToolBarMF(
        title = "Productos",
        icon = painterResource(id = R.drawable.uikit_ic_arrow_down),
        onReturnScreen = { }
    )
}
