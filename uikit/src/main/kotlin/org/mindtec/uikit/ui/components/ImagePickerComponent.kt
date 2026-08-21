package org.mindtec.uikit.ui.components

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.gray
import org.mindtec.uikit.utils.Fonts
import org.mindtec.uikit.utils.ImageUtils
import java.io.File

/**
 * Selector de imagen (cámara o galería) con optimización automática
 * (resize + orientación EXIF vía [ImageUtils]).
 *
 * Requiere que la app consumidora declare su propio `FileProvider` en el
 * manifest para poder tomar fotos con la cámara (necesario para exponer un
 * `content://` URI temporal al intent de cámara) — ver la documentación de
 * `androidx.core.content.FileProvider`. Por defecto se asume la
 * convención `"${'$'}{packageName}.fileprovider"`; si tu app usa otra
 * autoridad, pásala en [fileProviderAuthority].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerComponent(
    modifier: Modifier = Modifier,
    currentImageUrl: String? = null,
    localImageFile: File? = null,
    fileProviderAuthority: String? = null,
    onImageSelected: (File) -> Unit,
    placeholder: String? = null
) {
    val context = LocalContext.current
    val authority = fileProviderAuthority ?: "${context.packageName}.fileprovider"
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var localImageUri by remember { mutableStateOf<Uri?>(null) }

    val displayPlaceholder = placeholder ?: stringResource(R.string.uikit_image_picker_select_image)
    val cameraPermissionDeniedMessage = stringResource(R.string.uikit_image_picker_camera_permission_denied)

    LaunchedEffect(localImageFile) {
        localImageUri = localImageFile?.let { Uri.fromFile(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && selectedImageUri != null) {
            val optimizedFile = ImageUtils.optimizeImageForUpload(
                context = context,
                imageUri = selectedImageUri!!,
                maxWidth = 720,
                maxHeight = 720,
                quality = 50
            )

            optimizedFile?.let { file ->
                localImageUri = Uri.fromFile(file)
                onImageSelected(file)
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            selectedImageUri = ImageUtils.createImageFile(context, authority)
            cameraLauncher.launch(selectedImageUri!!)
        } else {
            Toast.makeText(context, cameraPermissionDeniedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val optimizedFile = ImageUtils.optimizeImageForUpload(
                context = context,
                imageUri = it,
                maxWidth = 720,
                maxHeight = 720,
                quality = 50
            )
            optimizedFile?.let { file ->
                localImageUri = Uri.fromFile(file)
                onImageSelected(file)
            }
        }
    }

    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { showBottomSheet = true },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val imageToShow = localImageUri?.toString() ?: currentImageUrl

                if (imageToShow != null) {
                    AsyncImage(
                        model = imageToShow,
                        contentDescription = stringResource(R.string.uikit_image_picker_product_image),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        FloatingActionButton(
                            onClick = { showBottomSheet = true },
                            modifier = Modifier.size(40.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.uikit_ic_camera),
                                contentDescription = stringResource(R.string.uikit_image_picker_change_image),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = displayPlaceholder,
                            style = Fonts.textRegular(14.sp, gray, TextAlign.Center)
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.uikit_image_picker_select_image),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            showBottomSheet = false
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.uikit_ic_camera),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.uikit_image_picker_take_photo),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(R.string.uikit_image_picker_take_photo_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            galleryLauncher.launch("image/*")
                            showBottomSheet = false
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.uikit_ic_gallery),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.uikit_image_picker_gallery),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(R.string.uikit_image_picker_gallery_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
