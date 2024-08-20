package com.didebbo.mappify.domain.repository

import androidx.lifecycle.LiveData
import com.didebbo.mappify.data.model.AvatarColor
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.data.provider.FirebaseDataProvider
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class PreLoginRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {

    fun getUser(): LiveData<FirebaseUser?> {
        return firebaseDataProvider.getUserAuth()
    }

    suspend fun getOwnerUserDocument(): Result<UserDocument> {
        return firebaseDataProvider.getOwnerUserDocument()
    }

    suspend fun createUserWithEmailAndPassword(userAuth: UserAuth): Result<Unit> {
        return firebaseDataProvider.createUserWithEmailAndPassword(userAuth)
    }

    suspend fun signInWithEmailAndPassword(userAuth: UserAuth): Result<Unit> {
        return firebaseDataProvider.signInWithEmailAndPassword(userAuth)
    }

    fun signOut() {
        firebaseDataProvider.signOut()
    }

    suspend fun getAvatarColor(id: String): Result<AvatarColor> {
        return firebaseDataProvider.getAvatarColor(id)
    }
}