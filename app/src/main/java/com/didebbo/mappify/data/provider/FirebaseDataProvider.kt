package com.didebbo.mappify.data.provider

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class FirebaseDataProvider {
    private val auth: FirebaseAuth = Firebase.auth
    private val currentUser = auth.currentUser

    fun isSignedIn(): Boolean {
        return currentUser != null
    }
    fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
    }

}