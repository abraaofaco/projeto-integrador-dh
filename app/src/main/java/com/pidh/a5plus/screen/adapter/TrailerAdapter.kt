package com.pidh.a5plus.screen.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailLoader.OnThumbnailLoadedListener
import com.google.android.youtube.player.YouTubeThumbnailView
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.ItemTrailerBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.provider.api.entities.Video

class TrailerAdapter(
    private val context: Context,
    private val onAppClickListener: OnAppClickListener
) : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    var listVideos: List<Video> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val viewBinding: ItemTrailerBinding) :
        RecyclerView.ViewHolder(viewBinding.root),  View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(video: Video) {
            viewBinding.youtubeThumbnail.initialize(
                context.getString(R.string.google_api_key),
                object : YouTubeThumbnailView.OnInitializedListener {
                    override fun onInitializationSuccess(
                        youTubeThumbnailView: YouTubeThumbnailView,
                        youTubeThumbnailLoader: YouTubeThumbnailLoader
                    ) {
                        youTubeThumbnailLoader.setVideo(video.key)
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(object :
                            OnThumbnailLoadedListener {
                            override fun onThumbnailLoaded(
                                youTubeThumbnailView: YouTubeThumbnailView,
                                s: String
                            ) {
                                youTubeThumbnailLoader.release()
                            }

                            override fun onThumbnailError(
                                youTubeThumbnailView: YouTubeThumbnailView,
                                errorReason: YouTubeThumbnailLoader.ErrorReason
                            ) {
                                Log.e("TrailerAdp@ThbError", "Youtube Thumbnail Error")
                            }
                        })
                    }

                    override fun onInitializationFailure(
                        youTubeThumbnailView: YouTubeThumbnailView,
                        youTubeInitializationResult: YouTubeInitializationResult
                    ) {
                        Log.e("TrailerAdp@ThbError", "Youtube Initialization Failure")
                    }
                })

            viewBinding.txtName.text = video.name
            viewBinding.txtDuration.text = video.type
        }

        override fun onClick(v: View?) {
            val video = listVideos.elementAt(adapterPosition)
            onAppClickListener.onItemClick(video)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTrailerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listVideos[position])
    }

    override fun getItemCount(): Int = listVideos.count()
}