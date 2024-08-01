package com.didebbo.mappify.presentation.view.activity

import androidx.activity.viewModels
import com.didebbo.mappify.presentation.baseclass.activity.BaseActivity
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.R

class PreLoginActivity: BaseActivity<PreLoginViewModel>() {
    override val navigationId: Int = R.navigation.pre_login_navigation
    override val viewModel: PreLoginViewModel by viewModels()
}