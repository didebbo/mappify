package com.didebbo.mappify.presentation.view.activity

import android.content.Intent
import androidx.activity.viewModels
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivityNavigator
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreLoginActivity: BaseActivityNavigator<PreLoginViewModel>() {
    override val navigationId: Int = R.navigation.pre_login_navigation
    override val viewModel: PreLoginViewModel by viewModels()

    fun navigateToPostLogin() {
        navigateToActivity(Intent(this,PostLoginActivity::class.java))
    }
}