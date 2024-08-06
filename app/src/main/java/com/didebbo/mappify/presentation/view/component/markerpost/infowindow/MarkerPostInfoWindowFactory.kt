package com.didebbo.mappify.presentation.view.component.markerpost.infowindow

import androidx.fragment.app.Fragment
import org.osmdroid.views.MapView

class MarkerPostInfoWindowFactory {
    fun create(
        parent: Fragment,
        mapView: MapView,
        data: MarkerPostInfoWindow.ViewData
    ): MarkerPostInfoWindow {
        return MarkerPostInfoWindow(parent, mapView, data)
    }
}