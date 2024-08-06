package com.didebbo.mappify.domain.repository

import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.provider.FirebaseDataProvider
import javax.inject.Inject


class PostLoginRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {
    suspend fun fetchMarkerPostDocuments(): Result<List<MarkerPostDocument>> {
        return firebaseDataProvider.fetchMarkerPostDocuments()
    }
}