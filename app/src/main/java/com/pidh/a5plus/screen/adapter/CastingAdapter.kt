package com.pidh.a5plus.screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pidh.a5plus.databinding.ItemCastingBinding
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.provider.api.entities.Casting

class CastingAdapter(
    private val glide: RequestManager
) : RecyclerView.Adapter<CastingAdapter.ViewHolder>() {

    var listOfCasting: List<Casting> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCastingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listOfCasting[position])
    }

    override fun getItemCount(): Int = listOfCasting.size

    inner class ViewHolder(private val viewBinding: ItemCastingBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(data: Casting) {
            viewBinding.apply {
                glide.load(Constants.BASE_URL_PROFILE + data.profilePath).into(imgCasting)
                txtName.text = data.name
            }
        }
    }
}