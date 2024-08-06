package com.didebbo.mappify.presentation.view.component

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.MarkerPostLayoutBinding
import com.didebbo.mappify.domain.repository.MarkerPostRepository
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.AddNewMarkerPointViewModel
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import javax.inject.Inject
import javax.inject.Singleton

class MarkerPostInfoWindowFactory {
    fun create(
        parent: Fragment,
        mapView: MapView,
        data: MarkerPostInfoWindow.ViewData
    ): MarkerPostInfoWindow {
        return MarkerPostInfoWindow(parent,mapView, data)
    }
}

class MarkerPostInfoWindow(parent: Fragment, mapView: MapView, private val data: ViewData): InfoWindow(R.layout.marker_post_layout, mapView) {

    private val binding: MarkerPostLayoutBinding =
        MarkerPostLayoutBinding.inflate(LayoutInflater.from(mapView.context),mapView,false)

    @Suppress("UNCHECKED_CAST")
    private val parentDestination: BaseFragmentDestination<ViewModel>? = parent as? BaseFragmentDestination<ViewModel>
    private val postLoginViewModel: PostLoginViewModel? = parentDestination?.viewModel as? PostLoginViewModel

    init { mView = binding.root }
    data class ViewData(
        val ownerId: String,
        val title: String,
        val description: String,
        val position: MarkerPostDocument.GeoPoint
    )
    override fun onOpen(item: Any?) {

        binding.root.setOnClickListener{
            close()
        }

        parentDestination?.parentActivity?.loaderCoroutineScope {
            val userDocumentResult = postLoginViewModel?.getUserDocument(data.ownerId)
            userDocumentResult?.exceptionOrNull()?.let {
                parentDestination.parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
                close()
            }
            userDocumentResult?.getOrNull()?.let { userDocument ->
                binding.avatarIconText.text = userDocument.getAvatarName()
                binding.userNameText.text = userDocument.getFullName()
                binding.postTitleText.text = data.title
                binding.postDescriptionText.text = data.description
                binding.coordinateText.text = "${String.format("%.4f",data.position.latitude)},${String.format("%.4f",data.position.longitude)}"
                binding.root.visibility = View.VISIBLE
            }
        }
    }

    override fun onClose() {

    }
}