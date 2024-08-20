package com.didebbo.mappify.presentation.view.fragment.destination.prelogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.data.model.AvatarColor
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.databinding.RegisterPageLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterPage: BaseFragmentDestination<PreLoginViewModel>(PreLoginViewModel::class.java) {

    private lateinit var binding: RegisterPageLayoutBinding
    private lateinit var colorPicker: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterPageLayoutBinding.inflate(inflater,container,false)
        colorPicker = binding.colorPickerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameTextField.doOnTextChanged { n, _, _, _ ->
            val name = n?.firstOrNull()?.uppercase() ?: "U"
            val surname = binding.surnameTextField.text.firstOrNull()?.uppercase() ?: "N"
         binding.avatarNameTextView.text = "$name$surname"
        }

        binding.surnameTextField.doOnTextChanged { s, _, _, _ ->
            val name = binding.nameTextField.text.firstOrNull()?.uppercase() ?: "U"
            val surname = s?.firstOrNull()?.uppercase() ?: "N"
            binding.avatarNameTextView.text = "$name$surname"
        }

        binding.emailRegisterButton.setOnClickListener {

            parentActivity?.hideSystemKeyboard()

            val userAuth = UserAuth(
                name = binding.nameTextField.text.toString(),
                surname = binding.surnameTextField.text.toString(),
                email = binding.emailTextField.text.toString(),
                password = binding.passwordTextField.text.toString(),
                avatarColor = AvatarColor.AvatarColors.entries.random().avatarColor
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