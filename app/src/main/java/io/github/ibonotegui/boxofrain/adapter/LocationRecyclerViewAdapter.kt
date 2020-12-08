package io.github.ibonotegui.boxofrain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.ibonotegui.boxofrain.R
import io.github.ibonotegui.boxofrain.data.Location
import io.github.ibonotegui.boxofrain.databinding.LocationListItemLayoutBinding

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class LocationRecyclerViewAdapter(
    private val context: Context,
    private val onLocationClickListener: OnLocationClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var locations = emptyList<Location>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.location_list_header_layout, parent, false)
            )
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.location_list_item_layout, parent, false)
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return ITEM_VIEW_TYPE_HEADER
        }
        return ITEM_VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int = if (locations.isNotEmpty()) locations.size + 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position > 0) {
            val location = this.locations[position - 1]
            val itemViewHolder = holder as ItemViewHolder
            itemViewHolder.locationNameTextView.text = location.name
            itemViewHolder.locationDefaultTextView.text = if (location.isDefault) "default" else ""
            itemViewHolder.root.setOnClickListener {
                onLocationClickListener.onClick(location)
            }
        }
    }

    internal fun setData(locations: List<Location>) {
        this.locations = locations
        notifyDataSetChanged()
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: LocationListItemLayoutBinding =
            LocationListItemLayoutBinding.bind(view)
        val root: View = binding.root
        val locationNameTextView: TextView = binding.locationNameTextView
        val locationDefaultTextView: TextView = binding.locationDefaultTextView
    }

    //pass just the name?
    class OnLocationClickListener(val clickListener: (location: Location) -> Unit) {
        fun onClick(location: Location) {
            clickListener(location)
        }
    }

}
