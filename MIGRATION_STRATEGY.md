# Estrategia de migración — de MypeFact a mfui-components

Fuente: `composeApp/src/androidMain/kotlin/org/mindtec/mypefact/ui/components/`
del proyecto MypeFact (104 archivos). Este documento prioriza qué migrar
primero y define las reglas para que cada componente quede genérico y
reutilizable en cualquier proyecto, no solo en MypeFact.

## Principios de genericidad (aplican a TODO componente migrado)

1. **Sin `R.string`/`R.drawable` fijos como valor por defecto de negocio.**
   La librería puede traer sus propios recursos con nombres propios; un
   proyecto consumidor los sobrescribe definiendo un recurso con el mismo
   nombre. Nunca hardcodear un string de copy específico de MypeFact.
2. **Iconos como `Painter`, no `Int` (`@DrawableRes`).** El caller resuelve
   `painterResource(...)` y se lo pasa ya armado al componente (igual que
   se hizo con `SmallOutlinedButtonMF`).
3. **Sin entidades de Room ni modelos de red del app host.** Si un
   componente usa `ProductEntity`, `WarehouseEntity`, `RestaurantNoteResponse`,
   etc., se reemplaza por un modelo genérico definido en la librería
   (`UiKitProduct`, `UiKitOption`, etc.) y cada app mapea su entidad real a
   ese modelo antes de pasarlo al componente.
4. **Sin ViewModel, Hilt ni navegación inyectados.** Todo el estado entra
   por parámetros y sale por lambdas (`onClick`, `onValueChange`, etc.). Si
   un componente importa un ViewModel o `NavHostController` directamente
   (ej. `DialogInputComment`, `ToolBarHome`), se separa en: componente
   genérico (UI pura) + la lógica de negocio se queda en la app.
5. **Un componente, un propósito.** Si un archivo mezcla UI genérica con
   lógica específica (ej. `DialogInputComment` mezclando comentarios de
   cocina de restaurante con un `BaseViewModel`), se divide en piezas más
   chicas antes de migrar.

Checklist rápido por componente al migrarlo:
- [ ] Cambiar paquete `org.mindtec.mypefact.*` → `org.mindtec.uikit.*`
      (misma estructura de subpaquetes, ver README).
- [ ] Revisar imports de `R` → mover a recursos propios o parámetro.
- [ ] Revisar imports de `data.*` → reemplazar por modelo genérico.
- [ ] Revisar imports de `ui.screens.*` → eliminar, hoistear estado.
- [ ] Compilar (`./gradlew :uikit:assembleRelease`) antes de dar por
      terminado el componente.

---

## Mapa 1 — Componentes críticos de "modo restaurante"

Verificado por import real en `ui/screens/restaurant/` (incluye `table/`,
`take_away/`, `delivery_incomplete/`) más la pantalla transitiva
`GenericRestaurantDetailScreen` (a la que `TableDetailScreen` y
`TakeAwayDetailScreen` delegan su UI).

### Ya listos para migrar tal cual (sin `data.*` ni `ui.screens.*`)
| Componente | Paquete origen | Usado en |
|---|---|---|
| `ToolBarMF` | `toolbar` | CommandView, SearchDishes, AddTakeAway, AddDelivery, DeliveryDetail — **migrado** ✅ |
| `LoaderDialog` | `alerts` | TakeAwayDetail, SearchDishes, TakeAwayManagement, TableDetail, TableManagement — **migrado** ✅ |
| `AlertDialog` | `alerts` | AddTakeAway, TakeAwayManagement, AddDelivery, DeliveryDetail — **migrado** ✅ |
| `ButtonMF` | `button` | TakeAwayDetail, AddTakeAway, TableManagement, AddDelivery — **migrado** ✅ |
| `ButtonCountProduct` | `button` | SearchDishes — **migrado** ✅ |
| `SmallIconButton` | `button` | TableDetail, GenericRestaurantDetailScreen — migrado en Wave 1 ✅ |
| `ToggleSelector2` | `button` | AddDelivery — **migrado** ✅ |
| `ButtonOptions` | `button` | DeliveryDetail, GenericRestaurantDetailScreen — **migrado** ✅ |
| `ItemEmptyList` | `items` | TableManagement — migrado en Wave 1 ✅ |
| `FieldWidget` | `text_field` | TableManagement — migrado en Wave 1 ✅ |
| `ConfirmDialog` | `dialog` | GenericRestaurantDetailScreen — **migrado** ✅ |
| `NullDialog` | `dialog` | GenericRestaurantDetailScreen — **migrado** ✅ |
| `SmallOutlinedButtonMF` | `button` | TableManagement — migrado en Wave 1 ✅ |

