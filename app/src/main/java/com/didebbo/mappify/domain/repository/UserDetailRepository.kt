package com.didebbo.mappify.domain.repository

import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.data.provider.FirebaseDataProvider
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserDetailRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {
    suspend fun getOwnerUserDocument(): Result<UserDocument> {
        return firebaseDataProvider.getOwnerUserDocument()
    }
    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return firebaseDataProvider.getUserDocument(id)
    }

    suspend fun updateOwnerUserDocument(userDocument: UserDocument): Result<UserDocument> {
        return  firebaseDataProvider.updateOwnerUserDocument(userDocument)
    }

    suspend fun getUserMarkerPosts(id: String): Result<List<MarkerPostDocument>> {
        return firebaseDataProvider.getUserMarkerPosts(id)
    }
}