# CLAUDE.md

Guidance for Claude Code in this repository. Detailed rules live in `.claude/rules/` (architecture, code style, Compose performance).

## Project

MaTip is a tip calculator: split tips between friends, save them in lists, share a list, back everything up to the cloud. It is a Kotlin Multiplatform app, Android first, iOS next. Compose Multiplatform UI, Material 3 Expressive, MVVM.

## Modules

- `:shared`: all app code and resources.
  - `commonMain`: screens, ViewModels, Room database, repositories, Koin modules, navigation, theme, resources (`composeResources`).
  - `androidMain`: only real platform code (platform actions, Credential Manager sign-in, `CurrentActivity`, database builder, dynamic color, currency formatting, full-screen dialog properties, the platform Koin module).
  - Tests: `commonTest` (kotlin.test, fakes in `fakes/`), `androidHostTest` (JVM only), `androidDeviceTest` (emulator or device).
- `:androidApp`: thin Android shell (`MainActivity`, `MatipApplication` calls `initKoin`, manifest, icons, signing, `google-services.json`). Holds the UI tests (`TipUITests`) because they need the real app and Firebase.

Package root: `com.exercise.matipv2`. Platform code goes behind an interface injected with Koin (`PlatformActions`, `GoogleIdTokenProvider`), or `expect`/`actual` for small functions (`formatCurrency`, theme helpers).

## Stack

Kotlin 2.4, Compose Multiplatform 1.12 and Material 3 1.12 alpha (on Android these resolve to AndroidX Compose 1.12 beta and Material 3 1.5 alpha), Navigation 3 (JetBrains), Koin, Room KMP with the bundled SQLite driver, GitLive Firebase (Auth and Firestore), kotlinx-serialization, kotlinx-datetime. Versions are in `gradle/libs.versions.toml`; add new dependencies there first.

## Commands

- Build: `./gradlew :androidApp:assembleDebug`
- Release build: `./gradlew :androidApp:assembleRelease` (needs `RELEASE_*` and `FIREBASE_WEB_CLIENT_ID` in `local.properties`, and `androidApp/google-services.json`, both git-ignored)
- Unit tests: `./gradlew :shared:testAndroidHostTest` (runs `commonTest` too)
- Device tests: `./gradlew :shared:connectedAndroidDeviceTest :androidApp:connectedDebugAndroidTest`
- Lint: `./gradlew :androidApp:lintDebug`

## Gotchas

- Never use `fallbackToDestructiveMigration`. Every Room schema change needs a `Migration` in `data/local/MatipMigrations.kt`, a test in `MatipDatabaseMigrationTest`, and the exported schema JSON in `shared/schemas/`. The database name `matip_database` and schema version history must not change otherwise.
- Firestore backup documents use the property names of `data/remote/BackupDtos.kt`. Never rename them, old backups would stop restoring. `BackupMappingTest` pins the shape.
- With one Android target the compiler does not check that `commonMain` is platform-neutral. Before committing, grep `shared/src/commonMain` for `java.`, `android.`, `androidx.core`, `androidx.activity`, `org.koin.android` and `com.google.firebase` imports. A real check arrives with the iOS target.
- Strings, plurals, fonts, icons and logos are in `shared/src/commonMain/composeResources` (English and `values-pt-rBR`). Accessors are `Res.*` from `com.exercise.matipv2.generated.resources`. Vector drawables need a literal fill color, not `@android:color/...`.
- Use the theme's motion scheme (`MaterialTheme.motionScheme`) for animations, not `tween` and easing.
- Source files use CRLF line endings. Keep them when editing with scripts.
- `FullScreenDialogProperties.android.kt` keeps an API 35+ workaround so the full-screen dialog does not overshoot the screen. Check on a device before changing it.
- Device tests uninstall the app, and the app locale persists between runs. If a phone and an emulator are both attached, pin the emulator: `ANDROID_SERIAL=emulator-5554 ./gradlew ...`.