✅ **Wave 2 completa** — los 13 componentes de esta tabla están migrados,
compilando y con captura Paparazzi. De paso se migraron 2 dependencias más
que este grupo arrastraba: `OutlinedButtonMF` (la necesita `ButtonOptions`)
y `WarningDialog` (la necesita `ToolBarMF`).

### Necesitan modelo genérico antes de migrarse (`data.*` confirmado)
| Componente | Depende de | Nota |
|---|---|---|
| `TableProductList.kt` (incluye `BoxItemsProducts`) | `ProductEntity` | Usado en DeliveryDetail — **migrado** ✅ |
| `FilterDialogMF` | `WarehouseEntity` (solo vía `ShowSelectWarehouse`) | El `FilterDialogMF<T>` en sí ya era 100% genérico — **migrado** ✅. `ShowSelectWarehouse` (wrapper de 15 líneas atado a `WarehouseEntity`) NO se migró, es trivial de recrear en la app consumidora. |
| `ItemProductRestaurantList` | `ProductEntity`, `RestaurantNote` (ver abajo) | Item de producto para mesas — **migrado** ✅ |
| `TableRestaurantDialog` | `ProductEntity` | Usado en SearchDishes — **migrado** ✅ |
| `TableRestaurantProductList` | `ProductEntity` | — **migrado** ✅ |
| `TableDialog` | `ProductEntity` | — **migrado** ✅ |
| `DialogInputComment` | `ProductEntity`, `RestaurantNoteResponse`, **+ `BaseViewModel`, `InsertCommentsUiState`, `CommentDialogUiState`** | El más acoplado del grupo — **migrado** ✅, rediseñado: recibe todo por parámetro/callback (`availableComments`, `onRequestNewComment`, `onConfirm`) en vez de un `BaseViewModel`. El estado de tabs/selección se quedó interno por ser pura interacción de UI. |

✅ **Wave 3 completa.** Modelo genérico definido en `ui.components.model`:
`UiKitProduct` (reemplaza `ProductEntity`), `UiKitComment`/`UiKitCommentGroup`
(reemplazan `RestaurantNote`/`RestaurantNoteResponse`/`ProductCommentGroup`
— antes eran 3 clases casi idénticas, ahora 2). `RestaurantUtils.expandProductsWithComments`
se portó a `utils.RestaurantListUtils` — ya no necesita parsear JSON porque
`UiKitProduct.commentGroups` llega pre-parseado. También se migraron 2
dependencias que este grupo arrastraba: `ItemProductList` (usa `TableProductList`)
y `CloseButton` (usa `TableDialog`/`TableRestaurantDialog`).

Bug encontrado y corregido de paso: `TableProductList`/`TableRestaurantProductList`
emitían la lista y el footer como dos nodos sueltos (sin un `Column` propio),
así que solo se apilaban bien por casualidad, porque ambos call sites
reales (`TableDialog`, `TableRestaurantDialog`) ya los envuelven en un
`Column`. Se envolvieron en su propio `Column` interno para que funcionen
igual sin importar el contenedor — mismo patrón de fix que
`TextFieldPhoneNumberByCountry` en Wave 1.

---

## Mapa 2 — Componentes de entrada de datos (text_field / select)

Ranking por cantidad de pantallas que los importan en TODO el proyecto
(no solo restaurante) — indica qué tan crítico es cada uno.

### Alta prioridad (uso masivo, migrar primero)
| Componente | Pantallas que lo usan |
|---|---|
| `TextFieldMF` | 25 |
| `SelectMF` | 17 |
| `SearchSection` | 12 |
| `TextFieldDate` | 7 |
| `FieldSearchNumDoc` | 5 |
| `EditTextSearch` | 5 |

### Prioridad media
| Componente | Pantallas |
|---|---|
| `TextFieldWithCenteredImage` | 2 |
| `TextFieldPlate` | 2 |
| `TextFieldPhoneNumberByCountry` | 2 (arrastra todo `country_code_picker/`, 10 archivos, migrar como bloque) |
| `TextFieldMonth` | 2 |
| `SwitchMF` | 2 |
| `SelectDialogMF` | 2 |
| `FieldWidget` | 2 (ya cubierto en Mapa 1) |

