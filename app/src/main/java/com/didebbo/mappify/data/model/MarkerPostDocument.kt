package com.didebbo.mappify.data.model

import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import java.io.Serializable
import java.util.UUID

data class MarkerPostDocument(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val position: GeoPoint = GeoPoint(),
    val ownerId: String = UUID.randomUUID().toString()
) {
    data class GeoPoint(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
    ): Serializable {
        fun toIGeoPoint(): org.osmdroid.util.GeoPoint {
            return org.osmdroid.util.GeoPoint(latitude,longitude)
        }
    }

    private fun hasValidTitle(): Boolean {
        return title.isNotEmpty()
    }
    private fun hasValidDescription(): Boolean {
        return description.isNotEmpty()
    }

    fun newMarkerPostException(): Exception? {
        if(!hasValidTitle()) return Exception("Invalid Title")
        if(!hasValidDescription()) return Exception("Invalid Description")
        return null
    }
}

