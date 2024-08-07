package com.didebbo.mappify.presentation.viewmodel

import android.util.Log
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

    private var coordinates: MarkerPostDocument.GeoPoint? = null
    var editCoordinates: Boolean = false
    val onDismissAction: (()->Unit)? = null


    fun getCoordinates(): MarkerPostDocument.GeoPoint {
        return coordinates ?: MarkerPostDocument.GeoPoint(0.0,0.0)
    }
    fun setupCoordinates(latitude: Double?, longitude: Double?) {
        if(latitude != null && longitude != null) {
            if(latitude != 0.0 && longitude != 0.0) {
                coordinates = MarkerPostDocument.GeoPoint(latitude,longitude)
            }
        }
        editCoordinates = coordinates == null
    }

    suspend fun addNewMarkerPost(markerPostDocument: MarkerPostDocument): Result<MarkerPostDocument> {
        return withContext(Dispatchers.IO) {
            markerPostRepository.addMarkerPostDocument(markerPostDocument)
        }
    }
}