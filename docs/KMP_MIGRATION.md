# Compose Multiplatform migration

Status, decisions and plan. Last updated 2026-11-26. Your open tasks are in [TODO.md](TODO.md).

## Where we are

| Phase | What | State |
|---|---|---|
| 0 | Prepare on Android (remove Analytics, rename `List` to `TipList`, platform interfaces, string resources, `formatCurrency`, schema baseline test) | Done, PR #303 |
| 1 | Split into `:shared` and `:androidApp`, then move code into `commonMain` in 10 stages | Done, PRs #304 to #311 |
| 2a | iOS targets, `iosMain` actuals, email-only sign in on iOS | Done, in the iOS PR |
| 2b | `iosApp/` Xcode project, runs on the iPhone simulator | Done, in the iOS PR |
| 3 | Ship iOS (Firebase plist, signing, icon, privacy manifest, TestFlight) | Not started, see TODO.md |

The Android app is unchanged for users except: expressive motion and shapes, the bottom bar is the expressive `ShortNavigationBar`, the drawer always shows the generic account icon (Coil and the Google photo were removed), and Settings keeps the calculator tab highlighted.

## Decisions (all made with you)

- Layout: `:shared` (Kotlin Multiplatform library) + `:androidApp` (thin shell) + `iosApp/` (Xcode project, not a Gradle module), same as Coopy.
- Android parity first, then iOS.
- Firebase through GitLive `firebase-kotlin-sdk` **2.8.0** (3.0 is only alpha/beta). Analytics was removed.
- Never change: `applicationId` `com.exercise.matipv2`, Room database name `matip_database`, schema history. Users keep their data across upgrades.
- Never use `fallbackToDestructiveMigration`. Every schema change needs a `Migration` plus a test (`MatipMigrations.kt`, `MatipDatabaseMigrationTest`).
- Navigation 3 (JetBrains 1.2.0) with `@Serializable` routes. The adaptive navigation suite was dropped for a hand-written bottom bar and rail.
- Coil, Ktor and the drawer avatar were removed (the only image loaded from the network was the Google photo).
- iOS offers **email sign in only**: Apple requires Sign in with Apple next to any third-party login (App Store rule 4.8).
- No CI workflow (simple app, you will test something else soon).
- Material 3 Expressive and the motion scheme everywhere (rules in `.claude/rules/`).

## Pull requests so far

| PR | Content |
|---|---|
| #303 | Phase 0 |
| #304 | Gradle wrapper, AGP 9.5 / Kotlin 2.4 / KSP, split into `:shared` and `:androidApp` |
| #305 | Pure code, strings, font, logos and icons to `commonMain` and `composeResources` |
| #306 | JetBrains Compose and Material 3, leaf components and theme to `commonMain`, first Expressive pass |
| #307 | Multiplatform Koin, repository interfaces to `commonMain` |
| #308 | Room KMP with the bundled SQLite driver, migrations 2 to 7, no destructive fallback |
| #309 | GitLive for Auth and Firestore |
| #310 | Navigation 3, all screens and ViewModels to `commonMain`, Coil removed |
| #311 | Tests in `commonTest` with fakes, docs, lifecycle-aware state, flows kept in the ViewModel |
| next | iOS targets, `iosMain`, `iosApp/` Xcode project, these docs |

`master` requires 1 approving review. A PR you open yourself cannot be approved by you, so #304 was merged with `--admin` once you agreed.

## How the code is laid out today

- `shared/src/commonMain`: screens, ViewModels, Room database, repositories, Koin modules (`initKoin`), navigation, theme, resources.
- `shared/src/androidMain`: platform actions, Credential Manager sign in, `CurrentActivity`, Room builder, dynamic color, `formatCurrency`, full-screen dialog properties, `androidPlatformModule`.
- `shared/src/iosMain`: the same list for iOS, plus `MainViewController` and `startKoinForIos`.
- Tests: `commonTest` (kotlin.test, fakes in `fakes/`), `androidHostTest`, `androidDeviceTest`. `TipUITests` is in `androidApp` because it needs the real app and Firebase.
- Details and commands: `CLAUDE.md`.

## Deviations from the original plan

- Staged instead of one big move: modules first with everything in `androidMain`, then one slice at a time into `commonMain`.
- `appModule` and `initKoin` moved to common only after Room (stage 7) and GitLive (stage 8), because they reference those types.
- Backups use `@Serializable` DTOs (the Android SDK version used plain maps), same field names.
- The common `metadata` compile is skipped with one target, so it proved nothing. The iOS compile is the real check (it caught `Dispatchers.IO` in common code).
- The iOS unit tests cannot run through Gradle (see TODO.md).

## Dependency risks to watch

- On Android the app runs **AndroidX Compose 1.12 beta** and **Material 3 1.5 alpha** (CMP 1.12.2 and Material 3 1.12.0-alpha03 resolve to those). Same as Coopy. Move to stable when JetBrains releases it.
- Release APK is about 8.4 MB (bundled SQLite native libraries for every ABI). Play serves per-ABI splits.
- GitLive 2.8.0 needs Firebase iOS SDK 12.19.3 or 13.1.1+ (we pin 13.2.0 through SPM). Check its README when updating either.
- Kotlin 2.4.21, AGP 9.5.1, Gradle 9.9.1, KSP 2.3.14, Room 2.8.4.

## How to verify a change

- Android: `./gradlew :androidApp:assembleDebug :shared:testAndroidHostTest :androidApp:lintDebug`
- Common code is platform-neutral: `./gradlew :shared:compileKotlinIosSimulatorArm64`
- Device tests (pin the emulator if a phone is attached): `ANDROID_SERIAL=emulator-5554 ./gradlew :shared:connectedAndroidDeviceTest :androidApp:connectedDebugAndroidTest`
- iOS app: `xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -sdk iphonesimulator -destination 'generic/platform=iOS Simulator' CODE_SIGNING_ALLOWED=NO build`
- Release: `./gradlew :androidApp:assembleRelease` (needs the `RELEASE_*` and `FIREBASE_WEB_CLIENT_ID` values in `local.properties`).
