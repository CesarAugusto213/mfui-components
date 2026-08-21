package org.mindtec.uikit.ui.components.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

/**
 * A diferencia del original (que recibía un `LoaderUiState` completo con
 * `isVisible`/`progress`/`description`), acá el estado entra directo por
 * parámetros — así la app no necesita definir ese wrapper solo para usar
 * este diálogo.
 */
@Composable
fun LoaderProgressDialog(
    isVisible: Boolean,
    progress: Float,
    description: String = ""
) {
    val safeProgress = progress.coerceIn(0f, 1f)

    if (isVisible) {
        Dialog(onDismissRequest = { }) {
            Card(
                modifier = Modifier.width(160.dp),
                colors = CardDefaults.cardColors(containerColor = white),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(55.dp),
                            progress = { 1f },
                            color = borderGrayCard,
                            strokeWidth = 8.dp,
                        )

                        CircularProgressIndicator(
                            modifier = Modifier.size(55.dp),
                            progress = { safeProgress },
                            color = UiKitTheme.colors.primary,
                            strokeWidth = 8.dp,
                        )

                        Text(
                            text = "${(safeProgress * 100).toInt()}%",
                            style = Fonts.textRegular(14.sp)
                        )
                    }

                    Text(
                        text = description.ifEmpty { stringResource(R.string.uikit_please_wait) },
                        style = Fonts.textLight(12.sp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoaderProgressDialogPreview() {
    LoaderProgressDialog(isVisible = true, progress = .6f, description = "Cargando la información...")
}
