package com.didebbo.mappify.presentation.view.fragment.destination.prelogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.didebbo.mappify.databinding.RegisterPageLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterPage: BaseFragmentDestination<PreLoginViewModel>(PreLoginViewModel::class.java) {

    private lateinit var binding: RegisterPageLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // parentActivity?.hideBackButton()
        binding = RegisterPageLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailRegisterButton.setOnClickListener {

            parentActivity?.hideSystemKeyboard()

            val userAuth = UserAuth(
                name = binding.nameTextField.text.toString(),
                surname = binding.surnameTextField.text.toString(),
                email = binding.emailTextField.text.toString(),
                password = binding.passwordTextField.text.toString()
            )

            parentActivity?.loaderCoroutineScope {
                viewModel.createUserWithEmailAndPassword(userAuth).let { result ->
                    result.exceptionOrNull()?.let {
                        val message = it.localizedMessage ?: "Undefined Error"
                        parentActivity?.showAlertView(message)
                        return@loaderCoroutineScope
                    }
                    result.getOrNull()?.let {
                        navController?.popBackStack()
                    }
                }
            }
        }
    }
}