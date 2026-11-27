# TODO

What is left, so work can continue later. Last updated 2026-11-26. Status and decisions are in [KMP_MIGRATION.md](KMP_MIGRATION.md).

## Yours (needs your accounts or devices)

### Before the iOS app can sign in or back up
- [ ] In the Firebase console, add an **iOS app** to the MaTip project with bundle id `com.exercise.matipv2`, download `GoogleService-Info.plist` and put it in `iosApp/iosApp/` (it is git-ignored). Without it the iOS app starts with placeholder Firebase options and sign in and backup fail.
- [ ] Check the Firestore security rules still allow only `users/{uid}/...` for the signed-in user (the iOS app writes the same documents as Android).

### Before an App Store upload
- [ ] Set `TEAM_ID` in `iosApp/Configuration/Config.xcconfig` (needed to run on a real device or archive).
- [ ] Replace the iOS app icon. The current one is the Android icon scaled up to 1024 px and has transparency, which Apple rejects.
- [ ] Add a privacy manifest (`PrivacyInfo.xcprivacy`, Coopy has one to copy and adapt). Check which required-reason APIs the app and its dependencies use before you declare them.
- [ ] Create the App Store listing, then set `APP_STORE_ID` in `shared/src/iosMain/.../platform/IosPlatformActions.kt` so "Review App" works.
- [ ] Decide on Google sign in for iOS. Today iOS is email only. Offering Google needs the GoogleSignIn iOS SDK plus Sign in with Apple (rule 4.8).

### Manual checks I could not do
- [ ] **iOS simulator or device:** tap through the whole app (type a bill, split, tabs, create a list, add a tip, open a list, share, Settings, email sign in). I only confirmed it builds, launches, renders and creates its database.
- [ ] **Android phone, after all PRs are merged:** one full pass. Google sign in, backup, restore (also an old backup from before the migration), lists switching on sign in and sign out, share, back button.
- [ ] **Upgrade from the real Play version** to this build with existing lists and tips (I only tested upgrading over the previous `master` build).
- [ ] Predictive back gesture on a recent Android, and a tablet or foldable for the side rail.
- [ ] Look at the minified release build on your phone (R8 with the beta Compose runtime).

### Housekeeping
- [ ] Review and merge the iOS PR (`master` needs 1 approving review).

## Next up (can be done with Claude)

- [ ] **Hardcoded English errors:** "Backup failed", "Restore failed", "Failed to delete account", "Failed to delete cloud data" in `AuthViewModel`, and "No backup found" in `FirestoreBackupRepository`. Move to string resources (the ViewModel should expose an error type, the UI picks the text), with pt-BR.
- [ ] **Expressive pass, remaining:** `MaterialShapes` (cookie or organic polygon) on the icon FAB, `AnimatedVisibility` for empty states and the clear-search button, more TalkBack labels (the "+" and "-" text, tip rows), spring motion in the dialogs.
- [ ] **Magic numbers** in components (`20.dp`, `36.dp`, `56.dp`, `70.dp`, `80.dp`, `25.dp`...): move to `MatipDimens`.
- [ ] **iOS UI smoke test** as an XCUITest target, so it can run from `xcodebuild test` without clicking.
- [ ] **iOS unit tests through Gradle:** `iosSimulatorArm64Test` cannot link because GitLive needs the Firebase iOS frameworks. Fix with the Kotlin CocoaPods plugin (`pod("FirebaseCore")` and friends) or skip. Common tests already run on Android.
- [ ] **Dark mode on iOS:** the simulator screenshot was light mode. Check the dark theme and the status bar.
- [ ] Add a test for `insertTipValueToList` (it runs on the main test dispatcher now that `Dispatchers.IO` is gone).
- [ ] When stable versions exist: Compose Multiplatform and Material 3 (drop the beta and alpha), GitLive 3.x.

## Known issues and gaps

- Room schema version 1 was never exported, so a database still at version 1 cannot be migrated and the app would fail to open it (the data stays on disk). Practically nobody is on it.
- `FullScreenDialogProperties.android.kt` keeps an API 35+ workaround so the full-screen dialog does not overshoot the screen. Check on a device before touching it.
- On this emulator (API 37) the list dialog is inset, the same as before the migration.
- The Lists tab clears the snackbar with a `LaunchedEffect`, a small hack kept from the old code.
- `README.md` roadmap items (crowdfunding, wallets) are ideas, not planned work.
