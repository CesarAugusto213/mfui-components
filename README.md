# mfui-components

Librería de componentes de Jetpack Compose (botones, diálogos, toolbars,
selects, cards, etc.) para reutilizar entre proyectos Android sin copiar y
pegar código. Los modelos de datos que consumen los componentes son
genéricos (no dependen de Room, Retrofit ni de ningún ViewModel/Hilt), así
que se pueden usar en cualquier proyecto.

Licencia [MIT](LICENSE).

## Instalación

En el `settings.gradle.kts` del proyecto:

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url = uri("https://jitpack.io") }
    }
}
```

Y en el `build.gradle.kts` del módulo de la app:

```kotlin
dependencies {
    implementation("com.github.CesarAugusto213:mfui-components:v0.3.0")
}
```

## Tema

Los componentes usan un solo grupo de colores de marca — `primary` (botones,
toolbar, bordes activos), `primaryContainer`/`primaryContainerAlt` (fondos
claros de badges/chips), `secondary` (acentos de texto) y `primaryPressed`
(estados presionados). El resto de la paleta (grises, colores de estado)
es fija.

Para adaptarlos a tu marca, envolvé tu contenido en `UiKitTheme`:

```kotlin
// Un solo color — el resto de los tonos se generan automáticamente
UiKitTheme(colors = UiKitColors.fromPrimary(Color(0xFF2196F3))) {
    // tu contenido
}

