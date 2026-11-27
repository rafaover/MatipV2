import UIKit
import FirebaseCore
import Shared

/// Sets up Firebase and starts Koin before the first screen.
class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        configureFirebase()
        KoinIosKt.startKoinForIos()
        return true
    }

    /// GoogleService-Info.plist is git-ignored, it comes from your Firebase project (the iOS app with this bundle id).
    /// Without it the app still starts with placeholder options, so the screens can be built and run,
    /// but sign in and cloud backup fail until the real file is added.
    private func configureFirebase() {
        if Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist") != nil {
            FirebaseApp.configure()
            return
        }
        let placeholder = FirebaseOptions(googleAppID: "1:000000000000:ios:0000000000000000", gcmSenderID: "000000000000")
        placeholder.apiKey = "development-placeholder"
        placeholder.projectID = "development-placeholder"
        FirebaseApp.configure(options: placeholder)
    }
}
