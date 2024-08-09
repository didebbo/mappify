package com.didebbo.mappify.presentation.view.component.markerpost.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.MarkerPostItemLayoutBinding

class MarkerPostAdapter(private val data: List<MarkerPostDocument>): RecyclerView.Adapter<MarkerPostAdapter.ViewHolder>()  {

    class ViewHolder(val binding: MarkerPostItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MarkerPostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkerPostAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItemData(position)
        holder.binding.titleTextView.text = data.title
        val latitude = String.format("%.4f",data.position.latitude)
        val longitude = String.format("%.4f",data.position.longitude)
        holder.binding.geoPointTextView.text = "$latitude , $longitude"
    }

    private fun getItemData(position: Int): MarkerPostDocument {
        return data[position]
    }
}