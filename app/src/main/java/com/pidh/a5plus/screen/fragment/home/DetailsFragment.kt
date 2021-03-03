package com.pidh.a5plus.screen.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentDetailsBinding
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.other.enum.MediaType
import com.pidh.a5plus.screen.SeriesActivity
import com.pidh.a5plus.screen.TrailerActivity
import com.pidh.a5plus.screen.adapter.CastingAdapter
import com.pidh.a5plus.screen.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailsFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var glide: RequestManager
    private lateinit var mAdapter: CastingAdapter
    private val mViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = CastingAdapter(glide)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        mViewModel.selectMovie.observe(viewLifecycleOwner) { movie ->
            binding.apply {
                (activity as AppCompatActivity).apply {
                    setSupportActionBar(toolbar)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }

                bottomNavDetails.setOnNavigationItemSelectedListener(this@DetailsFragment)

                if (movie.mediaType == MediaType.MOVIE.code)
                    bottomNavDetails.menu.findItem(R.id.action_go_seasons).isVisible = false

                glide.load(Constants.BASE_URL_POSTER + movie.posterPath).into(imgCover)

                val textScore = (movie.voteAverage * 10).roundToInt().toString() + "%"
                txtScore.text = textScore
                txtScore.setBackgroundResource(backgroundScore(movie.voteAverage))

                txtTitle.text = movie.title
                txtOverview.text = movie.overview
                rvCasting.adapter = mAdapter
            }

            mViewModel.castingDetail(movie.mediaType, movie.id)
        }

        mViewModel.listCasting.observe(viewLifecycleOwner) {
            mAdapter.listOfCasting = it
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_go_home -> {
                findNavController().navigate(R.id.action_detailsFragment_to_homeFragment)
                true
            }
            R.id.action_go_trailer -> {
                val intent = Intent(activity, TrailerActivity::class.java)
                intent.putExtra(Constants.MOVIE_PUT_EXTRA, mViewModel.selectMovie.value)
                startActivity(intent)
                true
            }
            R.id.action_go_seasons -> {
                val intent = Intent(activity, SeriesActivity::class.java)
                intent.putExtra(Constants.MOVIE_PUT_EXTRA, mViewModel.selectMovie.value)
                startActivity(intent)
                true
            }
            R.id.action_share -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, "Hey, olha o que eu estava vendo no A5+ ${mViewModel.selectMovie.value?.title} " +
                        "\nhttps://a5plus.page.link/video")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Compartilhar com:"))
                true
            }
            else -> false
        }
    }

    private fun backgroundScore(voteAverage: Float): Int {
        return when (voteAverage) {
            in 0.0..5.0 -> R.drawable.bck_movie_score_3
            in 5.0..7.0 -> R.drawable.bck_movie_score_2
            else -> R.drawable.bck_movie_score_1
        }
    }
}