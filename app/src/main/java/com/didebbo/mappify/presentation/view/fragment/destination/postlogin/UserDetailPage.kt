package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.AddMarkerPointLayoutBinding
import com.didebbo.mappify.databinding.UserDetailLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.AddNewMarkerPointViewModel
import com.didebbo.mappify.presentation.viewmodel.UserDetailViewModel

class UserDetailPage: BaseFragmentDestination<UserDetailViewModel>(UserDetailViewModel::class.java) {

    private lateinit var binding: UserDetailLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserDetailLayoutBinding.inflate(inflater,container,false)
        parentActivity?.showBackButton(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentActivity?.loaderCoroutineScope {
            viewModel.fetchUserDocument(viewModel.userId).let {
                it.exceptionOrNull()?.let { e ->
                    parentActivity?.showAlertView(e.localizedMessage ?: "UndefinedError")
                }
                it.getOrNull()?.let { userDocument ->
                    binding.avatarNameTextView.text = userDocument.getAvatarName()
                    binding.userNameTextView.text = userDocument.getFullName()
                    binding.userEmailTextView.text = userDocument.email
                    binding.postCounterTextVIew.text = "Marker Posts: ${userDocument.markerPostsIds.size}"
                    binding.userDescriptionText.text = userDocument.description
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean? {
        parentActivity?.finish()
        return true
    }
}