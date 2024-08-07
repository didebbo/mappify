package com.didebbo.mappify.presentation.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityNavigator
import com.didebbo.mappify.presentation.viewmodel.AddNewMarkerPointViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewMarkerPointActivity: BaseActivityNavigator<AddNewMarkerPointViewModel>() {

    override val viewModel: AddNewMarkerPointViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSystemNavigation(R.navigation.new_marker_point_navigation)

        val latitude = intent?.extras?.getDouble("latitude")
        val longitude = intent?.extras?.getDouble("longitude")
        viewModel.setupCoordinates(latitude,longitude)
    }
}