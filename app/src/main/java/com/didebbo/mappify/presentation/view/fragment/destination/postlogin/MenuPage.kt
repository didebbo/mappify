package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.R
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
            action = {}
        ),
        MenuPageAdapter.ItemViewData(
            title = "Add New Marker Point",
            action = {
                val intent = Intent(this.context,NewMarkerPointActivity::class.java)
                postLoginActivity?.navigateToIntentWithDismissDestination(
                    intent,
                    R.id.map_view_page_navigation_fragment
                )
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

        Log.i("gn","parentActivity: $parentActivity")

        recyclerView.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
        recyclerView.adapter = MenuPageAdapter(menuItemsData)


        binding.logOutItemMenu.setOnClickListener{
            postLoginActivity?.navigateToPreLogin()
        }
    }
}