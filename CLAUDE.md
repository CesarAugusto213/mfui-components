# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`mfui-components` is a Jetpack Compose UI component library (buttons, dialogs, toolbars, selects, cards, etc.) published via JitPack so multiple Android apps can share components instead of copy-pasting them. Single Gradle module: `:uikit`. Root project has no source of its own.

## Commands

```bash
./gradlew :uikit:assembleRelease        # compile the AAR
./gradlew :uikit:testDebugUnitTest      # run unit tests + Paparazzi screenshot tests
./gradlew :uikit:publishToMavenLocal    # sanity-check the maven-publish setup locally
```

To run a single test class/method, use the standard Gradle test filter, e.g.:
```bash
./gradlew :uikit:testDebugUnitTest --tests "org.mindtec.uikit.utils.RestaurantListUtilsTest"
./gradlew :uikit:testDebugUnitTest --tests "org.mindtec.uikit.PreviewGalleryTest.buttonMF"
```

CI (`.github/workflows`) runs on `macos-latest` and does exactly: `assembleRelease`, then `testDebugUnitTest`, uploading the Paparazzi report on failure. Match that locally before pushing.

## Architecture

### Generic-by-design components

The core constraint of this library is that every component must be usable from *any* Android app, so:

- No `R.string`/`R.drawable` hardcoded to one app's copy. Icons are passed in as `Painter`, never as `@DrawableRes Int` — the caller resolves `painterResource(...)` and hands it in already built.
- No Room entities, Retrofit responses, ViewModels, or Hilt/NavHostController injected directly into a component. State comes in via parameters, goes out via lambdas (`onClick`, `onValueChange`, ...).
- Any data a component needs is modeled generically in `ui/components/model/` (`UiKitProduct`, `UiKitOption`, `UiKitComment`, etc.) — consuming apps map their real entities to these before calling the component.
- One component, one purpose — if a file mixes generic UI with app-specific business logic, split it before adding it here.

Apply these same rules when porting a new component in from an app: strip its entity/ViewModel/Hilt coupling and re-express its inputs as parameters/callbacks and generic models before it lands here.

### Package layout (`uikit/src/main/kotlin/org/mindtec/uikit/`)

- `ui/components/<category>/` — components grouped by kind: `button`, `card`, `dialog`, `alerts`, `items`, `select`, `text_field`, `toolbar`, `country_code_picker`. A few components with no natural category (`CustomTable`, `WidgetCounter`, `NavigationBottomBar`, ...) sit directly under `ui/components/`.
- `ui/components/model/` — the generic `UiKit*` data classes every component's public API is expressed in terms of.
- `ui/theme/` — `UiKitTheme`/`UiKitColors` (see below) plus the fixed base palette in `Color.kt`.
- `utils/` — framework-independent helpers (e.g. `RestaurantListUtils`, `BigDecimalExtensions`) that are unit-tested directly, without Compose/Paparazzi.

### Theming

Components read brand colors from `UiKitColors` via a `CompositionLocalProvider` (`LocalUiKitColors`), exposed through `UiKitTheme` (`ui/theme/UiKitTheme.kt`, `ui/theme/UiKitColors.kt`). Only five roles are themeable per app (`primary`, `primaryContainer`, `primaryContainerAlt`, `secondary`, `primaryPressed`); the rest of the palette (grays, status colors) is intentionally fixed and lives in `Color.kt`. `UiKitColors.fromPrimary(color)` derives all five tones from one brand color by tinting/shading; `UiKitColors.Default` (purple) is used when a consuming app doesn't wrap content in `UiKitTheme` at all, so unthemed apps must keep rendering exactly as before.

### Testing model

Two distinct kinds of test live under `uikit/src/test/kotlin/`:

- **Visual snapshots** — a single `PreviewGalleryTest.kt` renders one representative sample of most components to PNG via Paparazzi (no emulator needed). It shares one `snapshot(name, content)` helper that wraps content in a white `Box` and provides a no-op `ActivityResultRegistryOwner` (Paparazzi doesn't host a real Activity, and some components — e.g. `TextFieldWithCenteredImage` — call `rememberLauncherForActivityResult`). Snapshot names follow `"<category>_<ComponentName>[_variant]"` and land in `uikit/src/test/snapshots/images/` as `org.mindtec.uikit_PreviewGalleryTest_<test>_<name-lowercased>.png` — these images are what the README's component catalog embeds directly, so regenerate/update them (and the relevant README section) when a component's default appearance changes.
- **Pure logic unit tests** — plain JUnit tests (e.g. `RestaurantListUtilsTest.kt`) for framework-independent code in `utils/`, no Paparazzi/Compose involved.

When adding a new public component, add a corresponding `PreviewGalleryTest` case using realistic sample data — it's both the visual regression check and the source image for the README catalog entry.

### Publishing

The AAR is published to JitPack, consumed as `com.github.CesarAugusto213:mfui-components:v<version>`. The version string is set manually in `uikit/build.gradle.kts` (`afterEvaluate { publishing { publications { ... version = "..." } } }`) — bump it there (and tag the release) when cutting a new version; it is not derived from `settings.gradle.kts` or a properties file.
