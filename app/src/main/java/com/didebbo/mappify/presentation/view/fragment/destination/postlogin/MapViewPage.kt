package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import com.github.didebbo.mappify.databinding.MapViewLayoutBinding
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class MapViewPage: BaseFragmentDestination<PostLoginViewModel>(PostLoginViewModel::class.java) {

    private lateinit var mapViewLayoutBinding: MapViewLayoutBinding
    private val postLoginActivity: PostLoginActivity? by lazy { parentActivity as? PostLoginActivity }
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapViewLayoutBinding = MapViewLayoutBinding.inflate(inflater,container,false)
        return  mapViewLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuredMapView()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    private fun configuredMapView() {
        parentActivity?.let { parentActivity ->
            mapView = mapViewLayoutBinding.mapView
            Configuration.getInstance().load(parentActivity.applicationContext, parentActivity.getPreferences(
                MODE_PRIVATE))
            val startPoint = GeoPoint(41.902865550106036, 12.481451481672554)
            val mapController = mapView.controller
            mapController.setZoom(15.0)
            mapController.setCenter(startPoint)
        }
    }
}