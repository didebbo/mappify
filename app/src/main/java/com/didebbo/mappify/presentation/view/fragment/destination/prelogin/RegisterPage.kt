package com.didebbo.mappify.presentation.view.fragment.destination.prelogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.databinding.RegisterPageLayoutBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterPage: BaseFragmentDestination<PreLoginViewModel>(PreLoginViewModel::class.java) {

    private lateinit var registerPageLayoutBinding: RegisterPageLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // parentActivity?.hideBackButton()
        registerPageLayoutBinding = RegisterPageLayoutBinding.inflate(inflater,container,false)
        return registerPageLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerPageLayoutBinding.emailRegisterButton.setOnClickListener {

            parentActivity?.hideSystemKeyboard()

            val userAuth = UserAuth(
                name = registerPageLayoutBinding.nameTextField.text.toString(),
                surname = registerPageLayoutBinding.surnameTextField.text.toString(),
                email = registerPageLayoutBinding.emailTextField.text.toString(),
                password = registerPageLayoutBinding.passwordTextField.text.toString()
            )

            lifecycleScope.launch {

                viewModel.createUserWithEmailAndPassword(userAuth).let { result ->
                    result.exceptionOrNull()?.let {
                        Snackbar.make(registerPageLayoutBinding.root, it.localizedMessage ?: "Undefined Error",Snackbar.LENGTH_SHORT).show()
                        return@launch
                    }
                    result.getOrNull()?.let {
                        navController?.popBackStack()
                    }
                }
            }
        }
    }
}