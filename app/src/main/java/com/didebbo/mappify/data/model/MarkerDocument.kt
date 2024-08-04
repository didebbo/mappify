package com.didebbo.mappify.data.model

import org.osmdroid.util.GeoPoint
import java.util.UUID

data class MarkerDocument(
    val title: String,
    val description: String,
    val position: GeoPoint,
    val owner: UserDocument
) {
    val id: String = UUID.randomUUID().toString()
}