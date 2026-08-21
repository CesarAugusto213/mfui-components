# mfui-components

Librería de componentes de Jetpack Compose (botones, diálogos, toolbars, etc.),
extraída de MypeFact para poder reusarse entre varios proyectos Android sin
copiar y pegar carpetas.

## Estrategia de migración

Ver [MIGRATION_STRATEGY.md](MIGRATION_STRATEGY.md) para el mapa completo de
qué componentes migrar primero (modo restaurante + entrada de datos), las
reglas de genericidad y el orden de oleadas.

## Estado actual

**Wave 1 completa** (ver `MIGRATION_STRATEGY.md`). Migrados y compilando:

- Tema base: `Color.kt`, `Fonts.kt`, `UiKitTheme.kt`.
- `button/`: `SmallOutlinedButtonMF`, `IconButtonMF`, `SmallIconButton`.
- `items/`: `ItemEmptyList`, `RadioButtonWithBorder`.
- `text_field/`: `TextFieldMF`, `SearchSection`, `EditTextSearch`, `TextFieldDate`,
  `TextFieldDateTime`, `TextFieldMonth`, `TextFieldPlate`, `TextFieldWithCenteredImage`,
  `TextFieldWithButton`, `SwitchMF`, `FieldWidget`, `FieldSearchNumDoc` (rediseñado,
  genérico), `TextFieldCountry`, `TextFieldPhoneNumberByCountry`.
- `select/`: `SelectMF`, `SelectDialogMF`, `SearchDropdownMenu`.
- `country_code_picker/`: las 10 clases completas (selector de país/teléfono,
  ya era un subsistema genérico y autocontenido — solo se le cambió el paquete).
- Soporte: `WidgetCounter`, `dialog/InputTextDialog` (extraído solo la parte
  genérica de `DialogInput.kt`, sin el wrapper atado a `ItemProductsUiState`).

Código muerto detectado y **no migrado a propósito** (cero imports en todo
MypeFact — candidatos a borrar allá): `TextFieldPrefix`, `TextFieldPassword`,
`SelectSmallMF`, `SelectSearchMF`, `CheckboxMF`.

Nuevas dependencias que trajo este wave: `io.michaelrocks:libphonenumber-android`
(validación/formato de teléfono) y `androidx.activity:activity-compose`
(selector de imagen en `TextFieldWithCenteredImage`).

Siguiente parada: **Wave 2** — shell de modo restaurante (`ToolBarMF`,
`LoaderDialog`, `AlertDialog`, `ButtonMF`, `ButtonCountProduct`, etc.).

## Convención de paquetes (para migrar componentes fácil)

El namespace base cambia de `org.mindtec.mypefact` a `org.mindtec.uikit`,
pero la estructura de subpaquetes se mantiene idéntica:

| MypeFact (origen)                          | mfui-components (destino)              |
|---------------------------------------------|------------------------------------------|
| `org.mindtec.mypefact.ui.theme`             | `org.mindtec.uikit.ui.theme`             |
| `org.mindtec.mypefact.ui.components.button` | `org.mindtec.uikit.ui.components.button` |
| `org.mindtec.mypefact.ui.components.dialog` | `org.mindtec.uikit.ui.components.dialog` |
| `org.mindtec.mypefact.utils`                | `org.mindtec.uikit.utils`                |

Para migrar un componente nuevo: copiar el archivo, reemplazar
`org.mindtec.mypefact` por `org.mindtec.uikit` en el `package` y en los
imports, y resolver a mano cualquier dependencia a `R` (recursos del app
host), a `data.*` (entidades propias) o a ViewModels/pantallas de MypeFact.

### Reglas rápidas para desacoplar un componente

1. **`R.string` / `R.drawable` fijos**: la librería puede traer sus propios
   recursos por defecto (`uikit/src/main/res/values/strings.xml`, etc.).
   Un proyecto consumidor puede sobrescribirlos definiendo un recurso con el
   mismo nombre — no hace falta convertir cada texto en parámetro.
2. **Iconos**: en vez de recibir un `Int` (`@DrawableRes`), los componentes
   de este kit reciben un `Painter` ya resuelto por el caller
   (`icon = painterResource(R.drawable.mi_icono)`). Es un cambio de API
   intencional respecto al original: desacopla el componente de
   `painterResource` como implementación fija.
3. **Entidades propias** (`ProductEntity`, etc.): el componente debe recibir
   un modelo/interfaz genérico definido en la librería; cada app mapea su
   propia entidad a ese modelo antes de pasarlo.
4. **ViewModels / navegación / Hilt** (caso `ToolBarHome`): separar en un
   componente genérico (título, ícono, acciones vía lambdas) + la lógica de
   negocio se queda en cada app armando esos callbacks.
5. **Íconos decorativos que NUNCA fueron parámetro en el original** (ej. la
   lupa fija de `EditTextSearch`, la flecha de `SelectMF`): esos SÍ se
   bundlean como recurso propio de la librería (`uikit_ic_*`), no se
   convierten en parámetro nuevo — no era un punto de personalización en el
   diseño original, así que agregarlo sería scope creep. Solo se vuelve
   parámetro (`Painter`, regla 2) un ícono que YA era `Int`/`@DrawableRes`
   configurable en el componente original.

## Ver los componentes renderizados (previews)

Todos los componentes tienen `@Preview` — si abres el proyecto en Android
Studio los ves ahí directamente. Como este flujo también se maneja por CLI,
el módulo `:uikit` además tiene [Paparazzi](https://github.com/cashapp/paparazzi)
configurado, que renderiza los `@Composable` a PNG usando layoutlib sin
necesitar emulador ni dispositivo:

```bash
./gradlew :uikit:recordPaparazziDebug   # genera/actualiza los PNG de referencia
./gradlew :uikit:verifyPaparazziDebug   # compara contra esos PNG (test de regresión visual)
```

Los tests están en `uikit/src/test/kotlin/org/mindtec/uikit/PreviewGalleryTest.kt`
(una muestra representativa de 22 componentes, no los 100% — se puede ampliar).
Los PNG quedan en `uikit/src/test/snapshots/images/` y están versionados en git
como referencia visual.

> **Nota:** el `compileSdk` del módulo está fijado en **35**, no 36. Paparazzi
> 1.3.5 tiene un bug conocido con compileSdk 36
> ([cashapp/paparazzi#1877](https://github.com/cashapp/paparazzi/issues/1877),
> arreglado recién en la rama 2.0.0-alpha, que a su vez no es compatible con
> Kotlin 2.0.0 de este proyecto). Ninguno de los componentes usa APIs
> exclusivas de API 36, así que no hay pérdida real por ahora — si algún día
> se sube el `compileSdk`, revisar si Paparazzi ya lo soporta.

## Build local

```bash
./gradlew :uikit:assembleRelease
./gradlew :uikit:publishToMavenLocal   # valida la publicación sin salir de tu máquina
```

## Publicar en GitHub + JitPack

1. Crear el repo en GitHub (público, o privado si tienes cuenta JitPack Pro)
   y subir este proyecto.
2. Crear un tag de release, por ejemplo `v0.1.0`, y publicarlo en GitHub.
3. JitPack construye el tag automáticamente la primera vez que alguien lo pide
   (puedes forzar el build entrando a `https://jitpack.io/#tu-usuario/mfui-components`).

## Consumir desde otro proyecto

En el `settings.gradle.kts` del proyecto consumidor:

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
    implementation("com.github.tu-usuario:mfui-components:v0.1.0")
}
```