### Prioridad baja (uso puntual, pero sin bloqueadores)
| Componente | Pantallas |
|---|---|
| `TextFieldWithButton` | 1 |
| `TextFieldDateTime` | 1 |
| `TextFieldCountry` | 1 |
| `SearchDropdownMenu` | 1 |

### Código muerto — NO migrar, candidatos a borrar en MypeFact
Cero imports en todo el proyecto (verificado):
`TextFieldPrefix`, `TextFieldPassword`, `SelectSmallMF`, `SelectSearchMF`,
`CheckboxMF`. Si en algún momento limpias MypeFact, estos 5 archivos son
buenos candidatos a eliminar directamente allá.

→ **Wave 1 de migración** (16 componentes útiles de `text_field/` + `select/`,
ninguno depende de `data.*` ni `ui.screens.*` — confirmar igual al migrar).

---

## Orden de oleadas recomendado

1. **Wave 1 — Entrada de datos** (16 componentes: text_field/select de uso
   alto y medio, más el bloque `country_code_picker/` completo). Es la base
   que casi todo lo demás termina usando.
2. **Wave 2 — Shell de restaurante** (12 componentes sin acoplamiento:
   toolbar, alerts, botones, `ItemEmptyList`, `ConfirmDialog`, `NullDialog`).
3. **Wave 3 — Datos de restaurante** (7 componentes, requieren definir
   `UiKitProduct`, `UiKitWarehouse` y `UiKitComment` genéricos primero;
   `DialogInputComment` al final por ser el más grande).
4. **Wave 4 — Resto de uso general**: `card/` (9), `alerts/` restante,
   resto de `dialog/` (16 archivos que no tocan restaurante), `items/`
   restante, componentes sueltos que falten.
5. **Wave 5 — `ToolBarHome`**: el único con Hilt + navegación + ViewModel
   directos. Se separa en `AppTopBar` genérico (título, ícono, acciones vía
   lambdas) y la lógica de logout/navegación se queda en cada app.

## Siguiente paso concreto

✅ **Wave 1 completa** — los 16 componentes de `text_field/`+`select/` más el
bloque `country_code_picker/` (10 archivos) y sus dependencias de soporte
(`WidgetCounter`, `InputTextDialog`) están migrados y compilando.

✅ **Wave 2 completa** — los 13 componentes del shell de restaurante
(`ToolBarMF`, `LoaderDialog`, `AlertDialog`, `ButtonMF`, `ButtonCountProduct`,
`ToggleSelector2`, `ButtonOptions`, `ConfirmDialog`, `NullDialog`, más
`OutlinedButtonMF` y `WarningDialog` como dependencias) están migrados,
compilando y con captura Paparazzi. Detalle de ambos en el README.

✅ **Wave 3 completa** — los 7 componentes de restaurante atados a datos
(`TableProductList`/`BoxItemsProducts`, `FilterDialogMF`,
`ItemProductRestaurantList`, `TableRestaurantDialog`,
`TableRestaurantProductList`, `TableDialog`, `DialogInputComment`) están
migrados sobre el modelo genérico `UiKitProduct`/`UiKitComment`/`UiKitCommentGroup`
(paquete `ui.components.model`), compilando y con captura Paparazzi. No
hizo falta un `UiKitWarehouse` — `FilterDialogMF<T>` resultó ser ya
genérico. `DialogInputComment` quedó completamente libre de ViewModel.

✅ **Wave 4a completa** — componentes que el flujo real de facturación de
restaurante usa (`TableManagementViewModel.generatePayment()` →
`GenericCreateScreen`/`GenericSearchProductScreen`/`GenericSearchClientScreen`/
`GenericAddProductScreen`), verificado por import real, no solo por estar en
`ui/screens/generic/`:

