package org.mindtec.uikit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.alerts.LoaderDialog
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun <T> CustomTable(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    headers: List<String>,
    weights: List<Float>,
    items: List<T>,
    emptyMessage: String = stringResource(R.string.uikit_empty_table),
    rowContent: @Composable (T) -> Unit,
    footerContent: @Composable (() -> Unit)? = null
) {
    if (isLoading) LoaderDialog()

    Card(shape = RoundedCornerShape(4.dp), modifier = modifier) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(white)
        ) {
            TableHeaderRow(headers = headers, weights = weights)

            LazyColumn(Modifier.weight(1f)) {
                if (items.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            text = emptyMessage,
                            style = Fonts.textRegular(14.sp, grayIconPlaceholder, TextAlign.Center)
                        )
                    }
                } else {
                    items(items) { rowContent(it) }
                }
            }

            footerContent?.let {
                it()
            }
        }
    }
}

@Composable
fun TableHeaderRow(headers: List<String>, weights: List<Float>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(purple300)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        headers.forEachIndexed { index, title ->
            TableText(title, Modifier.weight(weights[index]))
        }
    }
}

@Composable
fun TableText(
    title: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        modifier = modifier,
        text = title,
        style = Fonts.textBold(
            fontSize = 14.sp,
            color = white,
            textAlign = textAlign
        )
    )
}
