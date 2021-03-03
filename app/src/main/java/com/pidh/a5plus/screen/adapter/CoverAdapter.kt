package com.pidh.a5plus.screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pidh.a5plus.databinding.ItemCoverBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.provider.api.entities.Movie
import javax.inject.Inject

class CoverAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<CoverAdapter.ViewHolder>() {

    var listOfMovies: Set<Movie> = setOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onAppClickListener: OnAppClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCoverBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listOfMovies.elementAt(position))
    }

    override fun getItemCount(): Int = listOfMovies.size

    inner class ViewHolder(private val viewBinding: ItemCoverBinding) :
        RecyclerView.ViewHolder(viewBinding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(data: Movie) {
            glide.load(Constants.BASE_URL_POSTER + data.posterPath).into(viewBinding.imgCover)
        }

        override fun onClick(v: View?) {
            val movie = listOfMovies.elementAt(adapterPosition)

            onAppClickListener?.onItemClick(movie)
        }
    }
}