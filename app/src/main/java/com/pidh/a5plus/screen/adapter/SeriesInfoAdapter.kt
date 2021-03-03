package com.pidh.a5plus.screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pidh.a5plus.databinding.ItemSeriesInfoBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.provider.api.entities.SeriesInfo

class SeriesInfoAdapter(
    private val glide: RequestManager,
    private val listSeriesInfo: List<SeriesInfo>,
    private val onAppClickListener: OnAppClickListener? = null
) : RecyclerView.Adapter<SeriesInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemSeriesInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSeriesInfo[position])
    }

    override fun getItemCount(): Int = listSeriesInfo.size

    inner class ViewHolder(private val viewBinding: ItemSeriesInfoBinding) :
        RecyclerView.ViewHolder(viewBinding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(data: SeriesInfo) {
            val info =
                if (data.episodeCount > 0) "${data.episodeCount} ep | ${data.airDate}" else data.airDate

            viewBinding.apply {
                glide.load(Constants.BASE_URL_POSTER + data.posterPath).into(imgCover)

                txtInfo.text = info
                txtTitle.text = data.name
                txtOverview.text = data.overview
            }
        }

        override fun onClick(v: View?) {
            val info = listSeriesInfo.elementAt(adapterPosition)
            onAppClickListener?.onItemClick(info)
        }
    }
}