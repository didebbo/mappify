package com.didebbo.mappify.presentation.view.component.menu.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.databinding.MenuPageItemLayoutBinding

class MenuPageAdapter(private val data: List<ItemViewData>): RecyclerView.Adapter<MenuPageAdapter.ItemView>() {

    class ItemViewData(
        val title: String,
        val action: ()->Unit
    )
    class ItemView(val binding: MenuPageItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val binding =
            MenuPageItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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