// O con control total sobre cada tono
UiKitTheme(
    colors = UiKitColors(
        primary = Color(0xFF2196F3),
        primaryContainer = Color(0xFFD6EAFB),
        primaryContainerAlt = Color(0xFFC7E3F9),
        secondary = Color(0xFF64B5F6),
        primaryPressed = Color(0xFF1565C0)
    )
) {
    // tu contenido
}
```

Sin envolver nada, se ven exactamente igual que antes (`UiKitColors.Default`).

## Catálogo de componentes

Captura real de cada componente + lo mínimo que necesita para funcionar. Todos viven en `org.mindtec.uikit.ui.components.*` — el import completo es `org.mindtec.uikit.ui.components.<paquete>.<Nombre>`.

### Botones (`ui.components.button`)

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_smallOutlinedButtonMF_button_smalloutlinedbuttonmf.png" width="420">

**`SmallOutlinedButtonMF`**

```kotlin
SmallOutlinedButtonMF(
    text = "Ver todos",
    onClick = { }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_iconButtonMF_button_iconbuttonmf.png" width="420">

**`IconButtonMF`**

```kotlin
IconButtonMF(
    text = "Verificar",   // o icon = painterResource(...)
    onClick = { }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_smallIconButton_button_smalliconbutton.png" width="420">

**`SmallIconButton`**

```kotlin
SmallIconButton(
    icon = painterResource(R.drawable.mi_icono),
    onClick = { }
)
```


<sub>⚠️ <code>icon</code> es obligatorio y es un <code>Painter</code>, no un id de recurso.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_buttonMF_button_buttonmf.png" width="420">

**`ButtonMF`**

```kotlin
ButtonMF(
    text = "Ingresar",
    onClick = { }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_outlinedButtonMF_button_outlinedbuttonmf.png" width="420">

**`OutlinedButtonMF`**

```kotlin
OutlinedButtonMF(
    text = "Cancelar",
    onClick = { }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_buttonCountProduct_button_buttoncountproduct.png" width="420">

**`ButtonCountProduct`**

```kotlin
ButtonCountProduct(
    onClick = { },
    quantity = itemsSeleccionados,
    total = "S/ 50"
)
```


<sub>⚠️ El ícono derecho por defecto ya viene bundleado (<code>uikit_ic_arrow_right</code>) — pasa <code>icon</code> solo si quieres cambiarlo.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_toggleSelector2_button_toggleselector2.png" width="420">

**`ToggleSelector2<T>`**

```kotlin
ToggleSelector2(
    label = "Selecciona una opción",
    options = opciones,
    selectedOption = seleccion,
    onOptionSelected = { seleccion = it },
    optionLabel = { it.nombre }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_buttonOptions_button_buttonoptions.png" width="420">

**`ButtonOptions`**

```kotlin
ButtonOptions(
    onClickCancel = { },
    onClickSave = { }
)
```


<sub>⚠️ Combina <code>OutlinedButtonMF</code> (cancelar) + <code>ButtonMF</code> (guardar) en una fila.</sub>


### Campos de texto (`ui.components.text_field`)

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldMF_text_field_textfieldmf.png" width="420">

**`TextFieldMF`**

```kotlin
TextFieldMF(
    title = "Nombre",
    value = nombre,
    onChangeValue = { nombre = it }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldMFWithError_text_field_textfieldmf_error.png" width="420">

Mismo componente con `showError = true` (usa el string bundleado
`uikit_field_required_error`, sobrescribible desde tu app).

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_searchSection_text_field_searchsection.png" width="420">

**`SearchSection`**

```kotlin
SearchSection(
    modifier = Modifier.fillMaxWidth(),
    title = "Buscar producto",
    onSearch = { query -> },
    onClickIcon = { }       // o label = "Cerrar" en vez de icon
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_editTextSearch_text_field_edittextsearch.png" width="420">

**`EditTextSearch`**

```kotlin
EditTextSearch(
    title = "Buscar…",
    onSearch = { query -> }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldPlate_text_field_textfieldplate.png" width="420">

**`TextFieldPlate`**

```kotlin
TextFieldPlate(
    title = "Placa",
    value = placa,
    onChangeValue = { placa = it }
)
```


<sub>⚠️ Formatea a <code>ABC-123</code> automáticamente al tipear.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldWithButton_text_field_textfieldwithbutton.png" width="420">

**`TextFieldWithButton`**

```kotlin
TextFieldWithButton(
    title = "Código",
    icon = painterResource(R.drawable.ic_barcode),
    onChangeValue = { },
    onClickIcon = { }       // ej. abrir el scanner
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldDate_text_field_textfielddate.png" width="420">

**`TextFieldDate`**

```kotlin
TextFieldDate(
    title = "Fecha de emisión",
    onChangeValue = { },
    icon = painterResource(R.drawable.ic_calendar),
    enabled = true
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldDateTime_text_field_textfielddatetime.png" width="420">

**`TextFieldDateTime`**

```kotlin
TextFieldDateTime(
    title = "Fecha y hora",
    onChangeValue = { },
    icon = painterResource(R.drawable.ic_calendar),
    enabled = true
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldMonth_text_field_textfieldmonth.png" width="420">

**`TextFieldMonth`**

```kotlin
TextFieldMonth(
    title = "Mes y año",
    onChangeValue = { },
    icon = painterResource(R.drawable.ic_calendar),
    enabled = true
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldWithCenteredImage_text_field_textfieldwithcenteredimage.png" width="420">

**`TextFieldWithCenteredImage`**

```kotlin
TextFieldWithCenteredImage(
    title = "Logo",
    imageUri = logoUri,     // null = muestra el placeholder
    onImageSelected = { uri -> }
)
```


<sub>⚠️ Usa <code>rememberLauncherForActivityResult</code> internamente — solo funciona con una <code>Activity</code>/<code>ComponentActivity</code> real detrás (cualquier pantalla normal de la app sirve).</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_switchMF_text_field_switchmf.png" width="420">

**`SwitchMF`**

```kotlin
SwitchMF(
    text = "Notificaciones",
    value = habilitado,
    onCheckedChange = { habilitado = it }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_fieldWidget_text_field_fieldwidget.png" width="420">

**`FieldWidget`**

```kotlin
FieldWidget(
    title = "Cantidad",
    quantity = cantidad,
    onClickedMinus = { cantidad = it },
    onClickedPlus = { cantidad = it },
    onConfirmation = { cantidad = it }
)
```


<sub>⚠️ Combina <code>WidgetCounter</code> + <code>dialog/InputTextDialog</code> (ambos incluidos).</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_widgetCounter_widgetcounter.png" width="420">

**`WidgetCounter`**

```kotlin
WidgetCounter(
    quantity = cantidad,
    stock = stockDisponible,
    stockControl = true,
    showInputDialog = { },
    onClickedMinus = { },
    onClickedPlus = { }
)
```


<sub>⚠️ Vive en <code>ui.components</code> (no en <code>text_field</code>) — es la piecita de <code>FieldWidget</code>, úsala suelta si no necesitas el diálogo de cantidad exacta.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_fieldSearchNumDoc_text_field_fieldsearchnumdoc.png" width="420">

**`FieldSearchNumDoc`**

```kotlin
FieldSearchNumDoc(
    title = "N° de documento",
    number = numero,
    requiredLength = 8,     // vos decides la longitud (DNI, RUC, etc.)
    onChangeValue = { numero = it },
    action = {
        IconButtonMF(text = "Buscar", onClick = { })
    }
)
```


<sub>⚠️ La validación (DNI, RUC, etc.) y el botón de acción los armas vos vía <code>action</code>.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldCountry_text_field_textfieldcountry.png" width="420">

**`TextFieldCountry`**

```kotlin
TextFieldCountry(
    title = "País",
    onValueChange = { country -> }
)
```


<sub>⚠️ Devuelve un <code>Country</code> de <code>country_code_picker/</code> (ver más abajo).</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_textFieldPhoneNumberByCountry_text_field_textfieldphonenumberbycountry.png" width="420">

**`TextFieldPhoneNumberByCountry`**

```kotlin
TextFieldPhoneNumberByCountry(
    title = "Teléfono",
    onValueChange = { (codigo, numero, esValido) -> }
)
```


<sub>⚠️ <code>showError</code> por defecto es <code>true</code> — pásale tu propia condición de validez.</sub>


### Selects (`ui.components.select`)

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_selectMF_select_selectmf.png" width="420">

**`SelectMF<T>`**

```kotlin
SelectMF(
    title = "Categoría",
    options = categorias,
    selectedOption = seleccion?.nombre,
    onOptionSelected = { seleccion = it },
    optionLabel = { it.nombre }
)
```


<sub>⚠️ Con más de 5 <code>options</code> muestra buscador automáticamente.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_selectDialogMF_select_selectdialogmf.png" width="420">

**`SelectDialogMF`**

```kotlin
SelectDialogMF(
    title = "Serie",
    selectedOption = serie,
    onClick = { mostrarDialogo = true }
)
```


<sub>⚠️ Solo dibuja el campo — el diálogo de selección lo armas vos y lo abres en <code>onClick</code>.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_searchDropdownMenu_select_searchdropdownmenu.png" width="420">

**`SearchDropdownMenu<T>`**

```kotlin
SearchDropdownMenu(
    title = "Cliente",
    options = clientes,
    filter = { item, texto -> item.nombre.contains(texto, true) },
    optionLabel = { it.nombre },
    optionSelected = { cliente -> }
)
```


### Items (`ui.components.items`)

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_itemEmptyList_items_itememptylist.png" width="420">

**`ItemEmptyList`**

```kotlin
ItemEmptyList()   // o ItemEmptyList("Sin resultados")
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_radioButtonWithBorderSelected_items_radiobuttonwithborder_selected.png" width="420"><br>
<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_radioButtonWithBorderUnselected_items_radiobuttonwithborder_unselected.png" width="420">

**`RadioButtonWithBorder`**

```kotlin
RadioButtonWithBorder(
    isSelected = seleccion == opcion,
    onClick = { seleccion = opcion },
    label = opcion.nombre
)
```


### Selector de país / teléfono (`ui.components.country_code_picker`)

Subsistema completo de 10 archivos: enum `Country` con ~240 países, validación y formato de números vía `libphonenumber-android`, bottom sheet y diálogo de selección.

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_countryCodePicker_country_code_picker_countrycodepicker.png" width="420">

**`CountryCodePicker`**

```kotlin
CountryCodePicker(
    selectedCountry = Country.Peru,
    onCountrySelected = { country -> }
)
```


<sub>⚠️ Normalmente no lo usas directo — <code>TextFieldPhoneNumberByCountry</code> ya lo trae integrado como <code>leadingIcon</code>.</sub>


La dependencia `io.michaelrocks:libphonenumber-android` va como `implementation` dentro de la librería — no hace falta declararla en tu app, ya viaja incluida.

### Alertas y diálogos (`ui.components.alerts`, `ui.components.dialog`)

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_loaderDialog_alerts_loaderdialog.png" width="420">

**`LoaderDialog`**

```kotlin
LoaderDialog(text = "Cargando…")
```


<sub>⚠️ El ícono gira infinitamente. Trae un ícono de carga bundleado por defecto.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_alertDialog_alerts_alertdialog.png" width="420">

**`AlertDialog`**

```kotlin
AlertDialog(
    description = "Registro realizado con éxito",
    image = painterResource(R.drawable.ic_success),
    onDismissRequest = { },
    onConfirmation = { },
    showConfirmationButton = true,
    buttonSave = "Ok"
)
```


<sub>⚠️ <code>image</code> es obligatorio; el ícono opcional del botón es <code>buttonIcon</code>.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_warningDialog_dialog_warningdialog.png" width="420">

**`WarningDialog`**

```kotlin
WarningDialog(
    description = "¿Seguro que quieres salir sin guardar?",
    onDismissRequest = { },
    onConfirmation = { }
)
```


<sub>⚠️ Es el diálogo de confirmación que usa <code>ToolBarMF</code> internamente cuando <code>onShowDialog = true</code>.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_confirmDialog_dialog_confirmdialog.png" width="420">

**`ConfirmDialog`**

```kotlin
ConfirmDialog(
    title = "Confirmar cambios",
    onDismissRequest = { },
    onConfirmation = { motivo -> }
)
```


<sub>⚠️ Con <code>showTextField = true</code> agrega un <code>TextFieldMF</code> y el motivo llega en <code>onConfirmation</code>.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_nullDialog_dialog_nulldialog.png" width="420">

**`NullDialog`**

```kotlin
NullDialog(
    title = "ANULAR",
    billNumber = "F001-298",
    onDismissRequest = { },
    onConfirmation = { motivo -> }
)
```


<sub>⚠️ Pensado específicamente para anular/nulificar un comprobante (no es un confirm genérico como <code>ConfirmDialog</code>).</sub>


### Toolbar (`ui.components.toolbar`)

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_toolBarMF_toolbar_toolbarmf.png" width="420">

**`ToolBarMF`**

```kotlin
ToolBarMF(
    title = "Productos",
    onReturnScreen = { navController.popBackStack() }
)
```


<sub>⚠️ Con <code>onShowDialog = true</code> muestra un <code>WarningDialog</code> de confirmación antes de salir (back físico incluido, vía <code>BackHandler</code>). <code>icon</code>/<code>onClickIcon</code> agregan una acción a la derecha.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_appTopBar_toolbar_apptopbar.png" width="420">

**`AppTopBar`**

```kotlin
AppTopBar(
    logo = painterResource(R.drawable.mi_logo),
    menuItems = listOf(
        AppTopBarMenuItem(
            id = "catalog",
            label = "Catálogo",
            icon = painterResource(R.drawable.ic_catalog),
            onClick = { /* navegar */ }
        ),
        AppTopBarMenuItem(
            id = "settings",
            label = "Configuración",
            icon = painterResource(R.drawable.ic_settings),
            onClick = { /* navegar */ }
        )
    ),
    bottomMenuItems = listOf(
        AppTopBarMenuItem(
            id = "logout",
            label = "Cerrar sesión",
            icon = painterResource(R.drawable.ic_logout),
            color = Color.Red,
            onClick = { showLogoutConfirm = true }
        )
    ),
    dropdownHeight = 400.dp,   // opcional — sin esto bottomMenuItems no queda pegado abajo
    onProfileClick = { showProfile = true }
)
```


<sub>⚠️ Sin ViewModel ni navegación acoplados — cada <code>AppTopBarMenuItem</code> trae su propio <code>onClick</code> (navegación, checks, lo que necesites). Para el diálogo de "¿cerrar sesión?" usa <code>WarningDialog(description = "¿Seguro que quieres cerrar sesión?", ...)</code>. Para el popup de perfil, ver <code>ProfileDialog</code> abajo.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_profileDialog_dialog_profiledialog.png" width="420">

**`ProfileDialog`**

```kotlin
ProfileDialog(
    name = usuario.nombre,
    email = usuario.email,
    domain = usuario.subdominio,
    logoUri = miLogoUri,   // null = ícono de usuario por defecto
    onDismissRequest = { showProfile = false }
)
```


<sub>⚠️ <code>logoUri</code> es opcional — si es <code>null</code> se muestra un ícono de usuario por defecto. Vive en <code>ui.components.dialog</code>, no en <code>toolbar</code> — normalmente se abre desde el <code>onProfileClick</code> de <code>AppTopBar</code>.</sub>

### Restaurante (`ui.components`, `ui.components.dialog`, `ui.components.items`)

Componentes para listar y gestionar productos de un pedido, con
indicaciones de cocina. Usan el modelo genérico
[`UiKitProduct` / `UiKitComment`](#modelo-de-datos-genérico-uikitproduct--uikitcomment)
de más abajo — sin entidades de Room ni ViewModel.

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_tableProductList_tableproductlist.png" width="420">

**`TableProductList`**

```kotlin
TableProductList(
    items = productos, // List<UiKitProduct>
    onClickDelete = { index -> }
)
```


<sub>⚠️ Lista + footer con el total. Borra por índice — si necesitas borrar por id real, usa la versión de restaurante.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_itemProductRestaurantList_items_itemproductrestaurantlist.png" width="420">

**`ItemProductRestaurantList`**

```kotlin
ItemProductRestaurantList(
    item = producto,
    comments = indicaciones, // List<UiKitComment>
    onClickDelete = { id, commentIndex -> }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_tableRestaurantProductList_tablerestaurantproductlist.png" width="420">

**`TableRestaurantProductList`**

```kotlin
TableRestaurantProductList(
    items = productos,
    onClickDelete = { id, commentIndex -> }
)
```


<sub>⚠️ Expande automáticamente productos con cantidad &gt; 1 en unidades individuales cuando tienen indicaciones distintas (usa <code>RestaurantListUtils.expandProductsWithComments</code>).</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_tableDialog_dialog_tabledialog.png" width="420">

**`TableDialog`**

```kotlin
TableDialog(
    items = productos,
    onClickButton = { /* ir a editar */ },
    onDismissRequest = { },
    onClickDelete = { index -> }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_tableRestaurantDialog_dialog_tablerestaurantdialog.png" width="420">

**`TableRestaurantDialog`**

```kotlin
TableRestaurantDialog(
    items = productos,
    onClickButton = { /* agregar más productos */ },
    onDismissRequest = { },
    onClickDelete = { id, commentIndex -> }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_filterDialogMF_dialog_filterdialogmf.png" width="420">

**`FilterDialogMF<T>`**

```kotlin
FilterDialogMF(
    options = almacenes,
    selectedOption = seleccionado,
    setLabel = { it.nombre },
    onClick = { seleccionado = it }
)
```


<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_dialogInputComment_dialog_dialoginputcomment.png" width="420">

**`DialogInputComment`**

```kotlin
DialogInputComment(
    product = producto,
    quantity = 2,
    category = "Hamburguesas",
    availableComments = indicacionesDisponibles,
    onDismissRequest = { },
    onRequestNewComment = { categoria, texto -> /* guardar y refrescar */ },
    onConfirm = { producto, gruposDeComentarios -> /* persistir */ }
)
```


<sub>⚠️ Todo el estado de red (comentarios disponibles, guardar uno nuevo) se recibe por parámetro/callback; el estado de tabs/selección es interno, por ser pura interacción de UI.</sub>


#### Modelo de datos genérico: `UiKitProduct` / `UiKitComment`

Tu app mapea su propia entidad de producto a esto antes de pasarlo a
cualquier componente de esta sección — ahí es donde va la lógica específica
de tu proyecto (ej. cómo armar el texto de presentación, qué usar como id
cuando el producto es local/offline).

```kotlin
data class UiKitProduct(
    val id: String,               // id único para borrar/ubicar el item en la lista
    val itemTypeId: String,       // clave para agrupar unidades del mismo producto
    val description: String,      // ya resuelto, incluye cualquier sufijo de presentación
    val quantity: BigDecimal,
    val unitPrice: BigDecimal,
    val imageUrl: String? = null,
    val commentGroups: List<UiKitCommentGroup> = emptyList() // indicaciones de cocina, ya parseadas
)

data class UiKitComment(
    val id: Int,
    val description: String,
    val categoryId: Int,
    val isSelected: Boolean = false
)

data class UiKitCommentGroup(
    val itemIndex: Int,
    val comments: List<UiKitComment> = emptyList()
)
```

Ejemplo de mapeo desde una entidad Room propia:

```kotlin
fun MyProductEntity.toUiKitProduct() = UiKitProduct(
    id = if (id == 0) internalId else id.toString(),
    itemTypeId = itemId.toString(),
    description = description ?: "",
    quantity = auxQuantity.toBigDecimal(),
    unitPrice = saleUnitPrice.toBigDecimal(),
    imageUrl = image,
    commentGroups = comment?.let { json.decodeFromString(it) } ?: emptyList()
)
```

### Facturación de restaurante (`ui.components`, `ui.components.card`, `ui.components.items`, `ui.components.dialog`, `ui.components.alerts`)

Componentes para armar y cobrar un documento: selección de producto desde
catálogo, lotes/series, lista de precios y métodos de pago. Ver
[Modelos de facturación](#modelos-de-facturación) más abajo.

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_itemProduct_items_itemproduct.png" width="420">

**`ItemProduct`**

```kotlin
ItemProduct(
    product = producto, // UiKitCatalogProduct
    currentCost = precioActual,
    isChecked = seleccionado,
    quantity = cantidad,
    stockControl = true,
    isEditPriceEnabled = true,
    showInputDialog = { },
    showListPrice = { id -> },
    showCommentDialog = { producto, categoria -> },
    showPriceDialog = { },
    showQuantityByPrice = { },
    showItemLotsDialog = { },
    onCheckedChange = { },
    onClickedMinus = { },
    onClickedPlus = { }
)
```


<sub>⚠️ Card de un producto de catálogo/grilla de selección (distinto de <code>ItemProductList</code>, que muestra una línea ya agregada). Los 4 íconos del footer (editar, comentario, granel, precio por lista) son decorativos y vienen bundleados, no son parámetro.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_dialogFormFreeProduct_dialog_dialogformfreeproduct.png" width="420">

**`DialogFormFreeProduct`**

```kotlin
DialogFormFreeProduct(
    show = mostrar,
    unitOptions = unidadesDeMedida,       // List<UiKitOption>
    affectationOptions = tiposAfectacion, // List<UiKitOption>
    defaultAffectationId = "10",
    onAddProduct = { resultado -> /* mapear a tu propia entidad */ },
    onDismissRequest = { }
)
```


<sub>⚠️ Devuelve un <code>FreeProductFormResult</code> con lo que el usuario ingresó — tu app arma su propia entidad con eso.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_dialogItemLotsSeriesOnly_dialog_dialogitemlots_series.png" width="420"><br>
<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_dialogItemLotsLotsOnly_dialog_dialogitemlots_lots.png" width="420"><br>
<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_dialogItemLotsWithSeries_dialog_dialogitemlots_lotswithseries.png" width="420">

**`DialogItemLots`**

```kotlin
DialogItemLots(
    show = mostrar,
    selectionType = LotsSelectionType.LOTS_WITH_SERIES,
    itemLots = series,           // List<UiKitSeries>
    lotsGroup = lotes,           // List<UiKitLotGroup>
    lotsWithSeries = lotesConSeries, // List<LotWithSeries>
    onConfirmation = { resultado -> /* LotsSelectionResult */ },
    onDismissRequest = { }
)
```


<sub>⚠️ 3 modos según cómo el producto rastrea su stock: solo series, solo lotes, o lotes con series anidadas. Solo maneja la selección — tu app ya sabe para qué producto abrió el diálogo.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_listPriceDialog_dialog_listpricedialog.png" width="420">

**`ListPriceDialog`**

```kotlin
ListPriceDialog(
    show = mostrar,
    options = presentaciones, // List<UiKitPriceOption>
    originalPrice = precioOriginal,
    originalUnitType = "NIU",
    currentPrice = precioActual,
    onSelectItem = { seleccion -> /* SelectedPriceParam */ },
    onDismissRequest = { }
)
```


<sub>⚠️ Cada <code>UiKitPriceOption</code> trae un único <code>price</code> ya resuelto — si tienes varios precios posibles, decide cuál usar al mapear tu entidad.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_paymentCashDialog_dialog_paymentcashdialog.png" width="420">

**`PaymentCashDialog`**

```kotlin
PaymentCashDialog(
    title = "PAGO EN EFECTIVO",
    listPayment = metodosDePago,       // List<UiKitPaymentMethod>
    listDestination = destinos,        // List<UiKitPaymentDestination>
    currentPaymentMethod = null,
    currentPaymentDestinations = null,
    total = totalDelDocumento,
    methodLabel = "Efectivo",
    saveCashPayment = { pagos, etiqueta -> },
    onDismissRequest = { },
    onConfirmation = { }
)
```


<sub>⚠️ Reparte el total automáticamente entre las filas de pago que agregues. Si necesitas filtrar destinos según el método elegido, filtra <code>listDestination</code> antes de pasarla.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_paymentInstallmentsDialog_dialog_paymentinstallmentsdialog.png" width="420">

**`PaymentInstallmentsDialog`**

```kotlin
PaymentInstallmentsDialog(
    title = "PAGO A CRÉDITO",
    total = totalDelDocumento,
    methodLabel = "Crédito",
    saveInstallmentsPayment = { cuotas, etiqueta -> },
    onDismissRequest = { },
    onConfirmation = { }
)
```


<sub>⚠️ Reparte el total entre cuotas automáticamente; cada cuota nueva sugiere la fecha del mes siguiente. <code>dateFormatPattern</code> (default <code>dd/MM/yyyy</code>) es configurable si tu app usa otro formato.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_customTable_customtable.png" width="420">

**`CustomTable<T>`**

```kotlin
CustomTable(
    headers = listOf("Producto", "Cant.", "Precio"),
    weights = listOf(2f, 1f, 1f),
    items = filas,
    rowContent = { fila -> /* tu Row */ }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_outlineIconButtonMF_button_outlineiconbuttonmf.png" width="420">

**`OutlineIconButtonMF`**

```kotlin
OutlineIconButtonMF(
    icon = painterResource(R.drawable.mi_icono),
    onClick = { }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_selectableCard_items_selectablecard.png" width="420">

**`SelectableCard`**

```kotlin
SelectableCard(
    title = cliente.nombre,
    subTitle = cliente.documento,
    isSelected = seleccionado == cliente,
    onClick = { seleccionado = cliente }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_cardPriceMultiple_card_cardpricemultiple.png" width="420">

**`CardPriceMultiple`**

```kotlin
CardPriceMultiple(
    titles = listOf("Valor de Venta" to "IGV 18%"),
    subtitles = listOf("S/ 150" to "S/ 27"),
    total = "S/ 177"
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_dataClient_card_dataclient.png" width="420">

**`DataClient`**

```kotlin
DataClient(
    userName = cliente.nombre,
    userAddress = cliente.direccion,
    onUserChange = { },
    onAddressChange = { direccion -> },
    onEditUser = { /* abrir buscador de cliente */ }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_searchButton_card_searchbutton.png" width="420">

**`SearchButton`**

```kotlin
SearchButton(
    textButton = "Buscar Clientes",
    onClick = { }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_selectSmallMF_select_selectsmallmf.png" width="420">

**`SelectSmallMF<T>`**

```kotlin
SelectSmallMF(
    options = metodosDePago,
    selectedOption = seleccion?.description,
    onOptionSelected = { seleccion = it },
    optionLabel = { it.description }
)
```


<sub>⚠️ Versión compacta de <code>SelectMF</code> sin título ni buscador — la usan las filas de <code>PaymentCashDialog</code>.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_loaderProgressDialog_alerts_loaderprogressdialog.png" width="420">

**`LoaderProgressDialog`**

```kotlin
LoaderProgressDialog(
    isVisible = subiendo,
    progress = progreso, // 0f..1f
    description = "Subiendo imagen..."
)
```


<sub>⚠️ <code>isVisible</code>/<code>progress</code>/<code>description</code> van directo por parámetro, sin necesidad de armar un objeto de estado.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_imagePickerComponent_imagepickercomponent.png" width="420">

**`ImagePickerComponent`**

```kotlin
ImagePickerComponent(
    currentImageUrl = producto.imagen,
    onImageSelected = { archivoOptimizado -> }
)
```


<sub>⚠️ Optimiza la foto (resize + orientación EXIF) antes de devolverla. Para tomar foto con cámara tu app necesita declarar su propio <code>FileProvider</code> en el manifest — ver el KDoc del componente. <code>fileProviderAuthority</code> es opcional si no usas la convención <code>"{packageName}.fileprovider"</code>.</sub>


#### Modelos de facturación

```kotlin
// El precio ya llega resuelto — si tu app maneja varios precios por
// producto (ej. precio1/precio2/precio3), decide cuál usar al mapear.
data class UiKitPriceOption(
    val id: String,
    val unitTypeId: String,
    val description: String,
    val quantityUnit: String,
    val price: BigDecimal
)

// Series y lotes (DialogItemLots).
data class UiKitSeries(val id: Int, val series: String, val date: String)
data class UiKitLotGroup(
    val id: Int,
    val code: String,
    val quantity: BigDecimal,
    val dateOfDue: String,
    val selectedQuantity: Int = 0
)

// Modelos de método de pago (efectivo y cuotas a crédito).
data class UiKitPaymentMethod(val id: String, val description: String)
data class UiKitPaymentDestination(val id: String, val description: String)
data class UiKitCashPayment(
    val payment: UiKitPaymentMethod? = null,
    val destination: UiKitPaymentDestination? = null,
    val payWith: String? = null
)
data class UiKitInstallment(val date: String? = null, val amount: String? = null)

// Producto de catálogo/grilla de selección (distinto de UiKitProduct,
// que es una línea ya agregada a un documento).
data class UiKitCatalogProduct(
    val id: String,
    val description: String,
    val imageUrl: String? = null,
    val stock: BigDecimal = BigDecimal.ZERO,
    val currencySymbol: String = "S/",
    val category: String? = null,
    val calculateQuantity: Boolean = false
)

// Opción simple id/descripción para selects genéricos (unidades de medida,
// tipos de afectación IGV) donde no vale la pena un modelo dedicado.
data class UiKitOption(val id: String, val description: String)
```

### Cards y reportes (`ui.components.card`, `ui.components.items`, `ui.components`)

Cards, reportes y componentes de uso general — no atados a restaurante ni facturación.

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_cardInventory_card_cardinventory.png" width="420">

**`CardInventory`**

```kotlin
CardInventory(
    title = producto.nombre,
    stock = producto.stock.toString(),
    subtitleText = "Ingreso - Salida",
    onInputInventory = { },
    onMoveInventory = { },
    onAdjustmentInventory = { }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_cardList_card_cardlist.png" width="420">

**`CardList`** (+ `InfoRow`)

```kotlin
CardList(
    badgeColor = Color.Red,
    badgeText = "Cerrado",
    headerText = "Estado",
    footerLeftText = "Acción 1",
    footerRightText = "Acción 2",
    onFooterLeftClick = { },
    onFooterRightClick = { }
) {
    Column {
        InfoRow(leftText = "Abrió caja", rightText = "Julio Martínez")
        InfoRow(leftText = "Fecha", rightText = "12/12/2021")
    }
}
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_cardListClient_card_cardlistclient.png" width="420">

**`CardListClient`**

```kotlin
CardListClient(
    title = cliente.nombre,
    subtitle = cliente.documento,
    checked = cliente.activo,
    onCheckedChange = { },
    editIcon = painterResource(R.drawable.ic_edit),
    deleteIcon = painterResource(R.drawable.ic_delete),
    onEditClick = { },
    onDeleteClick = { }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_cardListSimple_card_cardlistsimple.png" width="420">

**`CardListSimple`**

```kotlin
CardListSimple(
    title = item.nombre,
    subtitle = item.detalle,
    editIcon = painterResource(R.drawable.ic_edit),
    deleteIcon = painterResource(R.drawable.ic_delete)
)
```


<sub>⚠️ Con <code>options = false</code>, en vez de los íconos muestra <code>trailingText</code> (ej. "Disponible: 10.00").</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_cardDetails_card_carddetails.png" width="420">

**`CardDetails`**

```kotlin
CardDetails(
    headerText = "F001-123",
    titleText = documento.cliente,
    subtitleText = documento.numero,
    badgeText = "Enviado",
    calendarIcon = painterResource(R.drawable.ic_calendar),
    dateTimeText = documento.fecha,
    additionalSubtitle = documento.detalle,
    actions = listOf(
        CardDetailsAction("print", painterResource(R.drawable.ic_print), showWhenCollapsed = true, onClick = { }),
        CardDetailsAction("whatsapp", painterResource(R.drawable.ic_wsp), onClick = { /* tu propio diálogo */ })
    )
)
```


<sub>⚠️ Cada <code>CardDetailsAction</code> ya viene resuelta por la app — <code>showWhenCollapsed</code> decide si aparece en la fila visible con la card cerrada o solo al expandir. Cada <code>onClick</code> es responsabilidad tuya, incluyendo abrir tus propios diálogos.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_itemCircleReport_items_itemcirclereport.png" width="420">

**`ItemCircleReport`**

```kotlin
ItemCircleReport(
    item = UiKitCircleReportItem(
        title = "Cuentas por Cobrar",
        percentage = BigDecimal(80),
        primaryText = "Margen bruto",
        primaryColor = Color(0xFFE53935),
        secondaryText = "Costo de producto",
        secondaryColor = Color(0xFFFEC6C6),
        number = "150"
    )
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_itemReportDashboard_items_itemreportdashboard.png" width="420">

**`ItemReportDashboard`**

```kotlin
ItemReportDashboard(
    UiKitDashboardCard(
        icon = painterResource(R.drawable.ic_bill),
        trailingIcon = painterResource(R.drawable.logo_sunat), // opcional
        title = "Boletas y Facturas",
        value = "S/ 1400"
    )
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_countSlider_countslider.png" width="420">

**`CountSlider`**

```kotlin
CountSlider(
    label = "Cantidad de mesas:",
    value = cantidad,
    valueRange = 1..50,
    onValueChange = { cantidad = it }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_navigationBottomBar_navigationbottombar.png" width="420">

**`NavigationBottomBar`**

```kotlin
NavigationBottomBar(
    items = listOf(
        UiKitNavItem("home", "Inicio", painterResource(R.drawable.ic_home)),
        UiKitNavItem("new", "Nuevo", painterResource(R.drawable.ic_new)),
        UiKitNavItem("list", "Lista", painterResource(R.drawable.ic_list))
    ),
    selectedRoute = rutaActual,
    onItemClick = { item -> navController.navigate(item.route) }
)
```


<sub>⚠️ Tu app arma su propia lista de <code>UiKitNavItem</code> — no hay pantallas fijas.</sub>

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_openSalesBoxDialog_dialog_opensalesboxdialog.png" width="420">

**`OpenSalesBoxDialog`**

```kotlin
OpenSalesBoxDialog(
    title = "Abrir caja",
    inputLabel = "Monto inicial",
    currencyLabel = "Efectivo",
    // readOnlyMessage = "¿Seguro que deseas cerrar la caja?" — modo "solo confirmar", sin campo de monto
    buttonClose = "No",
    buttonSave = "Sí",
    onDismissRequest = { },
    onConfirmation = { monto -> }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_singleTextInputDialog_dialog_singletextinputdialog.png" width="420">

**`SingleTextInputDialog`**

```kotlin
SingleTextInputDialog(
    description = "Ingresa tu correo",
    onDismissRequest = { },
    onConfirmation = { texto -> }
)
```


---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_stateTypeDialog_dialog_statetypedialog.png" width="420">

**`StateTypeDialog<T>`**

```kotlin
StateTypeDialog(
    title = "Actualizar estado",
    options = estados,
    label = estadoActual,
    optionLabel = { it.nombre },
    onOptionSelected = { nuevoEstado = it },
    onDismissRequest = { },
    onConfirmation = { /* guardar nuevoEstado */ }
)
```

---

<img src="uikit/src/test/snapshots/images/org.mindtec.uikit_PreviewGalleryTest_phoneNumberDialog_dialog_phonenumberdialog.png" width="420">

**`PhoneNumberDialog`**

```kotlin
PhoneNumberDialog(
    title = "Enviar por WhatsApp",
    onDismissRequest = { },
    onConfirmation = { numero -> }
)
```


<sub>⚠️ <code>phoneLength</code> (default 9, como en Perú) es configurable.</sub>

## Desarrollo local

```bash
./gradlew :uikit:assembleRelease       # compila el AAR
./gradlew :uikit:testDebugUnitTest     # corre los tests (incluye Paparazzi)
./gradlew :uikit:publishToMavenLocal   # valida la publicación sin salir de tu máquina
```
