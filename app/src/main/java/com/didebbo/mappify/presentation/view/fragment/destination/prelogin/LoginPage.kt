package com.didebbo.mappify.presentation.view.fragment.destination.prelogin

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.PreLoginActivity
import com.didebbo.mappify.presentation.viewmodel.PreLoginViewModel
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.databinding.LoginPageLayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginPage: BaseFragmentDestination<PreLoginViewModel>(PreLoginViewModel::class.java) {

    private lateinit var binding: LoginPageLayoutBinding
    private val preLoginActivity: PreLoginActivity? by lazy { parentActivity as? PreLoginActivity }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginPageLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser().observe(viewLifecycleOwner) {
            binding.userEmailTextView.text = it?.email
            binding.avatarNameTextView.visibility = if(it != null) View.VISIBLE else View.GONE
            binding.userEmailTextView.visibility = if(it != null) View.VISIBLE else View.GONE
            binding.emailTextField.visibility = if(it == null) View.VISIBLE else View.GONE
            binding.createNewAccountButton.visibility = if(it == null) View.VISIBLE else View.GONE
            it?.let {
                parentActivity?.loaderCoroutineScope {
                    getOwnerUserDocument()?.let { userDocument ->
                        binding.avatarNameTextView.text = userDocument.getAvatarName()
                        userDocument.avatarColor?.let { avatarColor ->
                            parentActivity?.getColor(avatarColor)?.let { resId ->
                                binding.avatarNameTextView.backgroundTintList = ColorStateList.valueOf(resId)
                            }
                        }
                    }
                }
            }
        }

        binding.createNewAccountButton.setOnClickListener {
            navController?.navigate(resId = R.id.register_page_navigation_fragment)
        }

        binding.signInButton.setOnClickListener {

            val userAuth = UserAuth(
                email = viewModel.getUser().value?.email ?: binding.emailTextField.text.toString(),
                password = binding.passwordTextField.text.toString()
            )

            parentActivity?.hideSystemKeyboard()

            parentActivity?.loaderCoroutineScope {
                viewModel.signInWithEmailAndPassword(userAuth).let { result ->
                    result.exceptionOrNull()?.let {
                        val message = it.localizedMessage ?: "Undefined Error"
                        parentActivity?.showAlertView(message)
                    }
                    result.getOrNull()?.let {
                        preLoginActivity?.navigateToPostLogin()
                    }
                }
            }
        }

        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
        }
    }

    override fun onStop() {
        super.onStop()
        binding.emailTextField.setText("")
        binding.passwordTextField.setText("")
    }

    private suspend fun getOwnerUserDocument(): UserDocument? {
        return withContext(Dispatchers.IO) {
            val result = viewModel.getOwnerUserDocument()
            result.exceptionOrNull()?.let {
                parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
            }
            result.getOrNull()
        }
    }
}