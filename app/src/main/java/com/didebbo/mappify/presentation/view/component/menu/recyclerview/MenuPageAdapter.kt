package com.didebbo.mappify.presentation.view.component.menu.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.R
import com.didebbo.mappify.databinding.MenuPageItemLayoutBinding

class MenuPageAdapter(private val ctx: Context, private val data: List<ItemViewData>): RecyclerView.Adapter<MenuPageAdapter.ItemView>() {

    class ItemViewData(
        val title: String,
        val action: ()->Unit
    )
    class ItemView(val binding: MenuPageItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val binding = MenuPageItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if(viewType > 0) { binding.root.background = ctx.getDrawable(R.drawable.recycler_view_item_background) }
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