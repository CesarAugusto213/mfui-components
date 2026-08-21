package org.mindtec.uikit.ui.components.card

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts

@Composable
fun CardPriceMultiple(
    titles: List<Pair<String, String>>,
    subtitles: List<Pair<String, String>>,
    total: String? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, gray, RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(lightGrayPlaceholder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                titles.forEachIndexed { index, titlePair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = titlePair.first,
                            style = Fonts.textLight(10.sp, neutral800)
                        )
                        Text(
                            modifier = Modifier.wrapContentWidth(Alignment.End),
                            text = titlePair.second,
                            style = Fonts.textLight(10.sp, neutral800)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 20.dp)
                                .wrapContentWidth(Alignment.Start)
                        ) {
                            Text(
                                text = subtitles.getOrNull(index)?.first ?: "",
                                style = Fonts.textRegular(14.sp, neutral800),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Column(modifier = Modifier.wrapContentWidth(Alignment.End)) {
                            Text(
                                text = subtitles.getOrNull(index)?.second ?: "",
                                style = Fonts.textRegular(14.sp, neutral800)
                            )
                        }
                    }
                }

                total?.let {
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(blue300)
                                .padding(16.dp, 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.uikit_total_label),
                                style = Fonts.textRegular(14.sp, white)
                            )
                            Text(
                                text = it,
                                style = Fonts.textRegular(14.sp, white)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPriceMultiplePreview() {
    CardPriceMultiple(
        titles = listOf(
            Pair("Método del Pago", "Caja"),
            Pair("Destino", "Paga Con")
        ),
        subtitles = listOf(
            Pair("Efectivo", "$108.00"),
            Pair("Caja General Caja General Caja General Caja General Caja General", "$110.00")
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun CardPriceTotalPreview() {
    CardPriceMultiple(
        titles = listOf(
            Pair("Valor de Venta", "IGV 18%")
        ),
        subtitles = listOf(
            Pair("S/ 150", "$108.00"),
        ),
        total = "$218.00"
    )
}

@Preview(showBackground = true)
@Composable
private fun CardPriceOnePreview() {
    CardPriceMultiple(
        titles = listOf(
            Pair("Fecha de Vencimiento", "Monto")
        ),
        subtitles = listOf(
            Pair("16/04/2024", "$108.00"),
        )
    )
}
