package com.didebbo.mappify.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.ui.setupActionBarWithNavController
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityNavigator
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreLoginActivity: BaseActivityNavigator<PreLoginViewModel>() {

    override val viewModel: PreLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSystemNavigation(R.navigation.pre_login_navigation)
    }
    fun navigateToPostLogin() {
        navigateToActivity(Intent(this,PostLoginActivity::class.java))
    }
}