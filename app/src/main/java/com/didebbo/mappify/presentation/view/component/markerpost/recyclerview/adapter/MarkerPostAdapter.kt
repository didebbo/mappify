package com.didebbo.mappify.presentation.view.component.markerpost.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewManager
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.databinding.MarkerPostItemLayoutBinding

class MarkerPostAdapter(private val data: List<ViewHolder.Data>): RecyclerView.Adapter<MarkerPostAdapter.ViewHolder>()  {

    class ViewHolder(val binding: MarkerPostItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
     data class Data(
         val id: String,
         val title: String,
         val latitude: Double,
         val longitude: Double,
         val onCLick: (()->Unit)? = null
     ) {
         companion object {
             fun fromMarkerDocument(data: MarkerPostDocument): Data {
                 return Data(data.id,data.title, data.position.latitude, data.position.longitude)
             }
         }
     }
    }

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
        val latitude = String.format("%.4f",data.latitude)
        val longitude = String.format("%.4f",data.longitude)
        holder.binding.geoPointTextView.text = "$latitude , $longitude"
        holder.binding.root.setOnClickListener{
            data.onCLick?.invoke()
        }
    }

    private fun getItemData(position: Int): ViewHolder.Data {
        return data[position]
    }
}