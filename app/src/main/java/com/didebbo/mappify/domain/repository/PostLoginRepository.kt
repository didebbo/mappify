package com.didebbo.mappify.domain.repository

import androidx.lifecycle.LiveData
import com.didebbo.mappify.data.model.MarkerDocument
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.data.provider.FirebaseDataProvider
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class PostLoginRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {
    fun getUser(): FirebaseUser? {
        return firebaseDataProvider.getUser().value
    }
    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return firebaseDataProvider.getUserDocument(id)
    }

    suspend fun getMarkerDocument(id: String): Result<MarkerDocument> {
        return firebaseDataProvider.getMarkerDocument(id)
    }

    suspend fun addMarkerPoint(markerDocument: MarkerDocument): Result<Unit> {
        return firebaseDataProvider.addMarkerPoint(markerDocument)
    }
}