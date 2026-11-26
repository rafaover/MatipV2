package com.exercise.matipv2.platform

/**
 * Things the app asks the operating system to do. Screens depend on this
 * interface only, so no Android types leak into shared UI code.
 */
interface PlatformActions {

    /** Opens the system share sheet with [title] followed by [content]. */
    fun shareText(title: String, content: String)

    /** Opens the mail app on a new message. Returns false if there is no mail app. */
    fun sendEmail(address: String, subject: String): Boolean

    /** Opens [url] in the browser. Returns false if nothing can open it. */
    fun openUrl(url: String): Boolean

    /** Opens this app's page in the store so the user can review it. */
    fun openStoreListing()
}
