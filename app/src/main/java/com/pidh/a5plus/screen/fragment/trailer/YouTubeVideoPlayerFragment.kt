package com.pidh.a5plus.screen.fragment.trailer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import com.pidh.a5plus.R
import com.pidh.a5plus.screen.viewmodels.TrailerViewModel

class YouTubeVideoPlayerFragment : Fragment(), YouTubePlayer.OnInitializedListener {

    private val mViewModel: TrailerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_youtube_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val youtubeFragment =
            childFragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerSupportFragmentX?

        youtubeFragment?.initialize(
            getString(R.string.google_api_key),
            this@YouTubeVideoPlayerFragment
        )
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        mViewModel.selectTrailer.observe(viewLifecycleOwner) { video ->
            p1?.cueVideo(video.key)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(
            context,
            R.string.youtube_failure,
            Toast.LENGTH_LONG
        ).show()
    }
}