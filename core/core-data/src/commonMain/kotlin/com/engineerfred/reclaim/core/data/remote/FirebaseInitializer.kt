package com.engineerfred.reclaim.core.data.remote

/**
 * Handles platform-specific Firebase initialization.
 *
 * Android: calls Firebase.initialize(context) from the Application class.
 * iOS:     Firebase is configured via FirebaseApp.configure() in the Swift
 *          AppDelegate — nothing is needed from the KMP side here.
 *
 * Call [initialize] once from the app shell (Application.onCreate on Android,
 * AppDelegate.applicationDidFinishLaunching on iOS) before any Firebase
 * service is accessed.
 *
 * IMPORTANT: This call has no effect until you add:
 *   Android → google-services.json into composeApp/
 *   iOS     → GoogleService-Info.plist into iosApp/
 */
expect object FirebaseInitializer {
    fun initialize(context: Any?)
}