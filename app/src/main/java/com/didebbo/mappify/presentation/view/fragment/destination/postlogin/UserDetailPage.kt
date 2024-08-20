package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.AvatarColor
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.Position
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
import org.osmdroid.util.GeoPoint

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
                    val data = markerPosts.map {
                        MarkerPostAdapter.ViewHolder.Data(
                            it.id,
                            it.title,
                            null,
                            it.position.latitude,
                            it.position.longitude
                        )
                    }
                    configureMarkerPostRecyclerView(data)
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
        parentActivity?.loaderCoroutineScope {
            val resId = getAvatarColor(data.avatarColorId)?.resId ?: R.color.avatar_gray
            parentActivity?.getColor(resId)?.let {
                binding.avatarNameTextView.backgroundTintList = ColorStateList.valueOf(it)
            }
            binding.avatarNameTextView.text = data.getAvatarName()
            binding.userNameTextView.text = data.getFullName()
            binding.userEmailTextView.text = data.email
            binding.postCounterTextVIew.text = "Marker Posts: ${data.markerPostsIds.size}"
            binding.descriptionEditText.setText(data.description)
        }
    }

    private fun bindUserOwnerDocument(owner: UserDocument, other: UserDocument) {
        viewModel.ownerUserDocument = owner
        val isOwner = owner.id == other.id
        if(isOwner) binding.editDescriptionButton.visibility = View.VISIBLE
    }
    private fun configureMarkerPostRecyclerView(data: List<MarkerPostAdapter.ViewHolder.Data>) {
        val recyclerView = binding.userMarkerPostRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context).apply { orientation = LinearLayoutManager.VERTICAL }
        val data = data.map {
            it.copy(
                onCLick = {
                    val resultIntent = Intent().apply {
                        putExtra("destination",Bundle().apply {
                            putInt("resIdDestination", R.id.map_view_page_navigation_fragment)
                            putBundle("resDestinationBundle", Bundle().apply {
                                val position = Position("CUSTOM",it.title.uppercase(), GeoPoint(it.latitude,it.longitude))
                                putSerializable("navigateTo",position)
                            })
                        })
                    }
                    parentActivity?.setResult(Activity.RESULT_OK, resultIntent)
                    onSupportNavigateUp()
                }
            )
        }
        recyclerView.adapter = MarkerPostAdapter(this, data)
    }

    suspend fun getAvatarColor(id: String): AvatarColor? {
        val result = viewModel.getAvatarColor(id)
        result.exceptionOrNull()?.let {
            parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
        }
        return result.getOrNull()
    }
}