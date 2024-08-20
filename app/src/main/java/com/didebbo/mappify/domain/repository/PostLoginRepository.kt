package com.didebbo.mappify.domain.repository

import com.didebbo.mappify.data.model.AvatarColor
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.data.provider.FirebaseDataProvider
import javax.inject.Inject


class PostLoginRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {
    suspend fun fetchMarkerPostDocuments(): Result<List<MarkerPostDocument>> {
        return firebaseDataProvider.fetchMarkerPostDocuments()
    }

    suspend fun fetchUserDocuments(): Result<List<UserDocument>> {
        return firebaseDataProvider.fetchUserDocuments()
    }

    suspend fun getOwnerUserDocument(): Result<UserDocument> {
        return firebaseDataProvider.getOwnerUserDocument()
    }

    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return firebaseDataProvider.getUserDocument(id)
    }

    suspend fun getAvatarColor(id: String): Result<AvatarColor> {
        return firebaseDataProvider.getAvatarColor(id)
    }
}