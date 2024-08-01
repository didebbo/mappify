package com.didebbo.mappify.presentation.fragment.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentPage
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.github.didebbo.mappify.databinding.RegisterPageLayoutBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterPage: BaseFragmentPage<PreLoginViewModel>(PreLoginViewModel::class.java) {

    private lateinit var registerPageLayoutBinding: RegisterPageLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerPageLayoutBinding = RegisterPageLayoutBinding.inflate(inflater,container,false)
        return registerPageLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerPageLayoutBinding.emailRegisterButton.setOnClickListener {
            val userAuth = UserAuth(
                email = registerPageLayoutBinding.emailTextField.text.toString(),
                password = registerPageLayoutBinding.passwordTextField.text.toString()
            )
            if(!userAuth.isUserAuthValid()) {
                Snackbar.make(registerPageLayoutBinding.root,"Invalid User",Snackbar.LENGTH_SHORT).show()
            }
            viewModel.createUserWithEmailAndPassword(userAuth)
        }
    }
}