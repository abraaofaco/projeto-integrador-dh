package com.pidh.a5plus.screen.fragment.trailer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.appbar.AppBarLayout
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentTrailerListBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.provider.api.entities.Video
import com.pidh.a5plus.screen.adapter.TrailerAdapter
import com.pidh.a5plus.screen.viewmodels.TrailerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrailerListFragment : Fragment(), OnAppClickListener {

    @Inject
    lateinit var glide: RequestManager
    private lateinit var mBinding: FragmentTrailerListBinding
    private lateinit var mAdapter: TrailerAdapter
    private lateinit var mMovie: Movie
    private val mViewModel:TrailerViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMovie = activity?.intent?.getSerializableExtra(Constants.MOVIE_PUT_EXTRA) as Movie

        mViewModel.trailers(mMovie.mediaType, mMovie.id);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trailer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentTrailerListBinding.bind(view)



        context?.let {
            mAdapter = TrailerAdapter(it, this@TrailerListFragment)
            mBinding.rvTrailer.adapter = mAdapter
        }

        configToolbar()

        mViewModel.listTrailer.observe(viewLifecycleOwner) { videos ->
            mAdapter.listVideos = videos
        }
    }

    override fun onItemClick(video: Video) {
        mViewModel.selectTrailer.value = video
        findNavController().navigate(R.id.action_trailerListFragment_to_youTubePlayerFragment)
    }

    private fun configToolbar() {
        mBinding.apply {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(icnToolbar.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            icnToolbar.collapsingToolbar.title = mMovie.title
            glide.load(Constants.BASE_URL_BACKDROP + mMovie.backdropPath)
                .into(icnToolbar.imgBackground)

            icnToolbar.collapsingToolbar.setExpandedTitleTextAppearance(R.style.Theme_A5Plus_ExpandedAppBar);
            icnToolbar.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.Theme_A5Plus_CollapsedAppBar);

            icnToolbar.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                if ((icnToolbar.collapsingToolbar.height + verticalOffset) < (2 * ViewCompat.getMinimumHeight(
                        icnToolbar.collapsingToolbar
                    ))
                ) {
                    icnToolbar.toolbar.setNavigationIcon(R.drawable.ic_left_arrow)
                } else {
                    icnToolbar.toolbar.setNavigationIcon(R.drawable.ic_left_arrow_circle)
                }
            })
        }
    }
}