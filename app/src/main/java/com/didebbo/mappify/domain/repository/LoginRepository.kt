package com.didebbo.mappify.domain.repository

import com.didebbo.mappify.data.provider.FirebaseDataProvider
import javax.inject.Inject


class LoginRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {
    fun isSignedIn(): Boolean {
        return firebaseDataProvider.isSignedIn()
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        firebaseDataProvider.createUserWithEmailAndPassword(email, password)
    }
}