package org.mindtec.uikit.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.model.UiKitNavItem
import org.mindtec.uikit.ui.theme.*

/**
 * Barra de navegación inferior. Reemplaza a `NavigationBottomBar`, que traía
 * hardcodeadas las 5 pantallas de MypeFact vía `Utils.navigationItems` +
 * `NavigationBottomBarRouter` — acá la app arma su propia lista de
 * [UiKitNavItem].
 */
@Composable
fun NavigationBottomBar(
    items: List<UiKitNavItem>,
    selectedRoute: String,
    onItemClick: (UiKitNavItem) -> Unit
) {
    NavigationBar(containerColor = neutral800) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.route == selectedRoute,
                label = { Text(item.label) },
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.label
                    )
                },
                onClick = { onItemClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = purple300,
                    selectedTextColor = purple300,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = grayInactive,
                    unselectedTextColor = grayInactive
                )
            )
        }
    }
}

@Preview
@Composable
private fun NavigationBottomBarPreview() {
    val items = listOf(
        UiKitNavItem("home", "Inicio", painterResource(R.drawable.uikit_ic_image)),
        UiKitNavItem("new", "Nuevo", painterResource(R.drawable.uikit_ic_add)),
        UiKitNavItem("list", "Lista", painterResource(R.drawable.uikit_ic_checkbox))
    )

    NavigationBottomBar(
        items = items,
        selectedRoute = "home",
        onItemClick = {}
    )
}
