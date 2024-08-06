package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import com.didebbo.mappify.databinding.MapViewLayoutBinding
import com.didebbo.mappify.presentation.view.component.MarkerPostInfoWindow
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint
class MapViewPage: BaseFragmentDestination<PostLoginViewModel>(PostLoginViewModel::class.java) {

    private lateinit var mapViewLayoutBinding: MapViewLayoutBinding
    private val postLoginActivity: PostLoginActivity? by lazy { parentActivity as? PostLoginActivity }

    private val mapView: MapView by lazy {
        mapViewLayoutBinding.mapView
    }

    private lateinit var markerPostInfo: MarkerPostInfoWindow

    private val mapController: IMapController by lazy {
        mapView.controller
    }

    private val addLocationIndicator: ImageView by lazy {
        mapViewLayoutBinding.addLocationIndicator
    }

    private val addLocationButton: Button by lazy {
        mapViewLayoutBinding.addLocationButton
    }

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
        configureOverlay()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        viewModel.setEditingMode(false)
        lifecycleScope.launch {
            viewModel.fetchMarkerPostDocuments().onFailure {
                parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    private fun configuredMapView() {
        parentActivity?.let { parentActivity ->
            Configuration.getInstance().load(parentActivity.applicationContext, parentActivity.getPreferences(
                MODE_PRIVATE))
            val startPoint = GeoPoint(41.902865550106036, 12.481451481672554)
            mapController.setZoom(15.0)
            mapController.setCenter(startPoint)

            mapView.setOnTouchListener { _, _ ->
                if(markerPostInfo.isOpen) markerPostInfo.close()
                false
            }
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
        markerPostInfo = MarkerPostInfoWindow(
            mapView,
            MarkerPostInfoWindow.ViewData(
                userName = "Undefined Name",
                avatarName = "UN",
                title = markerPostDocument.title,
                description = markerPostDocument.description,
                position = markerPostDocument.position
            )
        )
        return markerPostInfo
    }
}