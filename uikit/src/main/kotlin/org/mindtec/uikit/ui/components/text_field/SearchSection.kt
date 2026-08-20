package org.mindtec.uikit.ui.components.text_field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.IconButtonMF
import org.mindtec.uikit.ui.components.button.SmallIconButton

/**
 * Barra de búsqueda con un botón de acción opcional a la derecha:
 * un ícono ([icon]) o un botón de texto ([label]) — se usa el que se pase.
 */
@Composable
fun SearchSection(
    modifier: Modifier,
    title: String,
    icon: Painter? = null,
    label: String? = null,
    newText: String? = null,
    keyboardNumeric: Boolean = false,
    onSearch: (String) -> Unit,
    onClickClear: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    onClickIcon: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EditTextSearch(
            modifier = Modifier.weight(1f),
            title = title,
            newText = newText,
            keyboardNumeric = keyboardNumeric,
            onSearch = { onSearch.invoke(it) },
            onClickClear = { onClickClear.invoke() },
            onFocusChanged = onFocusChanged
        )

        icon?.let {
            SmallIconButton(
                icon = it,
                onClick = { onClickIcon.invoke() }
            )
        }

        label?.let {
            IconButtonMF(
                text = it,
                heightSize = 55.dp,
                onClick = { onClickIcon.invoke() },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSectionPreview() {
    SearchSection(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 10.dp),
        title = stringResource(R.string.uikit_search_placeholder),
        label = "Cerrar",
        onSearch = {},
        onClickIcon = {}
    )
}
