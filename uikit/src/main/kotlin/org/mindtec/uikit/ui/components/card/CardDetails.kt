package org.mindtec.uikit.ui.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.ButtonMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

/**
 * Una acción del footer de [CardDetails] (imprimir, compartir, WhatsApp,
 * despachar, etc.).
 *
 * @param showWhenCollapsed En el original, dos acciones (imprimir y
 * despacho) se mostraban siempre —incluso con la card cerrada— filtrando
 * por comparación de `R.drawable.ic_print`/`ic_package`; acá esa selección
 * la marca explícitamente la app con esta bandera en vez de comparar íconos.
 */
data class CardDetailsAction(
    val id: String,
    val icon: Painter,
    val showWhenCollapsed: Boolean = false,
    val onClick: () -> Unit
)

/**
 * Card expandible de un documento (comprobante/despacho/etc.) con badge de
 * estado y acciones. Reemplaza al `CardDetails` original, que:
 * - filtraba qué acciones mostrar comparando identidad de `R.drawable`
 *   contra `isAccept`/`isRegistered`/`isEnabledDispatch` — esa decisión de
 *   negocio ahora la toma la app al construir [actions] (con
 *   [CardDetailsAction.showWhenCollapsed]).
 * - abría un `WhatsappDialog` propio cuando se tocaba el ícono de WhatsApp
 *   — ahora cada [CardDetailsAction.onClick] hace lo que la app necesite
 *   (incluyendo mostrar su propio diálogo).
 */
@Composable
fun CardDetails(
    modifier: Modifier = Modifier,
    headerText: String,
    titleText: String,
    subtitleText: String,
    badgeText: String,
    badgeColor: Color = blue300,
    textColor: Color = white,
    calendarIcon: Painter,
    dateTimeText: String,
    additionalTitle: String? = null,
    additionalSubtitle: String,
    actions: List<CardDetailsAction>,
    buttonMF: (@Composable () -> Unit)? = null,
    expandIcon: Painter = painterResource(R.drawable.uikit_ic_arrow_down),
    collapseIcon: Painter = painterResource(R.drawable.uikit_ic_arrow_up)
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .border(
                width = .5.dp,
                color = grayPlaceholder,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = if (isExpanded) grayCard else white,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = headerText,
                style = Fonts.textRegular(18.sp, purple200)
            )

            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { isExpanded = !isExpanded },
                painter = if (isExpanded) collapseIcon else expandIcon,
                contentDescription = null
            )
        }

        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = titleText,
                style = Fonts.textBold(16.sp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = subtitleText,
                        style = Fonts.textRegular(14.sp, gray)
                    )

                    Box(
                        modifier = Modifier
                            .background(color = badgeColor, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp, 4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = Fonts.textRegular(12.sp, textColor)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    actions.filter { it.showWhenCollapsed }.forEach { action ->
                        CardDetailsActionButton(action)
                    }
                }
            }
        }

        if (isExpanded) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Column(
                modifier = Modifier.padding(bottom = 8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = calendarIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(purple200),
                    )

                    Text(
                        text = dateTimeText,
                        style = Fonts.textRegular(14.sp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                additionalTitle?.let {
                    Text(
                        text = it,
                        style = Fonts.textRegular(16.sp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = additionalSubtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    actions.filter { !it.showWhenCollapsed }.forEach { action ->
                        CardDetailsActionButton(action)
                    }
                }
            }
            buttonMF?.invoke()
        }
    }
}

@Composable
private fun CardDetailsActionButton(action: CardDetailsAction) {
    IconButton(
        modifier = Modifier
            .border(width = 1.dp, color = Color.Transparent, shape = CircleShape)
            .padding(start = 4.dp, end = 4.dp, top = 4.dp)
            .background(white),
        onClick = action.onClick
    ) {
        Image(
            painter = action.icon,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardDetailsPreview() {
    CardDetails(
        headerText = "Encabezado",
        titleText = "Título del Card",
        subtitleText = "123456789",
        badgeText = "Enviado",
        calendarIcon = painterResource(R.drawable.uikit_ic_arrow_down),
        dateTimeText = "2026-08-19 14:30",
        additionalTitle = "Título Adicional",
        additionalSubtitle = "Subtítulo Adicional",
        actions = listOf(
            CardDetailsAction("print", painterResource(R.drawable.uikit_ic_edit), showWhenCollapsed = true, onClick = {}),
            CardDetailsAction("share", painterResource(R.drawable.uikit_ic_arrow_right), onClick = {}),
            CardDetailsAction("delete", painterResource(R.drawable.uikit_ic_delete), onClick = {})
        ),
        buttonMF = {
            ButtonMF(
                text = "Acción",
                onClick = { }
            )
        }
    )
}
