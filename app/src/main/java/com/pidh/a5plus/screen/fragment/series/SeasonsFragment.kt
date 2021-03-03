package com.pidh.a5plus.screen.fragment.series

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
import com.pidh.a5plus.databinding.FragmentSeasonsBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.provider.api.entities.SeriesInfo
import com.pidh.a5plus.screen.SeriesActivity
import com.pidh.a5plus.screen.adapter.SeriesInfoAdapter
import com.pidh.a5plus.screen.viewmodels.SeriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SeasonsFragment : Fragment(), OnAppClickListener {

    @Inject
    lateinit var glide: RequestManager
    private lateinit var mBinding: FragmentSeasonsBinding
    private lateinit var mMovie: Movie
    private val mViewModel: SeriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seasons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMovie = (activity as SeriesActivity).movie

        mViewModel.seasons(mMovie.id)

        mBinding = FragmentSeasonsBinding.bind(view)

        configToolbar()

        mViewModel.listSeason.observe(viewLifecycleOwner) { data ->
            mBinding.rvSeasons.adapter = SeriesInfoAdapter(glide, data, this)
        }
    }

    override fun onItemClick(season: SeriesInfo) {
        mViewModel.episodes(mMovie.id, season.seasonNumber)
        findNavController().navigate(R.id.action_seasonsFragment_to_episodesFragment)
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