package org.mindtec.uikit.ui.components.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

/**
 * Popup de perfil que suele abrirse desde el ícono de usuario de
 * [org.mindtec.uikit.ui.components.toolbar.AppTopBar]. Reemplaza al
 * `ProfileDialog` original, que leía el logo desde `Constants.IMAGE_PATH`
 * (una ruta fija de MypeFact) — acá la app pasa su propia [logoUri].
 *
 * @param unavailableLabel Texto a mostrar cuando un campo llega `null` o
 * vacío (el original tenía un string fijo `profile_value_unavailable`).
 */
@Composable
fun ProfileDialog(
    name: String?,
    email: String?,
    domain: String?,
    logoUri: String? = null,
    title: String = stringResource(R.string.uikit_profile_dialog_title),
    nameLabel: String = stringResource(R.string.uikit_profile_label_name),
    emailLabel: String = stringResource(R.string.uikit_profile_label_email),
    domainLabel: String = stringResource(R.string.uikit_profile_label_domain),
    unavailableLabel: String = stringResource(R.string.uikit_profile_value_unavailable),
    onDismissRequest: () -> Unit
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
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(grayCard, CircleShape)
                        .border(2.dp, UiKitTheme.colors.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (logoUri != null) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(64.dp),
                            painter = rememberAsyncImagePainter(logoUri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(id = R.drawable.uikit_ic_user),
                            colorFilter = ColorFilter.tint(UiKitTheme.colors.primary),
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = title,
                    style = Fonts.textRegular(16.sp, neutral700, TextAlign.Center)
                )

                Spacer(modifier = Modifier.size(20.dp))

                ProfileField(
                    label = nameLabel,
                    value = name?.takeIf { it.isNotBlank() } ?: unavailableLabel
                )

                Spacer(modifier = Modifier.size(12.dp))

                ProfileField(
                    label = emailLabel,
                    value = email?.takeIf { it.isNotBlank() } ?: unavailableLabel
                )

                Spacer(modifier = Modifier.size(12.dp))

                ProfileField(
                    label = domainLabel,
                    value = domain?.takeIf { it.isNotBlank() } ?: unavailableLabel
                )

                Spacer(modifier = Modifier.size(20.dp))

                SmallOutlinedButtonMF(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    text = stringResource(R.string.uikit_close),
                    textColor = white,
                    textSize = 18.sp,
                    border = darkButton,
                    background = darkButton,
                    onClick = { onDismissRequest.invoke() }
                )
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            style = Fonts.textLight(12.sp, gray, TextAlign.Start)
        )
        Text(
            text = value,
            style = Fonts.textRegular(15.sp, neutral700, TextAlign.Start)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileDialogPreview() {
    ProfileDialog(
        name = "Juan Pérez",
        email = "juan.perez@mypefact.com",
        domain = "miempresa",
        onDismissRequest = {}
    )
}
