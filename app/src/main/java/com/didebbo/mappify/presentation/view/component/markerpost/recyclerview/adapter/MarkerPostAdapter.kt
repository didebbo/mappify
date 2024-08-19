package com.didebbo.mappify.presentation.view.component.markerpost.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.databinding.MarkerPostItemLayoutBinding
import com.didebbo.mappify.presentation.baseclass.fragment.page.BaseFragmentDestination
import com.didebbo.mappify.presentation.viewmodel.PostLoginViewModel

class MarkerPostAdapter(private val parent: Fragment, private val data: List<ViewHolder.Data>): RecyclerView.Adapter<MarkerPostAdapter.ViewHolder>()  {

    @Suppress("UNCHECKED_CAST")
    private val parentDestination: BaseFragmentDestination<ViewModel>? = parent as? BaseFragmentDestination<ViewModel>
    private val postLoginViewModel: PostLoginViewModel? = parentDestination?.viewModel as? PostLoginViewModel

    class ViewHolder(val binding: MarkerPostItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
     data class Data(
         val id: String,
         val title: String,
         val ownerId: String,
         val latitude: Double,
         val longitude: Double,
         val onCLick: (()->Unit)? = null
     ) {
         companion object {
             fun fromMarkerDocument(data: MarkerPostDocument): Data {
                 return Data(data.id,data.title, data.ownerId, data.position.latitude, data.position.longitude)
             }
         }
     }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MarkerPostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.visibility = View.INVISIBLE
        binding.userTextView.visibility = View.GONE
        parentDestination?.parentActivity?.let {
            if(viewType > 0) { binding.root.background = AppCompatResources.getDrawable(it,R.drawable.recycler_view_item_background) }
        }
        return ViewHolder(binding)
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

        parentDestination?.parentActivity?.loaderCoroutineScope {
            postLoginViewModel?.getUserDocument(data.ownerId)?.onSuccess {
                holder.binding.userTextView.text = it.getFullName()
                holder.binding.userTextView.visibility = View.VISIBLE
            }
            holder.binding.root.visibility = View.VISIBLE
        }
    }

    private fun getItemData(position: Int): ViewHolder.Data {
        return data[position]
    }
}