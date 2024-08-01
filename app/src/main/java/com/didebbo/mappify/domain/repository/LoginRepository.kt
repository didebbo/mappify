package com.didebbo.mappify.domain.repository

import com.didebbo.mappify.data.model.UserAuth
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

    fun createUserWithEmailAndPassword(userAuth: UserAuth) {
        firebaseDataProvider.createUserWithEmailAndPassword(userAuth)
    }

    fun signInWithEmailAndPassword(userAuth: UserAuth) {
        firebaseDataProvider.signInWithEmailAndPassword(userAuth)
    }
}