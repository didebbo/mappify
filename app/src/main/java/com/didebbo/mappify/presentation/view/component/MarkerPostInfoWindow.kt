package com.didebbo.mappify.presentation.view.component

import android.view.LayoutInflater
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.MarkerPostLayoutBinding
import com.didebbo.mappify.domain.repository.MarkerPostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerPostInfoWindowFactory @Inject constructor(
    private val repository: MarkerPostRepository
) {
    fun create(
        mapView: MapView,
        data: MarkerPostInfoWindow.ViewData
    ): MarkerPostInfoWindow {
        return MarkerPostInfoWindow(repository, mapView, data)
    }
}

class MarkerPostInfoWindow(private val repository: MarkerPostRepository, private val mapView: MapView, private val data: ViewData): InfoWindow(R.layout.marker_post_layout, mapView) {

    private val binding: MarkerPostLayoutBinding =
        MarkerPostLayoutBinding.inflate(LayoutInflater.from(mapView.context),null,false)

    init { mView = binding.root }
    data class ViewData(
        val ownerId: String,
        val title: String,
        val description: String,
        val position: MarkerPostDocument.GeoPoint
    )
    override fun onOpen(item: Any?) {

        CoroutineScope(Dispatchers.IO).launch {
            val userDocumentResult = repository.getUserDocument(data.ownerId)
            userDocumentResult.exceptionOrNull()?.let {
                TODO()
            }
            userDocumentResult.getOrNull()?.let { userDocument ->
                withContext(Dispatchers.Main) {
                    binding.avatarIconText.text = userDocument.getAvatarName()
                    binding.userNameText.text = userDocument.getFullName()
                    binding.postTitleText.text = data.title
                    binding.postDescriptionText.text = data.description
                    binding.coordinateText.text = "${String.format("%.4f",data.position.latitude)},${String.format("%.4f",data.position.longitude)}"
                }
            }
        }
    }

    override fun onClose() {

    }
}