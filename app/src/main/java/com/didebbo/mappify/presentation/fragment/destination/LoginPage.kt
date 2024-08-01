package com.didebbo.mappify.presentation.fragment.destination

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.LoginPageLayoutBinding
import com.google.android.material.snackbar.Snackbar

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
            navController?.navigate(resId = R.id.navigate_from_loginPage_to_registerPage)
        }

        loginPageLayoutBinding.loginButton.setOnClickListener {

            val userAuth = UserAuth(
                email = loginPageLayoutBinding.emailTextField.text.toString(),
                password = loginPageLayoutBinding.passwordTextField.text.toString()
            )

            if(!userAuth.isUserAuthValid()) {
                Snackbar.make(loginPageLayoutBinding.root,"Invalid User", Snackbar.LENGTH_SHORT).show()
            }

            viewModel.signInWithEmailAndPassword(userAuth)
        }

        loginPageLayoutBinding.loginWithGoogleButton.setOnClickListener {
            Log.i("gn", "${viewModel.getUser()?.email}")
        }
    }
}