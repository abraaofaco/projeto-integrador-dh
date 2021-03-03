package com.pidh.a5plus.screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pidh.a5plus.databinding.ItemPlaceBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.provider.maps.entities.PlaceInfo
import java.text.DecimalFormat

class PlacesAdapter(
    private val onAppClickListener: OnAppClickListener
) :
    RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private var listOfPlaces: MutableList<PlaceInfo> = mutableListOf()

    fun addPlace(place: PlaceInfo) {
        if (!listOfPlaces.contains(place)) {
            listOfPlaces.add(place)

            listOfPlaces.sortBy {
                it.distance
            }

            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPlaceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var dataItem = listOfPlaces[position];

        holder.let {
            holder.bindView(dataItem)
        }
    }

    override fun getItemCount(): Int = listOfPlaces.size

    inner class ViewHolder(private val viewBinding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(viewBinding.root),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindView(data: PlaceInfo) {

            viewBinding.apply {
                val df = DecimalFormat("#.#")
                val distance = "${df.format(data.distance)} Km"

                txtName.text = data.name
                txtAddress.text = data.address
                txtDistance.text = distance
            }
        }

        override fun onClick(v: View?) {
            val place = listOfPlaces.elementAt(adapterPosition)
            onAppClickListener.onItemClick(place)
        }
    }
}