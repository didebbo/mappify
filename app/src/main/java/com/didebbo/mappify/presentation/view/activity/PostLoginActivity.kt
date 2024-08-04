package com.didebbo.mappify.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityNavigator
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import com.github.didebbo.mappify.R
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