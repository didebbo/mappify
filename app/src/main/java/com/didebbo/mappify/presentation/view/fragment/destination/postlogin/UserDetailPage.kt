package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.databinding.AddMarkerPointLayoutBinding
import com.didebbo.mappify.databinding.UserDetailLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.component.markerpost.recyclerview.adapter.MarkerPostAdapter
import com.didebbo.mappify.presentation.viewmodel.AddNewMarkerPointViewModel
import com.didebbo.mappify.presentation.viewmodel.UserDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

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

        setObservable()
        setListener()

        parentActivity?.loaderCoroutineScope {
            val userOwnerDocument = getUserOwnerDocument()
            val userDocument = getUserDocument()


            userDocument?.let { other ->
                bindUserDocument(other)
                val userMarkerPosts = getUserMarkerPost(other.id)
                userMarkerPosts?.let { markerPosts ->
                    configureMarkerPostRecyclerView(markerPosts)
                }
                userOwnerDocument?.let { owner ->
                    bindUserOwnerDocument(owner, other)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean? {
        parentActivity?.finish()
        return true
    }

    private fun setObservable() {
        viewModel.editingMode.observe(viewLifecycleOwner) { isEditable ->
            binding.editDescriptionButton.text = if(isEditable) "Done" else "Edit"
            binding.descriptionEditText.isEnabled = isEditable
        }
    }

    private fun setListener() {
        binding.editDescriptionButton.setOnClickListener {
            val description = binding.descriptionEditText.text.toString()
            parentActivity?.loaderCoroutineScope {
                viewModel.updateOwnerUser(description)
            }
        }
    }

    private suspend fun getUserOwnerDocument(): UserDocument? {
        val ownerUserDocumentResult = viewModel.fetchOwnerUserDocument()
        ownerUserDocumentResult.exceptionOrNull()?.let { e ->
            parentActivity?.showAlertView(e.localizedMessage ?: "UndefinedError")
        }
        return ownerUserDocumentResult.getOrNull()
    }

    private suspend fun getUserDocument(): UserDocument? {
        val userDocumentResult = viewModel.fetchUserDocument(viewModel.userId)
        userDocumentResult.exceptionOrNull()?.let { e ->
            parentActivity?.showAlertView(e.localizedMessage ?: "UndefinedError")
        }
        return userDocumentResult.getOrNull()
    }

    private suspend fun getUserMarkerPost(userId: String): List<MarkerPostDocument>? {
        val userMarkerPostsResult = viewModel.fetchUserMarkerPosts(userId)
        userMarkerPostsResult.exceptionOrNull()?.let {
            parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
        }
        return userMarkerPostsResult.getOrNull()
    }

    private fun bindUserDocument(data: UserDocument) {
        binding.avatarNameTextView.text = data.getAvatarName()
        binding.userNameTextView.text = data.getFullName()
        binding.userEmailTextView.text = data.email
        binding.postCounterTextVIew.text = "Marker Posts: ${data.markerPostsIds.size}"
        binding.descriptionEditText.setText(data.description)
    }

    private fun bindUserOwnerDocument(owner: UserDocument, other: UserDocument) {
        viewModel.ownerUserDocument = owner
        val isOwner = owner.id == other.id
        if(isOwner) binding.editDescriptionButton.visibility = View.VISIBLE
    }
    private fun configureMarkerPostRecyclerView(data: List<MarkerPostDocument>) {
        val recyclerView = binding.userMarkerPostRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context).apply { orientation = LinearLayoutManager.VERTICAL }
        val data = data.map {
            MarkerPostAdapter.ViewHolder.Data.fromMarkerDocument(it).copy(
                onCLick = {
                    parentActivity?.showAlertView(it.title)
                }
            )
        }
        recyclerView.adapter = MarkerPostAdapter(data)
    }
}