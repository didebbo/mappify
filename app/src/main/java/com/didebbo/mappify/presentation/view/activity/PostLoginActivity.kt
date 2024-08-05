package com.didebbo.mappify.presentation.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.didebbo.mappify.R
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityNavigator
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostLoginActivity: BaseActivityNavigator<PostLoginViewModel>() {

    override val viewModel: PostLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSystemNavigation(R.navigation.post_login_navigation)
    }

    fun navigateToPreLogin() {
        finish()
    }
}