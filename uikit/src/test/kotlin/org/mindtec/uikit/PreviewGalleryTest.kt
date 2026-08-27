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
import org.mindtec.uikit.ui.components.CountSlider
import org.mindtec.uikit.ui.components.CustomTable
import org.mindtec.uikit.ui.components.ImagePickerComponent
import org.mindtec.uikit.ui.components.NavigationBottomBar
import org.mindtec.uikit.ui.components.TableProductList
import org.mindtec.uikit.ui.components.TableRestaurantProductList
import org.mindtec.uikit.ui.components.alerts.AlertDialog
import org.mindtec.uikit.ui.components.alerts.LoaderDialog
import org.mindtec.uikit.ui.components.alerts.LoaderProgressDialog
import org.mindtec.uikit.ui.components.button.ButtonCountProduct
import org.mindtec.uikit.ui.components.button.ButtonMF
import org.mindtec.uikit.ui.components.button.ButtonOptions
import org.mindtec.uikit.ui.components.button.IconButtonMF
import org.mindtec.uikit.ui.components.button.OutlineIconButtonMF
import org.mindtec.uikit.ui.components.button.OutlinedButtonMF
import org.mindtec.uikit.ui.components.button.SmallIconButton
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.components.button.ToggleSelector2
import org.mindtec.uikit.ui.components.card.CardDetails
import org.mindtec.uikit.ui.components.card.CardDetailsAction
import org.mindtec.uikit.ui.components.card.CardInventory
import org.mindtec.uikit.ui.components.card.CardList
import org.mindtec.uikit.ui.components.card.CardListClient
import org.mindtec.uikit.ui.components.card.CardListSimple
import org.mindtec.uikit.ui.components.card.CardPriceMultiple
import org.mindtec.uikit.ui.components.card.DataClient
import org.mindtec.uikit.ui.components.card.InfoRow
import org.mindtec.uikit.ui.components.card.SearchButton
import org.mindtec.uikit.ui.components.country_code_picker.Country
import org.mindtec.uikit.ui.components.country_code_picker.CountryCodePicker
import org.mindtec.uikit.ui.components.dialog.ConfirmDialog
import org.mindtec.uikit.ui.components.dialog.DialogFormFreeProduct
import org.mindtec.uikit.ui.components.dialog.DialogInputComment
import org.mindtec.uikit.ui.components.dialog.DialogItemLots
import org.mindtec.uikit.ui.components.dialog.FilterDialogMF
import org.mindtec.uikit.ui.components.dialog.ListPriceDialog
import org.mindtec.uikit.ui.components.dialog.LotWithSeries
import org.mindtec.uikit.ui.components.dialog.LotsSelectionType
import org.mindtec.uikit.ui.components.dialog.NullDialog
import org.mindtec.uikit.ui.components.dialog.OpenSalesBoxDialog
import org.mindtec.uikit.ui.components.dialog.PaymentCashDialog
import org.mindtec.uikit.ui.components.dialog.PaymentInstallmentsDialog
import org.mindtec.uikit.ui.components.dialog.PhoneNumberDialog
import org.mindtec.uikit.ui.components.dialog.ProfileDialog
import org.mindtec.uikit.ui.components.dialog.SingleTextInputDialog
import org.mindtec.uikit.ui.components.dialog.StateTypeDialog
import org.mindtec.uikit.ui.components.dialog.TableDialog
import org.mindtec.uikit.ui.components.dialog.TableRestaurantDialog
import org.mindtec.uikit.ui.components.dialog.WarningDialog
import org.mindtec.uikit.ui.components.items.ItemCircleReport
import org.mindtec.uikit.ui.components.items.ItemEmptyList
import org.mindtec.uikit.ui.components.items.ItemProduct
import org.mindtec.uikit.ui.components.items.ItemProductRestaurantList
import org.mindtec.uikit.ui.components.items.ItemReportDashboard
import org.mindtec.uikit.ui.components.items.RadioButtonWithBorder
import org.mindtec.uikit.ui.components.items.SelectableCard
import org.mindtec.uikit.ui.components.model.UiKitCashPayment
import org.mindtec.uikit.ui.components.model.UiKitCatalogProduct
import org.mindtec.uikit.ui.components.model.UiKitCircleReportItem
import org.mindtec.uikit.ui.components.model.UiKitComment
import org.mindtec.uikit.ui.components.model.UiKitDashboardCard
import org.mindtec.uikit.ui.components.model.UiKitInstallment
import org.mindtec.uikit.ui.components.model.UiKitLotGroup
import org.mindtec.uikit.ui.components.model.UiKitNavItem
import org.mindtec.uikit.ui.components.model.UiKitOption
import org.mindtec.uikit.ui.components.model.UiKitPaymentDestination
import org.mindtec.uikit.ui.components.model.UiKitPaymentMethod
import org.mindtec.uikit.ui.components.model.UiKitPriceOption
import org.mindtec.uikit.ui.components.model.UiKitProduct
import org.mindtec.uikit.ui.components.model.UiKitSeries
import org.mindtec.uikit.ui.components.select.SearchDropdownMenu
import org.mindtec.uikit.ui.components.select.SelectDialogMF
import org.mindtec.uikit.ui.components.select.SelectMF
import org.mindtec.uikit.ui.components.select.SelectSmallMF
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
import org.mindtec.uikit.ui.components.toolbar.AppTopBar
import org.mindtec.uikit.ui.components.toolbar.AppTopBarMenuItem
import org.mindtec.uikit.ui.components.toolbar.ToolBarMF
import org.mindtec.uikit.ui.theme.UiKitColors
import org.mindtec.uikit.ui.theme.UiKitTheme
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

    @Test
    fun buttonMF() = snapshot("button_ButtonMF") {
        ButtonMF(text = "Ingresar", onClick = {})
    }

    @Test
    fun outlinedButtonMF() = snapshot("button_OutlinedButtonMF") {
        OutlinedButtonMF(text = "Cancelar", onClick = {})
    }

    @Test
    fun buttonCountProduct() = snapshot("button_ButtonCountProduct") {
        ButtonCountProduct(
            onClick = {},
            quantity = 10,
            total = "S/ 50",
            description = "Items"
        )
    }

    @Test
    fun toggleSelector2() = snapshot("button_ToggleSelector2") {
        ToggleSelector2(
            label = "Selecciona una opción",
            options = listOf("Opción 1", "Opción 2", "Opción 3"),
            selectedOption = "Opción 1",
            onOptionSelected = {},
            optionLabel = { it }
        )
    }

    @Test
    fun buttonOptions() = snapshot("button_ButtonOptions") {
        ButtonOptions(onClickCancel = {}, onClickSave = {})
    }

    @Test
    fun loaderDialog() = snapshot("alerts_LoaderDialog") {
        LoaderDialog(text = "Cargando…")
    }

    @Test
    fun alertDialog() = snapshot("alerts_AlertDialog") {
        AlertDialog(
            description = "Registro realizado de forma exitosa",
            image = painterResource(id = R.drawable.uikit_ic_arrow_down),
            onDismissRequest = {},
            onConfirmation = {},
            showConfirmationButton = true,
            buttonSave = "Ok"
        )
    }

    @Test
    fun warningDialog() = snapshot("dialog_WarningDialog") {
        WarningDialog(
            description = "Tienes cambios sin guardar. ¿Seguro que quieres salir?",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }

    @Test
    fun confirmDialog() = snapshot("dialog_ConfirmDialog") {
        ConfirmDialog(
            title = "Confirmar cambios",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }

    @Test
    fun nullDialog() = snapshot("dialog_NullDialog") {
        NullDialog(
            title = "ANULAR",
            billNumber = "F001-298",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }

    @Test
    fun toolBarMF() = snapshot("toolbar_ToolBarMF") {
        ToolBarMF(
            title = "Productos",
            icon = painterResource(id = R.drawable.uikit_ic_arrow_down),
            onReturnScreen = {}
        )
    }

    private val sampleProducts = listOf(
        UiKitProduct(
            id = "1",
            itemTypeId = "1",
            description = "HAMBURGUESA CLÁSICA",
            quantity = BigDecimal(2),
            unitPrice = BigDecimal("18.50")
        ),
        UiKitProduct(
            id = "2",
            itemTypeId = "2",
            description = "GASEOSA 500ML",
            quantity = BigDecimal.ONE,
            unitPrice = BigDecimal("5.00")
        )
    )

    @Test
    fun tableProductList() = snapshot("TableProductList") {
        TableProductList(
            items = sampleProducts,
            onClickDelete = {}
        )
    }

    @Test
    fun itemProductRestaurantList() = snapshot("items_ItemProductRestaurantList") {
        ItemProductRestaurantList(
            item = sampleProducts.first(),
            comments = listOf(
                UiKitComment(id = 1, description = "Sin cebolla", categoryId = 1, isSelected = true)
            ),
            onClickDelete = { _, _ -> }
        )
    }

    @Test
    fun tableRestaurantProductList() = snapshot("TableRestaurantProductList") {
        TableRestaurantProductList(
            items = sampleProducts,
            onClickDelete = { _, _ -> }
        )
    }

    @Test
    fun tableDialog() = snapshot("dialog_TableDialog") {
        TableDialog(
            items = sampleProducts,
            onClickButton = {},
            onDismissRequest = {},
            onClickDelete = {}
        )
    }

    @Test
    fun tableRestaurantDialog() = snapshot("dialog_TableRestaurantDialog") {
        TableRestaurantDialog(
            items = sampleProducts,
            onClickButton = {},
            onDismissRequest = {},
            onClickDelete = { _, _ -> }
        )
    }

    @Test
    fun filterDialogMF() = snapshot("dialog_FilterDialogMF") {
        FilterDialogMF(
            options = listOf("Almacén Central", "Almacén Norte", "Almacén Sur"),
            selectedOption = "Almacén Central",
            setLabel = { it }
        )
    }

    @Test
    fun dialogInputComment() = snapshot("dialog_DialogInputComment") {
        DialogInputComment(
            product = sampleProducts.first(),
            quantity = 2,
            category = "Hamburguesas",
            availableComments = listOf(
                UiKitComment(id = 1, description = "Sin cebolla", categoryId = 1),
                UiKitComment(id = 2, description = "Sin tomate", categoryId = 1),
                UiKitComment(id = 3, description = "Extra queso", categoryId = 1),
                UiKitComment(id = 4, description = "Para llevar", categoryId = 1)
            ),
            onDismissRequest = {},
            onRequestNewComment = { _, _ -> },
            onConfirm = { _, _ -> }
        )
    }

    // ==================== Flujo de restaurante -> facturación ====================

    @Test
    fun customTable() = snapshot("CustomTable") {
        CustomTable(
            headers = listOf("Producto", "Cant.", "Precio"),
            weights = listOf(2f, 1f, 1f),
            items = listOf("Hamburguesa" to "S/ 18.50", "Gaseosa" to "S/ 5.00"),
            rowContent = { (name, price) ->
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    androidx.compose.material3.Text(name, modifier = Modifier.weight(3f))
                    androidx.compose.material3.Text(price, modifier = Modifier.weight(1f))
                }
            }
        )
    }

    @Test
    fun outlineIconButtonMF() = snapshot("button_OutlineIconButtonMF") {
        OutlineIconButtonMF(
            icon = painterResource(R.drawable.uikit_ic_edit),
            onClick = {}
        )
    }

    @Test
    fun selectableCard() = snapshot("items_SelectableCard") {
        SelectableCard(
            title = "Cliente EIRL",
            subTitle = "1234567890",
            isSelected = true,
            onClick = {}
        )
    }

    @Test
    fun cardPriceMultiple() = snapshot("card_CardPriceMultiple") {
        CardPriceMultiple(
            titles = listOf("Valor de Venta" to "IGV 18%"),
            subtitles = listOf("S/ 150" to "S/ 27"),
            total = "S/ 177"
        )
    }

    @Test
    fun dataClient() = snapshot("card_DataClient") {
        DataClient(
            userName = "Nombre del usuario",
            userAddress = "Benavides 247 - Miraflores",
            onUserChange = {},
            onAddressChange = {},
            onEditUser = {}
        )
    }

    @Test
    fun searchButton() = snapshot("card_SearchButton") {
        SearchButton(
            textButton = "Buscar Clientes",
            onClick = {}
        )
    }

    @Test
    fun selectSmallMF() = snapshot("select_SelectSmallMF") {
        SelectSmallMF(
            options = listOf("Efectivo", "Tarjeta", "Transferencia"),
            selectedOption = "Efectivo",
            onOptionSelected = {},
            optionLabel = { it }
        )
    }

    @Test
    fun loaderProgressDialog() = snapshot("alerts_LoaderProgressDialog") {
        LoaderProgressDialog(
            isVisible = true,
            progress = 0.6f,
            description = "Cargando la información..."
        )
    }

    @Test
    fun imagePickerComponent() = snapshot("ImagePickerComponent") {
        ImagePickerComponent(
            modifier = Modifier.fillMaxWidth(),
            currentImageUrl = null,
            onImageSelected = {}
        )
    }

    private val sampleCatalogProduct = UiKitCatalogProduct(
        id = "1",
        description = "GASEOSA 500ML",
        stock = BigDecimal(20),
        currencySymbol = "S/",
        category = "Bebidas",
        calculateQuantity = false
    )

    @Test
    fun itemProduct() = snapshot("items_ItemProduct") {
        ItemProduct(
            modifier = Modifier.fillMaxWidth(),
            product = sampleCatalogProduct,
            currentCost = BigDecimal("5.00"),
            isChecked = true,
            isEditPriceEnabled = true,
            quantity = BigDecimal(2),
            stockControl = true,
            showInputDialog = {},
            showCommentDialog = { _, _ -> },
            showPriceDialog = {},
            showQuantityByPrice = {},
            showItemLotsDialog = {},
            showListPrice = {},
            onCheckedChange = {},
            onClickedPlus = {},
            onClickedMinus = {}
        )
    }

    @Test
    fun dialogFormFreeProduct() = snapshot("dialog_DialogFormFreeProduct") {
        DialogFormFreeProduct(
            show = true,
            unitOptions = listOf(UiKitOption("NIU", "Unidades"), UiKitOption("ZZ", "Servicio")),
            affectationOptions = listOf(
                UiKitOption("10", "Gravado"),
                UiKitOption("20", "Exonerado")
            ),
            defaultAffectationId = "10",
            onAddProduct = {},
            onDismissRequest = {}
        )
    }

    @Test
    fun dialogItemLotsSeriesOnly() = snapshot("dialog_DialogItemLots_series") {
        DialogItemLots(
            show = true,
            selectionType = LotsSelectionType.SERIES_ONLY,
            itemLots = listOf(
                UiKitSeries(id = 1, series = "SERIE-001-ABC", date = "05/01/2026"),
                UiKitSeries(id = 2, series = "SERIE-002-DEF", date = "06/01/2026")
            ),
            lotsGroup = emptyList(),
            lotsWithSeries = emptyList(),
            onConfirmation = {},
            onDismissRequest = {}
        )
    }

    @Test
    fun dialogItemLotsLotsOnly() = snapshot("dialog_DialogItemLots_lots") {
        DialogItemLots(
            show = true,
            selectionType = LotsSelectionType.LOTS_ONLY,
            itemLots = emptyList(),
            lotsGroup = listOf(
                UiKitLotGroup(id = 47, code = "LOTE-A", quantity = BigDecimal("11.0000"), dateOfDue = "23/05/2026"),
                UiKitLotGroup(id = 48, code = "LOTE-B", quantity = BigDecimal("25.0000"), dateOfDue = "30/04/2026", selectedQuantity = 2)
            ),
            lotsWithSeries = emptyList(),
            onConfirmation = {},
            onDismissRequest = {}
        )
    }

    @Test
    fun dialogItemLotsWithSeries() = snapshot("dialog_DialogItemLots_lotsWithSeries") {
        DialogItemLots(
            show = true,
            selectionType = LotsSelectionType.LOTS_WITH_SERIES,
            itemLots = emptyList(),
            lotsGroup = emptyList(),
            lotsWithSeries = listOf(
                LotWithSeries(
                    lot = UiKitLotGroup(id = 52, code = "LOTE-PRINCIPAL", quantity = BigDecimal("5.0000"), dateOfDue = "27/08/2026"),
                    series = listOf(
                        UiKitSeries(id = 99, series = "LP-SERIE-001", date = "05/01/2026"),
                        UiKitSeries(id = 100, series = "LP-SERIE-002", date = "05/01/2026")
                    ),
                    isExpanded = true
                )
            ),
            onConfirmation = {},
            onDismissRequest = {}
        )
    }

    @Test
    fun listPriceDialog() = snapshot("dialog_ListPriceDialog") {
        ListPriceDialog(
            show = true,
            options = listOf(
                UiKitPriceOption(id = "1", unitTypeId = "NIU", description = "Unidad", quantityUnit = "1", price = BigDecimal("18.50")),
                UiKitPriceOption(id = "2", unitTypeId = "CJA", description = "Caja x12", quantityUnit = "12", price = BigDecimal("200.00"))
            ),
            originalPrice = BigDecimal("18.50"),
            originalUnitType = "NIU",
            currentPrice = BigDecimal("18.50"),
            onSelectItem = {},
            onDismissRequest = {}
        )
    }

    @Test
    fun paymentCashDialog() = snapshot("dialog_PaymentCashDialog") {
        PaymentCashDialog(
            title = "PAGO EN EFECTIVO",
            listPayment = listOf(UiKitPaymentMethod("cash", "Efectivo"), UiKitPaymentMethod("card", "Tarjeta")),
            listDestination = listOf(UiKitPaymentDestination("caja1", "Caja General")),
            currentPaymentMethod = null,
            currentPaymentDestinations = null,
            total = BigDecimal("100.00"),
            methodLabel = "Efectivo",
            saveCashPayment = { _, _ -> },
            onDismissRequest = {},
            onConfirmation = {}
        )
    }

    @Test
    fun paymentInstallmentsDialog() = snapshot("dialog_PaymentInstallmentsDialog") {
        PaymentInstallmentsDialog(
            title = "PAGO A CRÉDITO",
            initialInstallments = listOf(
                UiKitInstallment(date = "21/09/2026", amount = "50.00"),
                UiKitInstallment(date = "21/10/2026", amount = "50.00")
            ),
            total = BigDecimal("100.00"),
            methodLabel = "Crédito",
            saveInstallmentsPayment = { _, _ -> },
            onDismissRequest = {},
            onConfirmation = {}
        )
    }

    @Test
    fun appTopBar() = snapshot("toolbar_AppTopBar") {
        AppTopBar(
            logo = painterResource(R.drawable.uikit_ic_image),
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
                    color = androidx.compose.ui.graphics.Color(0xFFE53935),
                    onClick = {}
                )
            ),
            dropdownHeight = 260.dp,
            onProfileClick = {}
        )
    }

    @Test
    fun appTopBarWithTitle() = snapshot("toolbar_AppTopBar_title") {
        AppTopBar(
            title = "Mi Negocio",
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
            onProfileClick = {}
        )
    }

    @Test
    fun profileDialog() = snapshot("dialog_ProfileDialog") {
        ProfileDialog(
            name = "Juan Pérez",
            email = "juan.perez@mypefact.com",
            domain = "miempresa",
            onDismissRequest = {}
        )
    }

    // ==================== Wave 4b — resto de uso general ====================

    @Test
    fun cardInventory() = snapshot("card_CardInventory") {
        CardInventory(
            title = "Product Name",
            stock = "1234.43",
            subtitleText = "Subtitle Text",
            onInputInventory = {},
            onMoveInventory = {},
            onAdjustmentInventory = {}
        )
    }

    @Test
    fun cardList() = snapshot("card_CardList") {
        CardList(
            badgeColor = androidx.compose.ui.graphics.Color(0xFFE53935),
            badgeText = "Cerrado",
            headerText = "Estado",
            footerLeftText = "Acción 1",
            footerRightText = "Acción 2",
            onFooterLeftClick = { },
            onFooterRightClick = { }
        ) {
            androidx.compose.foundation.layout.Column {
                InfoRow(leftText = "Abrio caja", rightText = "Julio Martinez")
                InfoRow(leftText = "Fecha", rightText = "12/12/2021")
            }
        }
    }

    @Test
    fun cardListClient() = snapshot("card_CardListClient") {
        CardListClient(
            title = "Client Title",
            subtitle = "Client Subtitle",
            checked = false,
            onCheckedChange = {},
            editIcon = painterResource(R.drawable.uikit_ic_edit),
            deleteIcon = painterResource(R.drawable.uikit_ic_delete)
        )
    }

    @Test
    fun cardListSimple() = snapshot("card_CardListSimple") {
        CardListSimple(
            title = "Client Title",
            subtitle = "Client Subtitle",
            editIcon = painterResource(R.drawable.uikit_ic_edit),
            deleteIcon = painterResource(R.drawable.uikit_ic_delete)
        )
    }

    @Test
    fun cardDetails() = snapshot("card_CardDetails") {
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
            )
        )
    }

    @Test
    fun itemCircleReport() = snapshot("items_ItemCircleReport") {
        ItemCircleReport(
            item = UiKitCircleReportItem(
                title = "Cuentas por Cobrar",
                percentage = java.math.BigDecimal(80),
                primaryText = "Margen bruto",
                primaryColor = androidx.compose.ui.graphics.Color(0xFFE53935),
                secondaryText = "Costo de producto",
                secondaryColor = androidx.compose.ui.graphics.Color(0xFFFEC6C6),
                number = "150"
            )
        )
    }

    @Test
    fun itemReportDashboard() = snapshot("items_ItemReportDashboard") {
        ItemReportDashboard(
            UiKitDashboardCard(
                icon = painterResource(R.drawable.uikit_ic_image),
                trailingIcon = painterResource(R.drawable.uikit_ic_image),
                title = "Boletas y Facturas",
                value = "S/ 1400"
            )
        )
    }

    @Test
    fun countSlider() = snapshot("CountSlider") {
        CountSlider(
            label = "Cantidad de mesas:",
            value = 10,
            onValueChange = {}
        )
    }

    @Test
    fun navigationBottomBar() = snapshot("NavigationBottomBar") {
        NavigationBottomBar(
            items = listOf(
                UiKitNavItem("home", "Inicio", painterResource(R.drawable.uikit_ic_image)),
                UiKitNavItem("new", "Nuevo", painterResource(R.drawable.uikit_ic_add)),
                UiKitNavItem("list", "Lista", painterResource(R.drawable.uikit_ic_checkbox))
            ),
            selectedRoute = "home",
            onItemClick = {}
        )
    }

    @Test
    fun openSalesBoxDialog() = snapshot("dialog_OpenSalesBoxDialog") {
        OpenSalesBoxDialog(
            title = "Abrir caja",
            inputLabel = "Monto inicial",
            currencyLabel = "Efectivo",
            buttonClose = "No",
            buttonSave = "Sí",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }

    @Test
    fun singleTextInputDialog() = snapshot("dialog_SingleTextInputDialog") {
        SingleTextInputDialog(
            description = "Ingresa tu correo",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }

    @Test
    fun stateTypeDialog() = snapshot("dialog_StateTypeDialog") {
        StateTypeDialog(
            title = "Actualizar estado",
            options = listOf("Registrado", "Enviado", "Aceptado", "Observado"),
            label = "Aceptado",
            optionLabel = { it },
            onDismissRequest = {},
            onConfirmation = {},
            onOptionSelected = {}
        )
    }

    @Test
    fun phoneNumberDialog() = snapshot("dialog_PhoneNumberDialog") {
        PhoneNumberDialog(
            title = "Enviar por WhatsApp",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }

    // ==================== Theming — UiKitColors ====================

    @Test
    fun themedBlue() = snapshot("theme_UiKitColors_blue") {
        UiKitTheme(colors = UiKitColors.fromPrimary(androidx.compose.ui.graphics.Color(0xFF2196F3))) {
            WarningDialog(
                description = "¿Seguro que quieres salir sin guardar?",
                onDismissRequest = {},
                onConfirmation = {}
            )
        }
    }

    @Test
    fun themedGreen() = snapshot("theme_UiKitColors_green") {
        UiKitTheme(colors = UiKitColors.fromPrimary(androidx.compose.ui.graphics.Color(0xFF2E7D32))) {
            androidx.compose.foundation.layout.Column(
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                SmallIconButton(
                    icon = painterResource(R.drawable.uikit_ic_arrow_down),
                    onClick = {}
                )
                RadioButtonWithBorder(
                    modifier = Modifier.fillMaxWidth(),
                    isSelected = true,
                    onClick = {},
                    label = "Opción seleccionada"
                )
            }
        }
    }
}
