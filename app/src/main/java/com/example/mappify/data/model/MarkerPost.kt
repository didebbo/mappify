package com.example.mappify.data.model

import com.google.android.gms.maps.model.LatLng

data class MarkerPost(
    private val id: String,
    private val title: String,
    private val description: String,
    private val position: LatLng,
    private val owner: User
)