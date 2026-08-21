package org.mindtec.uikit.ui.components.dialog

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.theme.*

@Composable
fun WarningDialog(
    description: String, onDismissRequest: () -> Unit, onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = description, style = TextStyle(
                        fontSize = 18.sp,
                        color = neutral700,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                )
                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    SmallOutlinedButtonMF(modifier = Modifier
                        .padding(end = 6.dp)
                        .height(50.dp)
                        .weight(1f),
                        text = stringResource(R.string.uikit_no),
                        textSize = 18.sp,
                        textColor = white,
                        border = UiKitTheme.colors.primary,
                        background = UiKitTheme.colors.primary,
                        onClick = { onDismissRequest.invoke() })

                    SmallOutlinedButtonMF(modifier = Modifier
                        .padding(start = 6.dp)
                        .height(50.dp)
                        .weight(1f),
                        text = stringResource(R.string.uikit_yes),
                        textColor = darkButton,
                        textSize = 18.sp,
                        border = darkButton,
                        onClick = { onConfirmation.invoke() })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WarningDialogPreview() {
    WarningDialog(
        description = "Tienes cambios sin guardar. ¿Seguro que quieres salir?",
        onDismissRequest = {},
        onConfirmation = {})
}
