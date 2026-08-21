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
| `ToolBarMF` | `toolbar` | CommandView, SearchDishes, AddTakeAway, AddDelivery, DeliveryDetail |
| `LoaderDialog` | `alerts` | TakeAwayDetail, SearchDishes, TakeAwayManagement, TableDetail, TableManagement |
| `AlertDialog` | `alerts` | AddTakeAway, TakeAwayManagement, AddDelivery, DeliveryDetail |
| `ButtonMF` | `button` | TakeAwayDetail, AddTakeAway, TableManagement, AddDelivery |
| `ButtonCountProduct` | `button` | SearchDishes |
| `SmallIconButton` | `button` | TableDetail, GenericRestaurantDetailScreen |
| `ToggleSelector2` | `button` | AddDelivery |
| `ButtonOptions` | `button` | DeliveryDetail, GenericRestaurantDetailScreen |
| `ItemEmptyList` | `items` | TableManagement |
| `FieldWidget` | `text_field` | TableManagement |
| `ConfirmDialog` | `dialog` | GenericRestaurantDetailScreen |
| `NullDialog` | `dialog` | GenericRestaurantDetailScreen |
| `SmallOutlinedButtonMF` | `button` | TableManagement — **ya migrado** ✅ |

→ **Wave 2 de migración** (13 componentes, 12 pendientes).

### Necesitan modelo genérico antes de migrarse (`data.*` confirmado)
| Componente | Depende de | Nota |
|---|---|---|
| `TableProductList.kt` (incluye `BoxItemsProducts`) | `ProductEntity` | Usado en DeliveryDetail |
| `FilterDialogMF` | `WarehouseEntity` | Usado en TableManagement, DeliveryDetail — necesita `UiKitWarehouse` genérico |
| `ItemProductRestaurantList` | `ProductEntity`, `RestaurantNote` (ver abajo) | Item de producto para mesas |
| `TableRestaurantDialog` | `ProductEntity` | Usado en SearchDishes |
| `TableRestaurantProductList` | `ProductEntity` | — |
| `TableDialog` | `ProductEntity` | — |
| `DialogInputComment` | `ProductEntity`, `RestaurantNoteResponse`, **+ `BaseViewModel`, `InsertCommentsUiState`, `CommentDialogUiState`** | El más acoplado del grupo: mezcla UI de comentarios de cocina con un ViewModel real. Requiere separar en "UI de selección de notas" (genérico) + lógica de guardado (se queda en la app). Contiene también la `data class RestaurantNote` (línea 598) que debe pasar a ser un modelo genérico de la librería (`UiKitComment` o similar). |

→ **Wave 3 de migración** (7 componentes; empezar por los 4 de solo
`ProductEntity`, dejar `DialogInputComment` para el final del wave por ser
el más grande).

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
(`WidgetCounter`, `InputTextDialog`) están migrados y compilando. Detalle en
el README.

Siguiente: **Wave 2** — shell de modo restaurante sin acoplamiento a datos
(`ToolBarMF`, `LoaderDialog`, `AlertDialog`, `ButtonMF`, `ButtonCountProduct`,
`SmallIconButton` ✅ ya migrado, `ToggleSelector2`, `ButtonOptions`,
`ItemEmptyList` ✅ ya migrado, `FieldWidget` ✅ ya migrado, `ConfirmDialog`,
`NullDialog`).
