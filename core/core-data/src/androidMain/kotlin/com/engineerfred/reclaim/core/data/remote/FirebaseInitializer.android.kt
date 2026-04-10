package com.engineerfred.reclaim.core.data.remote

import android.content.Context
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

/**
 * On Android, Firebase must be initialised with the Application context
 * before any Firebase service is used. Call this from Application.onCreate().
 *
 * NOTE: This has no effect until google-services.json is present in
 * the composeApp/ module root. Add it before running a Firebase-dependent
 * feature (auth, firestore, notifications).
 */
actual object FirebaseInitializer {
    actual fun initialize(context: Any?) {
        requireNotNull(context) { "Android FirebaseInitializer requires a non-null Context." }
        Firebase.initialize(context as Context)
    }
}