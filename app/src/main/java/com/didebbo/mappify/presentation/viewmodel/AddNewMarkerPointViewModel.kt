package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.MarkerPostDocument

class AddNewMarkerPointViewModel: ViewModel() {

    private var coordinates: MarkerPostDocument.GeoPoint =
        MarkerPostDocument.GeoPoint(0.0, 0.0)

    fun getCoordinates(): MarkerPostDocument.GeoPoint {
        return coordinates
    }
    fun updateCoordinates(geoPoint: MarkerPostDocument.GeoPoint) {
        coordinates = geoPoint
    }
}