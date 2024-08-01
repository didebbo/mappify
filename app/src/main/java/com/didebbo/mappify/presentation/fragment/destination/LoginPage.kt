package com.didebbo.mappify.presentation.fragment.destination

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.didebbo.mappify.presentation.view.activity.PreLoginActivity
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.LoginPageLayoutBinding

class LoginPage: BaseFragmentPage() {

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
            val intent = Intent(context,PostLoginActivity::class.java)
            startActivity(intent)
        }
    }
}