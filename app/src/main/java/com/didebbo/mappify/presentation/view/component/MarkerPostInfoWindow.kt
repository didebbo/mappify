package com.didebbo.mappify.presentation.view.component

import android.view.LayoutInflater
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.MarkerPostLayoutBinding
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MarkerPostInfoWindow(private val mapView: MapView, private val data: ViewData): InfoWindow(R.layout.marker_post_layout, mapView) {

    private val binding: MarkerPostLayoutBinding =
        MarkerPostLayoutBinding.inflate(LayoutInflater.from(mapView.context),null,false)

    init { mView = binding.root }
    data class ViewData(
        val userName: String,
        val avatarName: String,
        val title: String,
        val description: String,
        val position: MarkerPostDocument.GeoPoint
    )
    override fun onOpen(item: Any?) {
        binding.avatarIconText.text = data.avatarName
        binding.userNameText.text = data.userName
        binding.postTitleText.text = data.title
        binding.postDescriptionText.text = data.description
        binding.coordinateText.text = "${data.position.latitude} , ${data.position.longitude}"
    }

    override fun onClose() {

    }
}