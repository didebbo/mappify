package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.MarkerPostsListLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.component.markerpost.recyclerview.adapter.MarkerPostAdapter
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            getMarkerPostList()?.let { data ->
                setRecyclerView(data)
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

    private fun setRecyclerView(data: List<MarkerPostDocument>) {
        val recyclerView = binding.recyclerView
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