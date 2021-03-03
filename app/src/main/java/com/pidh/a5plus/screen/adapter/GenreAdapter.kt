package com.pidh.a5plus.screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pidh.a5plus.databinding.ItemGenreBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.provider.api.model.GenreItemsModel
import javax.inject.Inject

class GenreAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    var genreList: List<GenreItemsModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onAppClickListener: OnAppClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(genreList[position])
    }

    override fun getItemCount(): Int = genreList.size

    inner class ViewHolder(private val viewBinding: ItemGenreBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {


        fun bind(data: GenreItemsModel) {
            viewBinding.apply {
                txtGenreName.text = data.genreName

                val adapter = CoverAdapter(glide)

                adapter.onAppClickListener = this@GenreAdapter.onAppClickListener
                adapter.listOfMovies = data.items

                rvCovers.adapter = adapter
            }
        }
    }
}