package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.Position
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.databinding.MarkerPostsListLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.component.markerpost.recyclerview.adapter.MarkerPostAdapter
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint

class MarkerPostsListPage: BaseFragmentDestination<PostLoginViewModel>(PostLoginViewModel::class.java) {

    private lateinit var binding: MarkerPostsListLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MarkerPostsListLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentActivity?.loaderCoroutineScope {
            val markerPosts = getMarkerPostList()
            val userDocuments = getUserDocuments()
            markerPosts?.map { markerPost ->
                MarkerPostAdapter.ViewHolder.Data(
                    markerPost.id,
                    markerPost.title,
                    userDocuments?.firstOrNull { it.id == markerPost.ownerId },
                    markerPost.position.latitude,
                    markerPost.position.longitude
                )
            }?.let {
                setRecyclerView(it)
            }
        }
    }

    private suspend fun getMarkerPostList(): List<MarkerPostDocument>? {
        return withContext(Dispatchers.IO) {
            val result = viewModel.fetchMarkerPostDocuments()
            result.exceptionOrNull()?.let {
                parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
            }
            result.getOrNull()
        }
    }

    private suspend fun getUserDocuments(): List<UserDocument>? {
        return withContext(Dispatchers.IO) {
            val result = viewModel.fetchUserDocuments()
            result.exceptionOrNull()?.let {
                parentActivity?.showAlertView(it.localizedMessage ?: "Undefined Error")
            }
            result.getOrNull()
        }
    }

    private fun setRecyclerView(data: List<MarkerPostAdapter.ViewHolder.Data>) {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context).apply { orientation = LinearLayoutManager.VERTICAL }
        val data = data.map {
            it.copy(
                onCLick = {
                    val bundle = Bundle().apply {
                        val position = Position("CUSTOM", it.title.uppercase(), GeoPoint(it.latitude,it.longitude))
                        putSerializable("navigateTo",position)
                    }
                    parentActivity?.navController?.navigate(R.id.map_view_page_navigation_fragment,bundle)
                }
            )
        }
        recyclerView.adapter = MarkerPostAdapter(this,data)
    }

}