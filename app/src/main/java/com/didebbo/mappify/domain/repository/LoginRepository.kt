package com.didebbo.mappify.domain.repository

import com.didebbo.mappify.data.provider.FirebaseDataProvider
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class LoginRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {

    fun getUser(): FirebaseUser? {
        return firebaseDataProvider.getUser()
    }

    fun isSignedIn(): Boolean {
        return firebaseDataProvider.isSignedIn()
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        firebaseDataProvider.createUserWithEmailAndPassword(email, password)
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseDataProvider.signInWithEmailAndPassword(email, password)
    }
}