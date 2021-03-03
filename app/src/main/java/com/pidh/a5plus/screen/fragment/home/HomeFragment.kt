package com.pidh.a5plus.screen.fragment.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentHomeBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.other.enum.Tabs
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.screen.HomeActivity
import com.pidh.a5plus.screen.adapter.TabsAdapter
import com.pidh.a5plus.screen.viewmodels.HomeViewModel

class HomeFragment : Fragment(), OnAppClickListener {

    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mActivity: HomeActivity
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentHomeBinding.bind(view)

        mBinding.apply {

            mActivity.setSupportActionBar(toolbar)

            fragViewPager.adapter = TabsAdapter(this@HomeFragment)
            fragViewPager.isUserInputEnabled = false

            TabLayoutMediator(tabLayout, fragViewPager) { tab, position ->
                tab.text = Tabs.values()[position].title
            }.attach()
        }
    }

    override fun onItemClick(movie: Movie) {
        mViewModel.selectMovie.value = movie
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onStart() {
        super.onStart()

        mActivity.mBinding.fabPrimary.show()
        mViewModel.reload()
    }

    override fun onPause() {
        super.onPause()
        mActivity.hideFabOptions()
        mActivity.mBinding.fabPrimary.hide()
    }
}