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
        val userDocumentResult = getOwnerUserDocument()
        userDocumentResult.exceptionOrNull()?.let {
            return Result.failure(it)
        }
        userDocumentResult.getOrNull()?.let { userDocument ->
            val newMarkerPostDocument = markerPostDocument.copy(ownerId = userDocument.id)
            val markerPostResult = firebaseDataProvider.addMarkerPostDocument(newMarkerPostDocument)
            markerPostResult.exceptionOrNull()?.let {
                return Result.failure(it)
            }
            markerPostResult.getOrNull()?.let { markerPostDocument ->
                val markerPostsIds = userDocument.markerPostsIds.toMutableList()
                markerPostsIds.add(markerPostDocument.id)
                val updatedUserDocument = userDocument.copy(markerPostsIds = markerPostsIds)
                val updatedUserDocumentResult = updateUserDocument(updatedUserDocument)
                updatedUserDocumentResult.exceptionOrNull()?.let {
                    return Result.failure(it)
                }
                updatedUserDocumentResult.getOrNull()?.let {
                    return Result.success(markerPostDocument)
                }
            }
            return Result.failure(Exception("addMarkerPostDocument() markerPostDocument Not Found"))
        }
        return Result.failure(Exception("addMarkerPostDocument() userDocument Not Found"))
    }

    suspend fun updateUserDocument(userDocument: UserDocument): Result<Unit> {
        return firebaseDataProvider.updateUserDocument(userDocument)
    }
}