package com.didebbo.mappify.presentation.view.component.markerpost.infowindow

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.MarkerPostLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.UserDetailActivity
import com.didebbo.mappify.presentation.view.fragment.destination.postlogin.MapViewPage
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MarkerPostInfoWindow(parent: Fragment, mapView: MapView, private val data: ViewData): InfoWindow(
    R.layout.marker_post_layout, mapView) {

    private val binding: MarkerPostLayoutBinding =
        MarkerPostLayoutBinding.inflate(LayoutInflater.from(mapView.context), mapView, false)

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

        (parentDestination as? MapViewPage)?.let { mapViewPage ->
            mapViewPage._visibleMarkerPosts.value?.apply {
                this.add(this@MarkerPostInfoWindow)
                mapViewPage._visibleMarkerPosts.postValue(this)
            }
        }

        binding.root.setOnClickListener{
            close()
        }

        parentDestination?.lifecycleScope?.launch {
            postLoginViewModel?.getUserDocument(data.ownerId)?.let { userDocumentResult ->
                userDocumentResult.exceptionOrNull()?.let {
                    parentDestination.parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
                    close()
                }
                userDocumentResult.getOrNull()?.let { userDocument ->
                    userDocument.avatarColor?.let { resId ->
                        parentDestination.context?.getColor(resId)?.let {
                            binding.avatarIconText.backgroundTintList = ColorStateList.valueOf(it)
                        }
                    }
                    binding.avatarIconText.text = userDocument.getAvatarName()
                    binding.userNameText.text = userDocument.getFullName()
                    binding.postTitleText.text = data.title
                    binding.postDescriptionText.text = data.description
                    binding.coordinateText.text = "${String.format("%.4f",data.position.latitude)},${String.format("%.4f",data.position.longitude)}"
                    binding.root.visibility = View.VISIBLE

                    binding.userNameText.setOnClickListener {
                        val intent = Intent(parentDestination.context,UserDetailActivity::class.java).apply {
                            putExtra("userId", userDocument.id)
                        }
                        parentDestination.parentActivity?.navigateToIntentWithDismissDestination(intent)
                        parentDestination.lifecycleScope.launch {
                            delay(1000)
                            close()
                        }
                    }
                }
            }
        }
    }

    override fun onClose() {
        (parentDestination as? MapViewPage)?.let { mapViewPage ->
            mapViewPage._visibleMarkerPosts.value?.apply{
                this.remove(this@MarkerPostInfoWindow)
                mapViewPage._visibleMarkerPosts.postValue(this)
            }
        }
    }
}