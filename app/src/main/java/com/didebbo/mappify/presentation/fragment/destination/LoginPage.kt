package com.didebbo.mappify.presentation.fragment.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.didebbo.mappify.presentation.view.activity.PreLoginActivity
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.LoginPageLayoutBinding

class LoginPage: BaseFragmentPage() {

    private lateinit var loginPageLayoutBinding: LoginPageLayoutBinding
    private val preLoginActivity: PreLoginActivity? by lazy { parentActivity as? PreLoginActivity }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginPageLayoutBinding = LoginPageLayoutBinding.inflate(inflater,container,false)
        return loginPageLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginPageLayoutBinding.registerButton.setOnClickListener {
            preLoginActivity?.navController?.navigate(R.id.navigate_from_loginPage_to_registerPage)
        }
    }
}