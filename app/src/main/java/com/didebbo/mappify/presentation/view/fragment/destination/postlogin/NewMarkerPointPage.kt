package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.databinding.AddMarkerPointLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.AddNewMarkerPointViewModel
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel

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
        binding.latitudeEditText.setText(viewModel.getCoordinates().latitude.toString())
        binding.longitudeEditText.setText(viewModel.getCoordinates().longitude.toString())
    }
}