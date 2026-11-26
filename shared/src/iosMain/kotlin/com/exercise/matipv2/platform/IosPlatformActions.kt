package com.exercise.matipv2.platform

import platform.Foundation.NSCharacterSet
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.URLQueryAllowedCharacterSet
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

/** The App Store id of the app. Empty until the app is on the App Store, then "Review App" does nothing. */
private const val APP_STORE_ID = ""

class IosPlatformActions : PlatformActions {

    override fun shareText(title: String, content: String) {
        val shareSheet = UIActivityViewController(
            activityItems = listOf("$title\n$content"),
            applicationActivities = null
        )
        topViewController()?.presentViewController(shareSheet, animated = true, completion = null)
    }

    override fun sendEmail(address: String, subject: String): Boolean {
        val encodedSubject = (subject as NSString)
            .stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.URLQueryAllowedCharacterSet)
            ?: subject
        return openUrl("mailto:$address?subject=$encodedSubject")
    }

    override fun openUrl(url: String): Boolean {
        val nsUrl = NSURL.URLWithString(url) ?: return false
        val application = UIApplication.sharedApplication
        if (!application.canOpenURL(nsUrl)) return false

        application.openURL(nsUrl, options = emptyMap<Any?, Any>(), completionHandler = null)
        return true
    }

    override fun openStoreListing() {
        if (APP_STORE_ID.isEmpty()) return
        openUrl("itms-apps://itunes.apple.com/app/id$APP_STORE_ID?action=write-review")
    }

    /** The screen on top, so the share sheet appears over whatever is showing. */
    private fun topViewController(): UIViewController? {
        var controller = UIApplication.sharedApplication.keyWindow?.rootViewController
        while (controller?.presentedViewController != null) {
            controller = controller.presentedViewController
        }
        return controller
    }
}
