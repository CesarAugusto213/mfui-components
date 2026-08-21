package org.mindtec.uikit.ui.components.toolbar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
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

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = purple300),
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = white
                )
            )
        },
        navigationIcon = {
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
        },
        actions = {
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
    )

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
