package com.didebbo.mappify.presentation.view.component.spinner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.didebbo.mappify.data.model.Position
import com.didebbo.mappify.databinding.SpinnerDropdownItemBinding

class SpinnerArrayAdapter(private val ctx: Context, var data: MutableList<Position>): BaseAdapter() {
    class ViewHolder(val binding: SpinnerDropdownItemBinding)
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Position {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            // Se convertView è null, inflatamo una nuova vista e creiamo un nuovo ViewHolder
            val binding =
                SpinnerDropdownItemBinding.inflate(LayoutInflater.from(ctx), parent, false)
            view = binding.root
            viewHolder = ViewHolder(binding)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = data[position]
        viewHolder.binding.textView.text = "${item.name}"
        return view
    }
}