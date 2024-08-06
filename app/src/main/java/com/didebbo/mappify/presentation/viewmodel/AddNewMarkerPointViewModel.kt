package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.domain.repository.MarkerPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddNewMarkerPointViewModel @Inject constructor(
    private val markerPostRepository: MarkerPostRepository
): ViewModel() {

    private var coordinates: MarkerPostDocument.GeoPoint =
        MarkerPostDocument.GeoPoint(0.0, 0.0)

    fun getCoordinates(): MarkerPostDocument.GeoPoint {
        return coordinates
    }
    fun updateCoordinates(geoPoint: MarkerPostDocument.GeoPoint) {
        coordinates = geoPoint
    }

    suspend fun addNewMarkerPost(markerPostDocument: MarkerPostDocument): Result<MarkerPostDocument> {
        return withContext(Dispatchers.IO) {
            markerPostRepository.addMarkerPostDocument(markerPostDocument)
        }
    }
}