package org.mindtec.uikit.ui.components.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.black
import org.mindtec.uikit.ui.theme.grayCard
import org.mindtec.uikit.ui.theme.UiKitTheme
import org.mindtec.uikit.ui.theme.red200
import org.mindtec.uikit.ui.theme.white
import org.mindtec.uikit.utils.Fonts

/**
 * Un ítem del menú desplegable de [AppTopBar].
 *
 * @param onClick Se llama cuando el usuario toca el ítem — [AppTopBar] ya
 * cierra el menú antes de invocarlo, así que no hace falta manejar eso acá.
 */
data class AppTopBarMenuItem(
    val id: String,
    val label: String,
    val icon: Painter,
    val isSelected: Boolean = false,
    val color: Color = black,
    val onClick: () -> Unit
)

/**
 * Toolbar principal de la app: botón de menú hamburguesa que abre un
 * dropdown, logo centrado opcional y acceso a perfil opcional a la derecha.
 *
 * Sin ViewModel ni navegación acoplados: cada [AppTopBarMenuItem] trae su
 * propio `onClick`, así que la navegación/lógica de negocio (ir a un
 * `Activity`, chequear Bluetooth, cerrar sesión, etc.) la arma la app al
 * construir la lista de ítems — [AppTopBar] solo dibuja el menú.
 *
 * Para el diálogo de "¿cerrar sesión?" no hace falta un componente nuevo:
 * usa [org.mindtec.uikit.ui.components.dialog.WarningDialog] con la
 * descripción que quieras. Para el popup de perfil, ver
 * [org.mindtec.uikit.ui.components.dialog.ProfileDialog].
 *
 * @param logo Logo/ícono centrado del toolbar (opcional). Tiene prioridad
 * sobre [title] si se pasan ambos.
 * @param title Texto centrado del toolbar — alternativa a [logo] para apps
 * sin logo gráfico (opcional). Se ignora si [logo] es no-null.
 * @param menuItems Ítems del menú desplegable, en orden.
 * @param bottomMenuItems Ítems que se muestran separados al fondo del menú
 * (ej. "Sincronizar" / "Cerrar sesión") — solo se separan visualmente si
 * pasas [dropdownHeight]; sin eso, se listan seguidos de [menuItems].
 * @param onProfileClick Si es no-null, muestra el ícono de perfil a la
 * derecha y lo llama al tocarlo. Si es null, el ícono no se muestra.
 */
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    logo: Painter? = null,
    title: String? = null,
    backgroundColor: Color = UiKitTheme.colors.primary,
    menuItems: List<AppTopBarMenuItem>,
    bottomMenuItems: List<AppTopBarMenuItem> = emptyList(),
    dropdownWidth: Dp = 280.dp,
    dropdownHeight: Dp? = null,
    onProfileClick: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    fun handleClick(item: AppTopBarMenuItem) {
        expanded = false
        item.onClick.invoke()
    }

    Box(modifier = modifier) {
        // Box propio (no Row+weight ni el TopAppBar de Material3): con
        // weight, el ancho del ícono de menú (40.dp) y el de perfil
        // (28.dp) desbalanceaban el espacio repartido a cada lado del
        // centro, así que el logo/título nunca quedaba realmente
        // centrado. Alineando cada ítem por separado (CenterStart/
        // Center/CenterEnd) dentro del mismo Box, el contenido central
        // queda centrado respecto al ancho total sin importar el tamaño
        // de los ítems de los costados.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(backgroundColor)
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = if (expanded) R.drawable.uikit_ic_clear else R.drawable.uikit_ic_menu),
                contentDescription = null,
                colorFilter = ColorFilter.tint(white),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clickable { expanded = !expanded }
            )
            when {
                logo != null -> Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(150.dp),
                    painter = logo,
                    contentDescription = null
                )
                title != null -> Text(
                    text = title,
                    modifier = Modifier.align(Alignment.Center),
                    style = Fonts.textSemiBold(fontSize = 18.sp, color = white),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (onProfileClick != null) {
                Image(
                    painter = painterResource(id = R.drawable.uikit_ic_user),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(white),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(28.dp)
                        .clickable { onProfileClick.invoke() }
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 0.dp)
        ) {
            val boxModifier = if (dropdownHeight != null) {
                Modifier.width(dropdownWidth).height(dropdownHeight)
            } else {
                Modifier.width(dropdownWidth)
            }

            Box(modifier = boxModifier.padding(8.dp)) {
                Column {
                    menuItems.forEach { item ->
                        AppTopBarMenuItemRow(item = item, onClick = { handleClick(item) })
                    }

                    if (bottomMenuItems.isNotEmpty()) {
                        Spacer(modifier = Modifier.weight(1f))

                        bottomMenuItems.forEach { item ->
                            AppTopBarMenuItemRow(item = item, onClick = { handleClick(item) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppTopBarMenuItemRow(item: AppTopBarMenuItem, onClick: () -> Unit) {
    DropdownMenuItem(
        modifier = Modifier.background(if (item.isSelected) grayCard else Color.Transparent),
        leadingIcon = {
            Icon(
                painter = item.icon,
                tint = item.color,
                contentDescription = null
            )
        },
        text = {
            Text(item.label, modifier = Modifier.fillMaxWidth(), color = item.color)
        },
        onClick = onClick
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppTopBarPreview() {
    AppTopBar(
        menuItems = listOf(
            AppTopBarMenuItem(
                id = "catalog",
                label = "Catálogo",
                icon = painterResource(R.drawable.uikit_ic_image),
                isSelected = true,
                onClick = {}
            ),
            AppTopBarMenuItem(
                id = "settings",
                label = "Configuración",
                icon = painterResource(R.drawable.uikit_ic_edit),
                onClick = {}
            )
        ),
        bottomMenuItems = listOf(
            AppTopBarMenuItem(
                id = "logout",
                label = "Cerrar sesión",
                icon = painterResource(R.drawable.uikit_ic_clear),
                color = red200,
                onClick = {}
            )
        ),
        dropdownHeight = 400.dp,
        onProfileClick = {}
    )
}
