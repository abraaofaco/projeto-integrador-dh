package com.pidh.a5plus.screen.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentTabItemBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.other.enum.Tabs
import com.pidh.a5plus.provider.api.model.GenreItemsModel
import com.pidh.a5plus.screen.adapter.GenreAdapter
import com.pidh.a5plus.screen.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TabItemFragment(
    private val tab: Tabs,
    private val fragment: Fragment
) : Fragment() {

    @Inject
    lateinit var mAdapter: GenreAdapter

    private lateinit var mBinding: FragmentTabItemBinding
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (tab) {
            Tabs.MOVIE -> mViewModel.popularMovies()
            Tabs.ON_TV -> mViewModel.popularTvSeries()
            Tabs.IN_THEATERS -> mViewModel.nowPlayingInTheaters()
            Tabs.UPCOMING_RELEASES -> mViewModel.upcomingReleases()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentTabItemBinding.bind(view)
        mAdapter.onAppClickListener = fragment as OnAppClickListener

        mBinding.apply {
            rvGenres.adapter = mAdapter
        }

        when (tab) {
            Tabs.MOVIE -> {
                mViewModel.listPopularMovies.observe(viewLifecycleOwner) {
                    showData(it)
                }
            }
            Tabs.ON_TV -> {
                mViewModel.listPopularTvSeries.observe(viewLifecycleOwner) {
                    showData(it)
                }
            }
            Tabs.IN_THEATERS -> {
                mViewModel.listInTheaters.observe(viewLifecycleOwner) {
                    showData(it)
                }
            }
            Tabs.UPCOMING_RELEASES -> {
                mViewModel.listUpcomingReleases.observe(viewLifecycleOwner) {
                    showData(it)
                }
            }
        }
    }

    private fun showData(data: List<GenreItemsModel>) {
        mBinding.shimmer.visibility = View.GONE
        mAdapter.genreList = data
    }
}