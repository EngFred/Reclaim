package com.engineerfred.reclaim.core.data.remote

/**
 * On iOS, Firebase is initialised by calling [FirebaseApp.configure()]
 * inside the Swift AppDelegate (or via the @main entry point).
 * Nothing is needed from the KMP side at this layer.
 *
 * In iosApp/iosApp/iOSApp.swift, add before the WindowGroup:
 *   init() { FirebaseApp.configure() }
 *
 * NOTE: GoogleService-Info.plist must be present in the iosApp/
 * Xcode project for configure() to succeed.
 */
actual object FirebaseInitializer {
    actual fun initialize(context: Any?) {
        // iOS Firebase initialisation is handled natively in AppDelegate.
        // See comment above for the required Swift code.
    }
}