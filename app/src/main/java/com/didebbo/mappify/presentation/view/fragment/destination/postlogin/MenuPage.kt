package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.databinding.MenuPageLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.NewMarkerPointActivity
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
import com.didebbo.mappify.presentation.view.component.menu.recyclerview.MenuPageAdapter
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel

class MenuPage: BaseFragmentDestination<PostLoginViewModel>(PostLoginViewModel::class.java) {

    private val postLoginActivity: PostLoginActivity? by lazy { parentActivity as? PostLoginActivity }

    private lateinit var binding: MenuPageLayoutBinding
    private lateinit var recyclerView: RecyclerView

    private val menuItemsData: List<MenuPageAdapter.ItemViewData> = listOf(
        MenuPageAdapter.ItemViewData(
            title = "User Details",
            action = {
                val bundle = Bundle().apply {
                    putString("userId", viewModel.ownerUserDocument?.id)
                }
                navController?.navigate(R.id.user_detail_activity_navigation,bundle)
            }
        ),
        MenuPageAdapter.ItemViewData(
            title = "Add New Marker Point",
            action = {
                val mapCenter = viewModel.currentPosition.geoPoint
                mapCenter.let {
                    val  centerPoint =  MarkerPostDocument.GeoPoint(mapCenter.latitude,mapCenter.longitude)
                    val bundle = Bundle().apply {
                        putDouble("latitude", centerPoint.latitude)
                        putDouble("longitude",centerPoint.longitude)
                    }
                    val intent = Intent(this.context,NewMarkerPointActivity::class.java).apply {
                        putExtras(bundle)
                    }
                    postLoginActivity?.navigateToIntentWithDismissDestination(
                        intent,
                        R.id.map_view_page_navigation_fragment
                    )
                }
            }
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MenuPageLayoutBinding.inflate(inflater,container,false)
        recyclerView = binding.menuRecyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchOwnerUserDocument()

        recyclerView.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
        recyclerView.adapter = MenuPageAdapter(menuItemsData)


        binding.logOutItemMenu.setOnClickListener{
            postLoginActivity?.navigateToPreLogin()
        }
    }

    private fun fetchOwnerUserDocument() {
        var userDocument: UserDocument? = null
        viewModel.ownerUserDocument?.let {
            bindUser(it)
            return
        }
        parentActivity?.loaderCoroutineScope {
            val userDocumentResult = viewModel.getOwnerUserDocument()
            userDocumentResult.exceptionOrNull()?.let {
                parentActivity?.showAlertView(it.localizedMessage ?: "")
            }
            userDocumentResult.getOrNull()?.let {
                bindUser(it)
            }
        }
    }

    private fun bindUser(data: UserDocument) {
        binding.avatarNameTextView.text = data.getAvatarName()
        binding.userNameTextView.text = data.getFullName()
        binding.userEmailTextView.text = data.email
        binding.postCounterTextVIew.text = "Marker Posts: ${data.markerPostsIds.size}"
    }
}