# CLAUDE.md

Guidance for Claude Code in this repository. Detailed rules live in `.claude/rules/` (architecture, code style, Compose performance).

## Project

MaTip is a tip calculator: split tips between friends, save them in lists, share a list, back everything up to the cloud. It is a Kotlin Multiplatform app: Android is shipped, iOS compiles and gets its app shell next. Compose Multiplatform UI, Material 3 Expressive, MVVM.

## Docs

- `docs/KMP_MIGRATION.md`: migration status, decisions, PR history and how to verify.
- `docs/TODO.md`: open tasks (yours and next up) and known issues. Keep both current when work lands.

## Modules

- `:shared`: all app code and resources.
  - `commonMain`: screens, ViewModels, Room database, repositories, Koin modules, navigation, theme, resources (`composeResources`).
  - `androidMain` and `iosMain`: only real platform code (platform actions, sign-in, database builder, theme helpers, currency formatting, full-screen dialog properties, the platform Koin module).
  - Tests: `commonTest` (kotlin.test, fakes in `fakes/`), `androidHostTest` (JVM only), `androidDeviceTest` (emulator or device).
- `:androidApp`: thin Android shell (`MainActivity`, `MatipApplication` calls `initKoin`, manifest, icons, signing, `google-services.json`). Holds the UI tests (`TipUITests`) because they need the real app and Firebase.

- `iosApp/`: the iOS app shell (Xcode project, not a Gradle module). SwiftUI hosts `MainViewController()` from `:shared`; `AppDelegate` configures Firebase and calls `startKoinForIos()`. Firebase comes from Swift Package Manager (`firebase-ios-sdk` 13.2, GitLive needs 13.1.1 or newer).

Package root: `com.exercise.matipv2`. Platform code goes behind an interface injected with Koin (`PlatformActions`, `GoogleIdTokenProvider`), or `expect`/`actual` for small functions (`formatCurrency`, theme helpers).

## Stack

Kotlin 2.4, Compose Multiplatform 1.12 and Material 3 1.12 alpha (on Android these resolve to AndroidX Compose 1.12 beta and Material 3 1.5 alpha), Navigation 3 (JetBrains), Koin, Room KMP with the bundled SQLite driver, GitLive Firebase (Auth and Firestore), kotlinx-serialization, kotlinx-datetime. Versions are in `gradle/libs.versions.toml`; add new dependencies there first.

## Commands

- Build: `./gradlew :androidApp:assembleDebug`
- Release build: `./gradlew :androidApp:assembleRelease` (needs `RELEASE_*` and `FIREBASE_WEB_CLIENT_ID` in `local.properties`, and `androidApp/google-services.json`, both git-ignored)
- Unit tests: `./gradlew :shared:testAndroidHostTest` (runs `commonTest` too)
- Device tests: `./gradlew :shared:connectedAndroidDeviceTest :androidApp:connectedDebugAndroidTest`
- Lint: `./gradlew :androidApp:lintDebug`
- iOS app on the simulator: `xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -sdk iphonesimulator -destination 'generic/platform=iOS Simulator' CODE_SIGNING_ALLOWED=NO build` (the build runs Gradle's `embedAndSignAppleFrameworkForXcode` itself)
- Check `commonMain` is platform-neutral: `./gradlew :shared:compileKotlinIosSimulatorArm64`
- iOS framework for Xcode: `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64` (and `...IosArm64` for devices)

## Gotchas

- Never use `fallbackToDestructiveMigration`. Every Room schema change needs a `Migration` in `data/local/MatipMigrations.kt`, a test in `MatipDatabaseMigrationTest`, and the exported schema JSON in `shared/schemas/`. The database name `matip_database` and schema version history must not change otherwise.
- Firestore backup documents use the property names of `data/remote/BackupDtos.kt`. Never rename them, old backups would stop restoring. `BackupMappingTest` pins the shape.
- The Android compile accepts JVM-only code in `commonMain` (for example `Dispatchers.IO`). The iOS compile does not, so run `:shared:compileKotlinIosSimulatorArm64` after touching common code.
- Room queries run on `Dispatchers.IO`, set on the platform database builder. ViewModels launch on `viewModelScope` without a dispatcher.
- iOS offers email sign in only (Apple requires Sign in with Apple next to any third-party login). `GoogleIdTokenProvider.isAvailable` hides the Google button.
- iOS needs `iosApp/iosApp/GoogleService-Info.plist` from your Firebase project (iOS app with bundle id `com.exercise.matipv2`), it is git-ignored. Without it `AppDelegate` configures placeholder Firebase options so the screens run, but sign in and cloud backup fail.
- Set `TEAM_ID` in `iosApp/Configuration/Config.xcconfig` to run on a device. Keep its version numbers in step with `androidApp/build.gradle.kts`.
- Before an App Store upload: replace the app icon (the current one is the Android icon scaled up and has transparency), add a privacy manifest, and set `APP_STORE_ID` in `IosPlatformActions.kt`.
- `:shared:iosSimulatorArm64Test` cannot link: GitLive needs the Firebase iOS frameworks, which only the Xcode app provides. Common tests run through `testAndroidHostTest`.
- Strings, plurals, fonts, icons and logos are in `shared/src/commonMain/composeResources` (English and `values-pt-rBR`). Accessors are `Res.*` from `com.exercise.matipv2.generated.resources`. Vector drawables need a literal fill color, not `@android:color/...`.
- Use the theme's motion scheme (`MaterialTheme.motionScheme`) for animations, not `tween` and easing.
- Source files use CRLF line endings. Keep them when editing with scripts.
- `FullScreenDialogProperties.android.kt` keeps an API 35+ workaround so the full-screen dialog does not overshoot the screen. Check on a device before changing it.
- Device tests uninstall the app, and the app locale persists between runs. If a phone and an emulator are both attached, pin the emulator: `ANDROID_SERIAL=emulator-5554 ./gradlew ...`.
