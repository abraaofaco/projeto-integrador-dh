package com.pidh.a5plus.screen.fragment.series

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import com.google.android.material.appbar.AppBarLayout
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentEpisodesBinding
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.screen.SeriesActivity
import com.pidh.a5plus.screen.adapter.SeriesInfoAdapter
import com.pidh.a5plus.screen.viewmodels.SeriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EpisodesFragment : Fragment() {

    @Inject
    lateinit var glide: RequestManager
    private lateinit var mBinding: FragmentEpisodesBinding
    private val mViewModel: SeriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentEpisodesBinding.bind(view)

        configToolbar()

        mViewModel.episodesResponse.observe(viewLifecycleOwner) { data ->
            val listEpisodes = data.listEpisodes.sortedBy { it.episodeNumber }

            mBinding.apply {
                glide.load(Constants.BASE_URL_POSTER + data.posterPath)
                    .into(icnToolbar.imgBackground)

                txtTitle.text = data.name
                txtOverview.text = data.overview
                rvEpisodes.adapter = SeriesInfoAdapter(glide, listEpisodes)
            }
        }
    }

    private fun configToolbar() {
        mBinding.apply {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(icnToolbar.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            icnToolbar.imgBackground.scaleType = ImageView.ScaleType.MATRIX
            icnToolbar.collapsingToolbar.title = (activity as SeriesActivity).movie.title

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