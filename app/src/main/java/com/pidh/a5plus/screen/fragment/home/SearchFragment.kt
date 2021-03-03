package com.pidh.a5plus.screen.fragment.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentSearchBinding
import com.pidh.a5plus.helper.events.OnAppClickListener
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.screen.adapter.CoverAdapter
import com.pidh.a5plus.screen.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), OnAppClickListener, SearchView.OnQueryTextListener {

    @Inject
    lateinit var mAdapter: CoverAdapter

    private lateinit var mBinding: FragmentSearchBinding
    private var mQuery: String = ""
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentSearchBinding.bind(view)

        mBinding.apply {
            (activity as AppCompatActivity).apply {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            svToolbar.apply {
                setOnQueryTextListener(this@SearchFragment)
                requestFocus()
            }

            mAdapter.onAppClickListener = this@SearchFragment
            rvSearchResults.adapter = mAdapter
        }

        mViewModel.listOfSearchResult.observe(viewLifecycleOwner) {
            mAdapter.listOfMovies = it
        }
    }

    override fun onItemClick(movie: Movie) {
        mViewModel.selectMovie.value = movie
        findNavController().navigate(R.id.action_searchFragment_to_detailsFragment)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText == null || newText == mQuery)
            return false

        mQuery = newText

        //debounce :)
        lifecycleScope.launch {
            delay(500)
            if (newText == mQuery) mViewModel.searchMovies(newText)
        }

        return true
    }
}