package com.didebbo.mappify.presentation.view.fragment.destination.postlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.R
import com.didebbo.mappify.databinding.MenuPageItemLayoutBinding
import com.didebbo.mappify.databinding.MenuPageLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.view.activity.PostLoginActivity
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
                navController?.navigate(R.id.new_marker_point_navigation_activity)
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

        recyclerView.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
        recyclerView.adapter = MenuPageAdapter(menuItemsData)


        binding.logOutItemMenu.setOnClickListener{
            postLoginActivity?.navigateToPreLogin()
        }
    }
}

class MenuPageAdapter(private val data: List<ItemViewData>): RecyclerView.Adapter<MenuPageAdapter.ItemView>() {

    class ItemViewData(
        val title: String,
        val action: ()->Unit
    )
    class ItemView(val binding: MenuPageItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val binding = MenuPageItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemView(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        val item = getItemViewData(position)
        holder.binding.menuItem.text = item.title
        holder.binding.menuItem.setOnClickListener {
            item.action()
        }
    }

    private fun getItemViewData(position: Int): ItemViewData {
        return data[position]
    }
}