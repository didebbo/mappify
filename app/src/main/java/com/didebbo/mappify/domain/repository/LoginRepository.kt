package com.didebbo.mappify.domain.repository

import androidx.lifecycle.LiveData
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.data.provider.FirebaseDataProvider
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class LoginRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {

    fun getUser(): LiveData<FirebaseUser?> {
        return firebaseDataProvider.getUser()
    }

    suspend fun createUserWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser?> {
        return firebaseDataProvider.createUserWithEmailAndPassword(userAuth)
    }

    suspend fun signInWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser?> {
        return firebaseDataProvider.signInWithEmailAndPassword(userAuth)
    }

    fun signOut() {
        firebaseDataProvider.signOut()
    }
}