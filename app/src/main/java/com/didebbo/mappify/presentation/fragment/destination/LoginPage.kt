package com.didebbo.mappify.presentation.fragment.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.didebbo.mappify.presentation.view.activity.PreLoginActivity
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.R
import com.github.didebbo.mappify.databinding.LoginPageLayoutBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginPage: BaseFragmentPage<PreLoginViewModel>(PreLoginViewModel::class.java) {

    private lateinit var loginPageLayoutBinding: LoginPageLayoutBinding
    private val preLoginActivity: PreLoginActivity? by lazy { parentActivity as? PreLoginActivity }

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

        viewModel.getUser().observe(viewLifecycleOwner) {
            loginPageLayoutBinding.userEmailTextField.text = it?.email
            loginPageLayoutBinding.userEmailTextField.visibility = if(it != null) View.VISIBLE else View.GONE
            loginPageLayoutBinding.emailTextField.visibility = if(it == null) View.VISIBLE else View.GONE
            loginPageLayoutBinding.createNewAccountButton.visibility = if(it == null) View.VISIBLE else View.GONE
        }

        loginPageLayoutBinding.createNewAccountButton.setOnClickListener {
            navController?.navigate(resId = R.id.navigate_from_loginPage_to_registerPage)
        }

        loginPageLayoutBinding.signInButton.setOnClickListener {

            val userAuth = UserAuth(
                email = viewModel.getUser().value?.email ?: loginPageLayoutBinding.emailTextField.text.toString(),
                password = loginPageLayoutBinding.passwordTextField.text.toString()
            )

            parentActivity?.hideSystemKeyboard()

            lifecycleScope.launch {
                viewModel.signInWithEmailAndPassword(userAuth).let { result ->
                    result.exceptionOrNull()?.let {
                        Snackbar.make(loginPageLayoutBinding.root, it.localizedMessage ?: "Undefined Error",Snackbar.LENGTH_SHORT).show()
                    }
                    result.getOrNull()?.let {
                        preLoginActivity?.navigateToPostLogin()
                    }
                }
            }
        }

        loginPageLayoutBinding.signOutButton.setOnClickListener {
            viewModel.signOut()
        }
    }
}