package org.mindtec.uikit

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test
import org.mindtec.uikit.ui.components.WidgetCounter
import org.mindtec.uikit.ui.components.button.IconButtonMF
import org.mindtec.uikit.ui.components.button.SmallIconButton
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.components.country_code_picker.Country
import org.mindtec.uikit.ui.components.country_code_picker.CountryCodePicker
import org.mindtec.uikit.ui.components.items.ItemEmptyList
import org.mindtec.uikit.ui.components.items.RadioButtonWithBorder
import org.mindtec.uikit.ui.components.select.SearchDropdownMenu
import org.mindtec.uikit.ui.components.select.SelectDialogMF
import org.mindtec.uikit.ui.components.select.SelectMF
import org.mindtec.uikit.ui.components.text_field.EditTextSearch
import org.mindtec.uikit.ui.components.text_field.FieldSearchNumDoc
import org.mindtec.uikit.ui.components.text_field.FieldWidget
import org.mindtec.uikit.ui.components.text_field.SearchSection
import org.mindtec.uikit.ui.components.text_field.SwitchMF
import org.mindtec.uikit.ui.components.text_field.TextFieldCountry
import org.mindtec.uikit.ui.components.text_field.TextFieldDate
import org.mindtec.uikit.ui.components.text_field.TextFieldDateTime
import org.mindtec.uikit.ui.components.text_field.TextFieldMF
import org.mindtec.uikit.ui.components.text_field.TextFieldMonth
import org.mindtec.uikit.ui.components.text_field.TextFieldPhoneNumberByCountry
import org.mindtec.uikit.ui.components.text_field.TextFieldPlate
import org.mindtec.uikit.ui.components.text_field.TextFieldWithButton
import org.mindtec.uikit.ui.components.text_field.TextFieldWithCenteredImage
import org.mindtec.uikit.ui.theme.white
import java.math.BigDecimal

/**
 * Galería visual del kit: renderiza una muestra representativa de
 * componentes a PNG usando Paparazzi (sin emulador ni Android Studio).
 * Correr con: ./gradlew :uikit:testDebugUnitTest
 * Las imágenes quedan en uikit/src/test/snapshots/images/.
 */
class PreviewGalleryTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(softButtons = false),
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    // Paparazzi no hostea una Activity real, así que componentes que usan
    // rememberLauncherForActivityResult (ej. TextFieldWithCenteredImage)
    // necesitan un owner aunque sea de mentira para no crashear al renderizar.
    private val noOpActivityResultRegistryOwner = object : ActivityResultRegistryOwner {
        override val activityResultRegistry = object : ActivityResultRegistry() {
            override fun <I, O> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: androidx.core.app.ActivityOptionsCompat?
            ) { /* no-op: no hay launch real en un screenshot estático */ }
        }
    }

    private fun snapshot(name: String, content: @androidx.compose.runtime.Composable () -> Unit) {
        paparazzi.snapshot(name = name) {
            CompositionLocalProvider(
                LocalActivityResultRegistryOwner provides noOpActivityResultRegistryOwner
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(white)
                        .padding(16.dp)
                ) {
                    content()
                }
            }
        }
    }

    @Test
    fun smallOutlinedButtonMF() = snapshot("button_SmallOutlinedButtonMF") {
        SmallOutlinedButtonMF(
            text = "Ver todos",
            onClick = {},
            background = androidx.compose.ui.graphics.Color(0xFFEFF3FE),
            border = androidx.compose.ui.graphics.Color(0xFF415AFF),
            textColor = androidx.compose.ui.graphics.Color(0xFF415AFF)
        )
    }

    @Test
    fun iconButtonMF() = snapshot("button_IconButtonMF") {
        IconButtonMF(text = "Verificar", onClick = {})
    }

    @Test
    fun smallIconButton() = snapshot("button_SmallIconButton") {
        SmallIconButton(
            icon = painterResource(id = R.drawable.uikit_ic_arrow_down),
            onClick = {}
        )
    }

    @Test
    fun textFieldMF() = snapshot("text_field_TextFieldMF") {
        TextFieldMF(
            title = "Nombre",
            value = "Juan Pérez",
            onChangeValue = {},
            showError = false
        )
    }

    @Test
    fun textFieldMFWithError() = snapshot("text_field_TextFieldMF_error") {
        TextFieldMF(
            title = "Nombre",
            value = "",
            onChangeValue = {},
            showError = true
        )
    }

    @Test
    fun searchSection() = snapshot("text_field_SearchSection") {
        SearchSection(
            modifier = Modifier.fillMaxWidth(),
            title = "Buscar producto",
            label = "Cerrar",
            onSearch = {},
            onClickIcon = {}
        )
    }

    @Test
    fun editTextSearch() = snapshot("text_field_EditTextSearch") {
        EditTextSearch(title = "Buscar…")
    }

    @Test
    fun textFieldPlate() = snapshot("text_field_TextFieldPlate") {
        TextFieldPlate(
            title = "Placa",
            description = "(Ejemplo: ABC-123)",
            value = TextFieldValue("ABC-1234"),
            onChangeValue = {}
        )
    }

    @Test
    fun textFieldWithButton() = snapshot("text_field_TextFieldWithButton") {
        TextFieldWithButton(
            title = "Código",
            icon = painterResource(id = R.drawable.uikit_ic_arrow_down),
            value = "0123456789",
            onChangeValue = {},
            onClickIcon = {}
        )
    }

    @Test
    fun textFieldDate() = snapshot("text_field_TextFieldDate") {
        TextFieldDate(
            title = "Fecha de emisión",
            defaultValue = "21/08/2026",
            onChangeValue = {},
            icon = painterResource(id = R.drawable.uikit_ic_arrow_down),
            enabled = true
        )
    }

    @Test
    fun textFieldDateTime() = snapshot("text_field_TextFieldDateTime") {
        TextFieldDateTime(
            title = "Fecha y hora",
            defaultValue = "21/08/2026 09:30:00",
            onChangeValue = {},
            icon = painterResource(id = R.drawable.uikit_ic_arrow_down),
            enabled = true
        )
    }

    @Test
    fun textFieldMonth() = snapshot("text_field_TextFieldMonth") {
        TextFieldMonth(
            title = "Mes y año",
            defaultValue = "2026-08",
            onChangeValue = {},
            icon = painterResource(id = R.drawable.uikit_ic_arrow_down),
            enabled = true
        )
    }

    @Test
    fun textFieldWithCenteredImage() = snapshot("text_field_TextFieldWithCenteredImage") {
        TextFieldWithCenteredImage(
            title = "Logo",
            imageUri = null,
            onImageSelected = {}
        )
    }

    @Test
    fun textFieldPhoneNumberByCountry() = snapshot("text_field_TextFieldPhoneNumberByCountry") {
        TextFieldPhoneNumberByCountry(
            title = "Teléfono",
            code = "+51",
            value = "987654321",
            onValueChange = {}
        )
    }

    @Test
    fun textFieldCountry() = snapshot("text_field_TextFieldCountry") {
        TextFieldCountry(
            title = "País",
            value = "Peru",
            onValueChange = {}
        )
    }

    @Test
    fun fieldSearchNumDoc() = snapshot("text_field_FieldSearchNumDoc") {
        FieldSearchNumDoc(
            title = "Número de documento",
            number = "12345678",
            requiredLength = 8,
            onChangeValue = {},
            action = {
                IconButtonMF(text = "Buscar", onClick = {})
            }
        )
    }

    @Test
    fun switchMF() = snapshot("text_field_SwitchMF") {
        var checked by remember { mutableStateOf(true) }
        SwitchMF(
            text = "Habilitar notificaciones",
            value = checked,
            onCheckedChange = { checked = it }
        )
    }

    @Test
    fun fieldWidget() = snapshot("text_field_FieldWidget") {
        FieldWidget(
            enabled = true,
            title = "Cantidad",
            quantity = BigDecimal(3),
            onClickedMinus = {},
            onClickedPlus = {},
            onConfirmation = {}
        )
    }

    @Test
    fun widgetCounter() = snapshot("WidgetCounter") {
        WidgetCounter(
            enabled = true,
            quantity = BigDecimal(5),
            stock = BigDecimal(20),
            stockControl = true,
            showInputDialog = {},
            onClickedMinus = {},
            onClickedPlus = {}
        )
    }

    @Test
    fun selectMF() = snapshot("select_SelectMF") {
        SelectMF(
            title = "Categoría",
            options = listOf("Abarrotes", "Bebidas", "Limpieza"),
            selectedOption = "Abarrotes",
            onOptionSelected = {},
            optionLabel = { it }
        )
    }

    @Test
    fun selectDialogMF() = snapshot("select_SelectDialogMF") {
        SelectDialogMF(
            title = "Serie",
            selectedOption = "F001",
            onClick = {}
        )
    }

    @Test
    fun searchDropdownMenu() = snapshot("select_SearchDropdownMenu") {
        SearchDropdownMenu(
            title = "Cliente",
            options = listOf("Cliente 1", "Cliente 2", "Cliente 3"),
            filter = { item, query -> item.contains(query, ignoreCase = true) },
            optionLabel = { it },
            optionSelected = {}
        )
    }

    @Test
    fun itemEmptyList() = snapshot("items_ItemEmptyList") {
        ItemEmptyList()
    }

    @Test
    fun radioButtonWithBorderSelected() = snapshot("items_RadioButtonWithBorder_selected") {
        RadioButtonWithBorder(
            modifier = Modifier.fillMaxWidth(),
            isSelected = true,
            onClick = {},
            label = "Opción seleccionada"
        )
    }

    @Test
    fun radioButtonWithBorderUnselected() = snapshot("items_RadioButtonWithBorder_unselected") {
        RadioButtonWithBorder(
            modifier = Modifier.fillMaxWidth(),
            isSelected = false,
            onClick = {},
            label = "Opción sin seleccionar"
        )
    }

    @Test
    fun countryCodePicker() = snapshot("country_code_picker_CountryCodePicker") {
        CountryCodePicker(
            selectedCountry = Country.Peru,
            onCountrySelected = {}
        )
    }
}
