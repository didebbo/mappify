package com.didebbo.mappify.data.model

import org.osmdroid.util.GeoPoint
import java.util.UUID

data class MarkerPostDocument(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Undefined Title",
    val description: String = "",
    val position: GeoPoint = GeoPoint(),
    val ownerId: String = UUID.randomUUID().toString()
) {
    data class GeoPoint(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
    )
}

