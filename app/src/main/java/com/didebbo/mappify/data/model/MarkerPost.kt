package com.didebbo.mappify.data.model

import com.google.android.gms.maps.model.LatLng
import java.util.UUID

data class MarkerPost(
    val title: String,
    val description: String,
    val position: LatLng,
    val owner: UserDocument
) {
    val id: String = UUID.randomUUID().toString()
}