| Componente | Depende de | Nota |
|---|---|---|
| `CustomTable<T>` | nada | ya era genérico — migrado tal cual |
| `OutlineIconButtonMF` | — | icono `Int` → `Painter` |
| `SelectableCard` | nada | migrado tal cual |
| `CardPriceMultiple` | nada | migrado tal cual |
| `DataClient` | nada | iconos bundleados (`uikit_ic_client`/`uikit_ic_edit`/`uikit_ic_local`) |
| `SearchButton` | nada | icono `Int?` → `Painter?` |
| `SelectSmallMF<T>` | nada | ya era genérico — **no era código muerto**, lo usa `PaymentCashDialog` |
| `LoaderProgressDialog` | `LoaderUiState` | estado aplanado a 3 parámetros directos |
| `ImagePickerComponent` | `FileUtils` (parcial) | `optimizeImageForUpload`/`createImageFile` portados a `utils.ImageUtils` (sin Crashlytics/Constants); requiere que la app declare su propio `FileProvider` |
| `ItemProduct` | `ProductEntity` | nuevo modelo `UiKitCatalogProduct`; 4 íconos de footer bundleados (decorativos, no eran parámetro en el original) |
| `DialogFormFreeProduct` | `ProductEntity`, `UnitType`, `Constants.listTypeAffectationTypes` | ya no construye la entidad — devuelve `FreeProductFormResult`; catálogos de unidad/afectación entran por parámetro (`List<UiKitOption>`) |
| `DialogItemLots` (+ `LotsDialogComponents.kt`) | `ItemLotsEntity`, `LotsGroupEntity`, `ItemProductsUiState` | nuevo modelo `UiKitSeries`/`UiKitLotGroup`; ya no recibe/devuelve `ItemProductsUiState` — solo maneja la selección |
| `ListPriceDialog` (+ `DialogListPriceContent`/`TableListPrice`/`TableRow`) | `ItemTypesEntity`, `ItemProductsUiState`, `ListPriceDialogUiState` | nuevo modelo `UiKitPriceOption` (precio ya resuelto, sin `price1/2/3`+`priceDefault`); estado aplanado, sin wrapper `ListPriceDialogUiState` |
| `PaymentCashDialog` | `PaymentMethodEntity`, `PaymentDestinationsEntity`, `CashState`, `DataPaymentMethodUiState` | nuevo modelo `UiKitPaymentMethod`/`UiKitPaymentDestination`/`UiKitCashPayment`; se quitó el filtro "Transferencia → solo BANCO" (lógica de negocio específica) |
| `PaymentInstallmentsDialog` | `QuotasState`, `DataPaymentMethodUiState` | nuevo modelo `UiKitInstallment`; `dateFormatPattern` configurable (antes usaba `Constants.DATE_FORMAT` fijo) |

Modelos nuevos en `ui.components.model`: `UiKitPriceOption`, `UiKitSeries`,
`UiKitLotGroup`, `UiKitPaymentMethod`, `UiKitPaymentDestination`,
`UiKitCashPayment`, `UiKitInstallment`, `UiKitCatalogProduct`, `UiKitOption`.
Nueva dependencia: `androidx.exifinterface:exifinterface` (orientación EXIF
en `ImagePickerComponent`).

✅ **Wave 5 completa** — `ToolBarHome` (el único componente con
`hiltViewModel()` + `NavHostController` + `Activity`s propias de MypeFact
incrustados) se separó en:

| Componente | Reemplaza | Nota |
|---|---|---|
| `AppTopBar` (+ `AppTopBarMenuItem`) | `ToolBarHome` | Sin ViewModel/nav/Activity — cada `AppTopBarMenuItem` trae su propio `onClick`, la app arma la lista con su navegación real. Se quitó el hack de `Box(width=280.dp, height=680.dp)` con un `Spacer(weight=1f)` fijo para separar "sync/logout" del resto: ahora es `bottomMenuItems` + `dropdownHeight` opcional. También se quitó el guard `enabled`/`selectedOption`/`locked` (lógica de negocio de "hay que sincronizar antes de navegar") — cada `onClick` de la app puede implementarlo si lo necesita. |
| `ProfileDialog` | `ProfileDialog` (el de MypeFact) | Ya era casi genérico — el único cambio es que `logoUri` entra por parámetro en vez de leerse de `Constants.IMAGE_PATH`. |

**No se migró** `LogOutDialog`: es funcionalmente un `WarningDialog` con
descripción fija ("¿Está seguro de cerrar sesión?") — portarlo hubiera sido
duplicar un componente que ya existe en el kit desde Wave 2. La recomendación
para consumidores queda documentada en el catálogo del README.

✅ **Wave 4b completa** — resto de componentes de uso general con caller
real confirmado (verificado por import, no solo por estar en la carpeta):

