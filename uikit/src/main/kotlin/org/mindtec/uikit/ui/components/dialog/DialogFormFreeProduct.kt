package org.mindtec.uikit.ui.components.dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.CloseButton
import org.mindtec.uikit.ui.components.button.ButtonMF
import org.mindtec.uikit.ui.components.model.UiKitOption
import org.mindtec.uikit.ui.components.select.SelectMF
import org.mindtec.uikit.ui.components.text_field.FieldWidget
import org.mindtec.uikit.ui.components.text_field.SwitchMF
import org.mindtec.uikit.ui.components.text_field.TextFieldMF
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import java.math.BigDecimal

/**
 * Resultado del formulario de producto libre/manual — reemplaza a la
 * construcción directa de un `ProductEntity` del original. La app
 * consumidora mapea esto a su propia entidad (el original armaba un id
 * interno con un prefijo tipo `Constants.PREFIX_FREE + id`, moneda fija
 * `PEN`, etc. — esa lógica de negocio se queda en la app).
 */
data class FreeProductFormResult(
    val name: String,
    val quantity: BigDecimal,
    val salePrice: BigDecimal,
    val hasIgv: Boolean,
    val unitType: UiKitOption,
    val affectation: UiKitOption
)

/**
 * Formulario para agregar un producto "libre" (no catalogado) a un
 * documento. [unitOptions] y [affectationOptions] reemplazan a los catálogos
 * `UnitType`/`Constants.listTypeAffectationTypes` del original — la app los
 * provee porque son datos de su propio dominio (tipos de comprobante SUNAT,
 * unidades de medida habilitadas, etc.).
 */
@Composable
fun DialogFormFreeProduct(
    show: Boolean,
    unitOptions: List<UiKitOption>,
    affectationOptions: List<UiKitOption>,
    defaultAffectationId: String,
    onAddProduct: (FreeProductFormResult) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current.applicationContext

    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(BigDecimal.ONE) }
    var salePrice by remember { mutableStateOf("") }
    var igv by remember { mutableStateOf(true) }
    var unitMeasurement by remember { mutableStateOf(unitOptions.first()) }
    var affectation by remember {
        mutableStateOf(affectationOptions.find { it.id == defaultAffectationId } ?: affectationOptions.first())
    }

    if (show) {
        Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(white)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.uikit_add_free_product),
                            style = Fonts.textBold(
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        TextFieldMF(
                            title = stringResource(R.string.uikit_product_name),
                            value = name,
                            onChangeValue = { name = it },
                            isDoneButton = true
                        )

                        FieldWidget(
                            title = stringResource(R.string.uikit_quantity_sale),
                            quantity = quantity,
                            onClickedMinus = {
                                quantity = it
                            },
                            onClickedPlus = {
                                quantity = it
                            },
                            onConfirmation = {
                                quantity = it
                            }
                        )

                        TextFieldMF(
                            title = stringResource(R.string.uikit_label_sale_price),
                            value = salePrice,
                            isNumericKeyboard = true,
                            onChangeValue = { salePrice = it },
                            isDoneButton = true
                        )

                        SwitchMF(
                            text = stringResource(R.string.uikit_label_igv),
                            value = igv,
                            onCheckedChange = {
                                igv = it
                            }
                        )

                        SelectMF(
                            title = stringResource(R.string.uikit_placeholder_unit_measurement),
                            options = unitOptions,
                            selectedOption = unitMeasurement.description,
                            onOptionSelected = {
                                unitMeasurement = it
                            },
                            optionLabel = { it.description }
                        )

                        SelectMF(
                            title = stringResource(R.string.uikit_label_type),
                            description = stringResource(R.string.uikit_label_type_desc),
                            options = affectationOptions,
                            selectedOption = affectation.description,
                            onOptionSelected = {
                                affectation = it
                            },
                            optionLabel = { it.description }
                        )

                        ButtonMF(
                            text = stringResource(R.string.uikit_label_add_item),
                            color = purple300,
                            textColor = white,
                            icon = painterResource(R.drawable.uikit_ic_add),
                            iconColor = white,
                            fontSize = 16.sp,
                            onClick = {
                                if (name.isNotEmpty() && salePrice.isNotEmpty()) {
                                    onAddProduct.invoke(
                                        FreeProductFormResult(
                                            name = name,
                                            quantity = quantity,
                                            salePrice = salePrice.toBigDecimal(),
                                            hasIgv = igv,
                                            unitType = unitMeasurement,
                                            affectation = affectation
                                        )
                                    )

                                    name = ""
                                    quantity = BigDecimal.ONE
                                    salePrice = ""
                                    igv = true
                                    affectation = affectationOptions.find { it.id == defaultAffectationId }
                                        ?: affectationOptions.first()
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.uikit_message_dialog_alert_incomplete),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.uikit_empty_product_label),
                            style = Fonts.textRegular(12.sp, red200, TextAlign.Center)
                        )
                    }

                    CloseButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        action = onDismissRequest
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DialogFormFreeProductPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        DialogFormFreeProduct(
            show = true,
            unitOptions = listOf(
                UiKitOption("NIU", "Unidades"),
                UiKitOption("ZZ", "Servicio")
            ),
            affectationOptions = listOf(
                UiKitOption("10", "Gravado"),
                UiKitOption("20", "Exonerado"),
                UiKitOption("30", "Inafecto")
            ),
            defaultAffectationId = "10",
            onAddProduct = {},
            onDismissRequest = {}
        )
    }
}
