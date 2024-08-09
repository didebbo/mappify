package com.didebbo.mappify.presentation.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityNavigator
import com.didebbo.mappify.presentation.viewmodel.AddNewMarkerPointViewModel
import com.didebbo.mappify.presentation.viewmodel.UserDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity: BaseActivityNavigator<UserDetailViewModel>() {

    override val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSystemNavigation(R.navigation.user_detail_navigation)
        intent?.extras?.getString("userId")?.let {
            viewModel.userId = it
        }
    }
}