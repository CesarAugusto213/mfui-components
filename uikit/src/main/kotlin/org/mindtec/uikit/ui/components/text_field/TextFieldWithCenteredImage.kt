package org.mindtec.uikit.ui.components.text_field

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.grayPlaceholder
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import org.mindtec.uikit.ui.theme.grayCard
import org.mindtec.uikit.utils.Fonts
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.interaction.MutableInteractionSource

@Composable
fun TextFieldWithCenteredImage(
    modifier: Modifier = Modifier,
    title: String,
    titleFontSize: TextUnit = 16.sp,
    imageUri: Uri? = null,
    previousImageUri: Uri? = null,
    onImageSelected: (Uri) -> Unit,
    onDeleteRequested: (() -> Unit)? = null,
    disabled: Boolean = false
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onImageSelected.invoke(it) }
        }
    )

    val image = imageUri ?: previousImageUri

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Fonts.textRegular(titleFontSize)
            )

            // Botón X para eliminar imagen - fuera del área de la imagen
            if (image != null && onDeleteRequested != null) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onDeleteRequested.invoke()
                        }
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red.copy(alpha = 0.8f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar imagen",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(grayCard),
                value = "",
                onValueChange = {},
                readOnly = true,
                enabled = disabled,
                placeholder = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (image != null) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = rememberAsyncImagePainter(image),
                                contentDescription = null,
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Image(
                                modifier = Modifier.size(100.dp),
                                painter = painterResource(id = R.drawable.uikit_ic_image),
                                contentDescription = "Imagen de placeholder",
                                colorFilter = ColorFilter.tint(color = grayPlaceholder)
                            )
                        }
                    }
                }
            )

            if (disabled) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldWithCenteredImagePreview() {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    TextFieldWithCenteredImage(
        modifier = Modifier.padding(16.dp),
        title = "Logo",
        imageUri = null,
        onImageSelected = { uri ->
            selectedImageUri = uri
        }
    )
}
