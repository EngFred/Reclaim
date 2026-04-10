package com.engineerfred.reclaim.core.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

class FirebaseProvider {
    val auth: FirebaseAuth
        get() = Firebase.auth

    val firestore: FirebaseFirestore
        get() = Firebase.firestore
}