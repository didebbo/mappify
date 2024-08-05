package com.didebbo.mappify.domain.repository

import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.data.provider.FirebaseDataProvider
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class PostLoginRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {
    fun getUser(): FirebaseUser? {
        return firebaseDataProvider.getUserAuth().value
    }

    suspend fun getOwnerUserDocument(): Result<UserDocument> {
        return firebaseDataProvider.getOwnerUserDocument()
    }

    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return firebaseDataProvider.getUserDocument(id)
    }

    suspend fun getMarkerDocument(id: String): Result<MarkerPostDocument> {
        return firebaseDataProvider.getMarkerPostDocument(id)
    }

    suspend fun addMarkerPostDocument(markerPostDocument: MarkerPostDocument): Result<MarkerPostDocument> {
        return firebaseDataProvider.addMarkerPostDocument(markerPostDocument)
    }

    suspend fun updateUserDocument(userDocument: UserDocument): Result<Unit> {
        return firebaseDataProvider.updateUserDocument(userDocument)
    }
}