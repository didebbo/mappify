package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.AvatarColor
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.Position
import com.didebbo.mappify.databinding.MapViewLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.component.markerpost.infowindow.MarkerPostInfoWindow
import com.didebbo.mappify.presentation.view.component.markerpost.infowindow.MarkerPostInfoWindowFactory
import com.didebbo.mappify.presentation.view.component.spinner.adapter.SpinnerArrayAdapter
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
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

    private lateinit var overlayLayout: RelativeLayout

    private lateinit var addLocationIndicator: ImageView
    private lateinit var addLocationButton: Button

    private lateinit var citySelectionSpinner: Spinner
    private  lateinit var citySelectionSpinnerAdapter: SpinnerArrayAdapter

    val _visibleMarkerPosts: MutableLiveData<MutableList<MarkerPostInfoWindow>>
    = MutableLiveData(mutableListOf())
    private val visibleMarkerPosts: LiveData<MutableList<MarkerPostInfoWindow>>
        get() = _visibleMarkerPosts

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapViewLayoutBinding.inflate(inflater,container,false)
        mapView = binding.mapView
        mapController = mapView.controller
        overlayLayout = binding.overlayLayout
        addLocationIndicator = binding.addLocationIndicator
        addLocationButton = binding.addLocationButton
        citySelectionSpinner = binding.citySelectionSpinner
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuredMapView()
        configureOverlay()
        bundleNavigateTo()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        lifecycleScope.launch {
            viewModel.fetchMarkerPostDocuments().onFailure {
                parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        lifecycleScope.launch{
            delay(1000)
            viewModel.setEditingMode(false)
        }
    }

    private fun bundleNavigateTo() {
        arguments?.let {
         it.getSerializable("navigateTo",Position::class.java)?.let { customPosition ->
             Log.i("gn","$customPosition")
             viewModel.currentPosition = customPosition
             viewModel.currentGeoPoint = customPosition.geoPoint
             val getCustomItem = citySelectionSpinnerAdapter.data.firstOrNull { item -> item.id == customPosition.id }
             getCustomItem?.let { item -> citySelectionSpinnerAdapter.data.remove(item) }
             citySelectionSpinnerAdapter.data.add(0,customPosition)
             val position = citySelectionSpinnerAdapter.data.indexOf(customPosition)
             citySelectionSpinner.setSelection(position)
             mapController.setCenter(customPosition.geoPoint)
         }
        }
    }

    private fun configuredMapView() {
        parentActivity?.let { parentActivity ->
            Configuration.getInstance().load(parentActivity.applicationContext, parentActivity.getPreferences(
                MODE_PRIVATE))
            mapController.setZoom(15.0)
            mapController.setCenter(viewModel.currentGeoPoint)
        }
    }

    private fun configureOverlay() {

        visibleMarkerPosts.observe(viewLifecycleOwner) {
            setOverlayVisibilityAnimation(it.isEmpty())
        }

        context?.let {
            citySelectionSpinnerAdapter = SpinnerArrayAdapter(it,viewModel.availablePositions)
            citySelectionSpinner.adapter = citySelectionSpinnerAdapter
            var firstInit = true
            citySelectionSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(firstInit) {
                        firstInit = false
                        return
                    }
                    val item = citySelectionSpinnerAdapter.getItem(position)
                    viewModel.currentPosition = item
                    mapController.setCenter(item.geoPoint)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        mapView.addMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                viewModel.currentGeoPoint = mapView.mapCenter
                return true
            }
            override fun onZoom(event: ZoomEvent?): Boolean {
                viewModel.currentGeoPoint = mapView.mapCenter
                return true
            }
        })

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
                    icon = AppCompatResources.getDrawable(requireContext(),R.drawable.map_marker)
                }
                mapView.overlays.add(marker)
            }
        }

        addLocationButton.setOnClickListener {
            viewModel.setEditingMode()
        }

        addLocationIndicator.setOnClickListener{
            val mapCenter = viewModel.currentGeoPoint
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

    private fun setOverlayVisibilityAnimation(setToVisible: Boolean, duration: Long = 1000) {
        if(setToVisible) {
            overlayLayout.visibility = View.VISIBLE
            overlayLayout.animate().alpha(1f ).setDuration(duration).start()
        } else {
            overlayLayout.animate().alpha(0f).setDuration(duration).withEndAction {
                overlayLayout.visibility = View.INVISIBLE
            }.start()
        }
    }

    suspend fun getAvatarColor(id: String): AvatarColor? {
        return withContext(Dispatchers.IO) {
            val result = viewModel.getAvatarColor(id)
            result.exceptionOrNull()?.let {
                parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
            }
            result.getOrNull()
        }
    }
}