| Componente | Depende de | Nota |
|---|---|---|
| `CardInventory` | nada (usa `SmallIconButton`, ya migrado) | íconos bundleados con override opcional (`inputIcon`/`moveIcon`/`adjustmentIcon`) |
| `CardList` (+ `InfoRow`) | nada | migrado tal cual |
| `CardListClient` | nada | íconos `Int` → `Painter` (`editIcon`/`deleteIcon` ahora requeridos) |
| `CardListSimple` | `R.string.label_available_stock` | el string con formato de negocio se quitó — ahora es un `trailingText: String?` que arma la app |
| `CardDetails` | Filtraba acciones comparando identidad de `R.drawable` contra `isAccept`/`isRegistered`/`isEnabledDispatch`; abría un `WhatsappDialog` propio al tocar el ícono de WhatsApp | Rediseño real: `List<CardDetailsAction>` con `showWhenCollapsed: Boolean` reemplaza la comparación de íconos; se quitaron `isAccept`/`isRegistered`/`isEnabledDispatch`/`showWhatsappDialog`/`onWhatsappConfirmation` — la app pre-filtra las acciones y maneja sus propios diálogos vía `onClick` |
| `ItemCircleReport` | `ItemsCircleReport` | nuevo modelo `UiKitCircleReportItem` (se sacó el `Modifier` del modelo, ahora es parámetro del composable) |
| `ItemReportDashboard` | `ItemCardDashboard` | nuevo modelo `UiKitDashboardCard`, íconos `Int` → `Painter` |
| `CountSlider` | Texto "Cantidad de mesas:" y rango `1f..50f` hardcodeados | ahora `label: String` y `valueRange: IntRange = 1..50` son parámetros |
| `NavigationBottomBar` | `Utils.navigationItems`, `NavigationBottomBarRouter` (5 pantallas fijas de MypeFact) | nuevo modelo `UiKitNavItem`; la app arma su propia lista en vez de un sealed class fijo |
| `OpenSalesBoxDialog` | `isValidNumberInput()` (extension propia), `R.string.cash` | validación decimal inlineada; el label de moneda ahora es `currencyLabel: String`; el modo "solo confirmar" se activa con `readOnlyMessage != null` en vez de comparar `texDialog.isEmpty()` |
| `SingleTextInputDialog` (antes `SendEmailDialog`) | nada | renombrado — no tenía nada específico de email, es un input de una línea genérico |
| `StateTypeDialog<T>` | nada | ya era genérico — migrado tal cual, con `selectLabel` parametrizado |
| `PhoneNumberDialog` (antes `WhatsappDialog`) | Validación de 9 dígitos y mensaje de error hardcodeados | `phoneLength: Int = 9` y `invalidPhoneMessage` ahora son parámetros |

Modelos nuevos: `UiKitCircleReportItem`, `UiKitDashboardCard`, `UiKitNavItem`;
más `CardDetailsAction` (definido junto a `CardDetails`, no en `model/`, por
ser específico de ese componente — mismo criterio que `LotWithSeries` en
Wave 4a). Íconos nuevos: `uikit_ic_arrow_up`, `uikit_ic_input_inventory`,
`uikit_ic_move_inventory`, `uikit_ic_adjustment_inventory`. Sin
dependencias nuevas.

**No migrados — evaluados y descartados por ser demasiado específicos de
MypeFact** (quedan como candidatos si algún día se necesitan, pero requieren
más rediseño del que vale la pena sin un caso de uso real):
- `CardDetailsQuotesOrdersList`: hardcodea 7 etiquetas contables en español
  ("Comprobante", "Notas de venta", "Importe Gravado", etc.) directo en
  `Text()`, sin parametrizar, y depende de `TableProductDispatch` (pantalla
  de la app) + `ProductEntity`.
- `ReportsDialog`: 5 botones fijos para tipos de reporte PDF específicos de
  MypeFact (`onClickPdfA4`, `onClickPdfTicket`, `onClickPdfIncome`,
  `onClickProductsPdfA4`, `onClickProductsPdfTicket`) — es un formulario de
  negocio, no un componente de UI reutilizable.
- `UbigeoDialog`: selector cascada departamento/provincia/distrito atado a
  `BaseDispatchViewModel` + `DepartmentEntity`/`ProvinceEntity`/
  `DistrictEntity` + `TaxDomicileResponse` — sistema de ubigeo específico de
  Perú/SUNAT con lógica de formato de dirección incrustada.

Código muerto confirmado (cero imports reales, candidatos a borrar en
MypeFact, no a migrar): `ItemAddProduct`, `CustomLineChart`,
`ItemLineReport`, `CreateBillDialog`, `CalendarDialog`,
`CalendarDialogSelection`.

Con Waves 1–5 y 4a–4b completas, prácticamente todo lo reutilizable de
`ui/components/` ya está en la librería. Lo único que falta es lo
explícitamente descartado arriba por ser demasiado específico.
