package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.MapViewLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.didebbo.mappify.presentation.view.component.markerpost.infowindow.MarkerPostInfoWindow
import com.didebbo.mappify.presentation.view.component.markerpost.infowindow.MarkerPostInfoWindowFactory
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

@AndroidEntryPoint
class MapViewPage: BaseFragmentDestination<PostLoginViewModel>(PostLoginViewModel::class.java) {

    @Inject
    lateinit var markerPostInfoWindowFactory: MarkerPostInfoWindowFactory

    private lateinit var binding: MapViewLayoutBinding

    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    private lateinit var addLocationIndicator: ImageView
    private lateinit var addLocationButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapViewLayoutBinding.inflate(inflater,container,false)
        mapView = binding.mapView
        mapController = mapView.controller
        addLocationIndicator = binding.addLocationIndicator
        addLocationButton = binding.addLocationButton
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuredMapView()
        configureOverlay()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        parentActivity?.loaderCoroutineScope {
            viewModel.fetchMarkerPostDocuments().onFailure {
                parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        viewModel.setEditingMode(false)
    }

    private fun configuredMapView() {
        parentActivity?.let { parentActivity ->
            Configuration.getInstance().load(parentActivity.applicationContext, parentActivity.getPreferences(
                MODE_PRIVATE))
            val startPoint = GeoPoint(41.902865550106036, 12.481451481672554)
            mapController.setZoom(15.0)
            mapController.setCenter(startPoint)
        }
    }

    private fun configureOverlay() {
        viewModel.editingMode.observe(viewLifecycleOwner) { editingMode ->
            val visibility = if(editingMode) View.VISIBLE else View.GONE
            val buttonText = if(editingMode) "Cancel" else "Add Location"
            addLocationIndicator.visibility = visibility
            addLocationButton.text = buttonText
        }

        viewModel.markerPostDocuments.observe(viewLifecycleOwner) { markerPostDocuments ->
            markerPostDocuments.forEach{
                val marker = Marker(mapView).apply {
                    position = GeoPoint(it.position.latitude,it.position.longitude)
                    infoWindow = generateMarkerPostInfoWindow(it)
                }
                mapView.overlays.add(marker)
            }
        }

        addLocationButton.setOnClickListener {
            viewModel.setEditingMode()
        }

        addLocationIndicator.setOnClickListener{
            val mapCenter = mapView.mapCenter
            val  centerPoint =  MarkerPostDocument.GeoPoint(mapCenter.latitude,mapCenter.longitude)
            val bundle = Bundle().apply {
                putDouble("latitude", centerPoint.latitude)
                putDouble("longitude",centerPoint.longitude)
            }
            navController?.navigate(R.id.new_marker_point_navigation_activity,bundle)
        }
    }

    private fun generateMarkerPostInfoWindow(markerPostDocument: MarkerPostDocument): MarkerPostInfoWindow {
        return markerPostInfoWindowFactory.create(
            this,
            mapView,
            MarkerPostInfoWindow.ViewData(
                ownerId = markerPostDocument.ownerId,
                title = markerPostDocument.title,
                description = markerPostDocument.description,
                position = markerPostDocument.position
            )
        )
    }
}