package com.didebbo.mappify.presentation.fragment.destination

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.LoginPageLayoutBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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

        loginPageLayoutBinding.createNewAccountButton.setOnClickListener {
            navController?.navigate(resId = R.id.navigate_from_loginPage_to_registerPage)
        }

        loginPageLayoutBinding.signInButton.setOnClickListener {

            val userAuth = UserAuth(
                email = loginPageLayoutBinding.emailTextField.text.toString(),
                password = loginPageLayoutBinding.passwordTextField.text.toString()
            )

            lifecycleScope.launch {
                if(viewModel.signInWithEmailAndPassword(userAuth)) {
                    val postLogin = Intent(context,PostLoginActivity::class.java)
                    startActivity(postLogin)
                } else {
                    Snackbar.make(loginPageLayoutBinding.root,"Invalid User", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        loginPageLayoutBinding.signOutButton.setOnClickListener {

        }
    }
}