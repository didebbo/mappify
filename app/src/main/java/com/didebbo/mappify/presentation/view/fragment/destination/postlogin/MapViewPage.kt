package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.Position
import com.didebbo.mappify.databinding.MapViewLayoutBinding
import com.didebbo.mappify.databinding.SpinnerDropdownItemBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.component.markerpost.infowindow.MarkerPostInfoWindow
import com.didebbo.mappify.presentation.view.component.markerpost.infowindow.MarkerPostInfoWindowFactory
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Random
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

    private lateinit var citySelectionSpinner: Spinner
    private  lateinit var citySelectionSpinnerAdapter: SpinnerArrayAdapter

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
         it.getSerializable("navigateTo",MarkerPostDocument.GeoPoint::class.java)?.let { geoPoint ->
             Log.i("gn","$geoPoint")
             val customPosition = Position("CUSTOM",GeoPoint(geoPoint.latitude,geoPoint.longitude))
             viewModel.currentPosition = customPosition
             viewModel.currentGeoPoint = customPosition.geoPoint
             val getCustomItem = citySelectionSpinnerAdapter.data.firstOrNull() { item -> item.name == customPosition.name }
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
        context?.let { it ->
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
                    item?.let {
                        viewModel.currentPosition = it
                        mapController.setCenter(it.geoPoint)
                    }
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
}

class SpinnerArrayAdapter(private val ctx: Context,var data: MutableList<Position>): BaseAdapter() {
    class ViewHolder(val binding: SpinnerDropdownItemBinding)
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Position {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            // Se convertView è null, inflatamo una nuova vista e creiamo un nuovo ViewHolder
            val binding = SpinnerDropdownItemBinding.inflate(LayoutInflater.from(ctx), parent, false)
            view = binding.root
            viewHolder = ViewHolder(binding)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = data[position]
        viewHolder.binding.textView.text = "${item.name}"
        return view
    }
}