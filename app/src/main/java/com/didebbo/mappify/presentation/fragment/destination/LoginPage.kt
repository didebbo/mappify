package com.didebbo.mappify.presentation.fragment.destination

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.LoginPageLayoutBinding

class LoginPage: BaseFragmentPage<PreLoginViewModel>(PreLoginViewModel::class.java) {

    private lateinit var loginPageLayoutBinding: LoginPageLayoutBinding

    override fun onCreateView(
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
            parentActivity?.navController?.navigate(resId = R.id.navigate_from_loginPage_to_registerPage)
        }

        loginPageLayoutBinding.loginButton.setOnClickListener {
            val email = loginPageLayoutBinding.emailTextField.text.toString()
            val password = loginPageLayoutBinding.passwordTextField.text.toString()
            viewModel.signInWithEmailAndPassword(email,password)
        }

        loginPageLayoutBinding.loginWithGoogleButton.setOnClickListener {
            Log.i("gn", "${viewModel.getUser()?.email}")
        }
    }
}