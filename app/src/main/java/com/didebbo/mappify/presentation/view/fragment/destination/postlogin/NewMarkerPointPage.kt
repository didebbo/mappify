package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.Position
import com.didebbo.mappify.databinding.AddMarkerPointLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.AddNewMarkerPointViewModel
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class NewMarkerPointPage: BaseFragmentDestination<AddNewMarkerPointViewModel>(AddNewMarkerPointViewModel::class.java) {

    private lateinit var binding: AddMarkerPointLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddMarkerPointLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentActivity?.showBackButton(true)

        val latitude = String.format("%.4f",viewModel.getCoordinates().latitude)
        val longitude = String.format("%.4f",viewModel.getCoordinates().longitude)

        binding.latitudeEditText.setText(latitude)
        binding.longitudeEditText.setText(longitude)
        binding.saveButton.setOnClickListener {
            parentActivity?.loaderCoroutineScope {
                val addNewMarkerPostResult = viewModel.addNewMarkerPost(
                    MarkerPostDocument(
                        title = binding.titleEditText.text.toString(),
                        description = binding.descriptionEditText.text.toString(),
                        position = MarkerPostDocument.GeoPoint(
                            binding.latitudeEditText.text.toString().toDouble(),
                            binding.longitudeEditText.text.toString().toDouble()
                        )
                    )
                )
                addNewMarkerPostResult.exceptionOrNull()?.let {
                    parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
                }
                addNewMarkerPostResult.getOrNull()?.let {
                    val resultIntent = Intent().apply {
                        putExtra("destination",Bundle().apply {
                            putInt("resIdDestination", R.id.map_view_page_navigation_fragment)
                            putBundle("resDestinationBundle", Bundle().apply {
                                val position = Position("CUSTOM",it.title, it.position.toIGeoPoint())
                                putSerializable("navigateTo",position)
                            })
                        })
                    }
                    parentActivity?.setResult(Activity.RESULT_OK, resultIntent)
                    onSupportNavigateUp()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.latitudeEditText.isEnabled = viewModel.editCoordinates
        binding.longitudeEditText.isEnabled = viewModel.editCoordinates
    }

    override fun onSupportNavigateUp(): Boolean? {
        parentActivity?.finish()
        return true
    }
}