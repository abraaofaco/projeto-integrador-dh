package com.pidh.a5plus.screen

import android.content.Intent
import androidx.activity.viewModels
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.ActivityHomeBinding
import com.pidh.a5plus.provider.IAuthProvider
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.provider.api.model.GenreItemsModel
import com.pidh.a5plus.screen.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var appAuth: IAuthProvider
    lateinit var mBinding: ActivityHomeBinding
    private var mIsAllFabVisible: Boolean = false
    private var mNMovieId: String? = null
    private var mNMediaType: String? = null
    private val mViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityHomeBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        mBinding.apply {
            fabPrimary.setOnClickListener {
                if (mIsAllFabVisible) hideFabOptions() else showFabOptions()
            }

            fabLocation.setOnClickListener {
                startActivity(Intent(this@HomeActivity, PlaceActivity::class.java))
            }

            fabSearch.setOnClickListener {
                findNavController(this@HomeActivity, R.id.home_nav_host_fragment)
                    .navigate(R.id.action_homeFragment_to_searchFragment)
            }

            fabClover.setOnClickListener {
                luck()
            }
        }

        //Push notification
        intent?.let {
            mNMovieId = it.getStringExtra("id")
            mNMediaType = it.getStringExtra("media_type")
        }
    }

    override fun onResume() {
        super.onResume()

        if (!mNMovieId.isNullOrBlank() && !mNMediaType.isNullOrBlank()) {
            mViewModel.findAndSelect(mNMediaType!!, mNMovieId!!.toLong())

            mBinding.fabPrimary.hide()
            redirectDetails()

            mNMediaType = null
            mNMovieId = null
        }
    }

    private fun luck() {
        val tvSeries = mViewModel.listPopularTvSeries.value?.size ?: 0
        val op = if (tvSeries == 0) 1 else 2

        val movie = when ((1..op).random()) {
            1 -> randomMovie(mViewModel.listPopularMovies.value)
            2 -> randomMovie(mViewModel.listPopularTvSeries.value)
            else -> null
        }

        if (movie == null) {
            Toast.makeText(
                this,
                "Ooops! Sem dados disponíveis, tente novamente em instantes",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            mViewModel.selectMovie.value = movie
            redirectDetails()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_settings -> {
                findNavController(
                    this,
                    R.id.home_nav_host_fragment
                ).navigate(R.id.action_homeFragment_to_settingsFragment)
            }
            R.id.action_sign_out -> signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    fun hideFabOptions() {
        mBinding.apply {
            fabClover.hide()
            fabLocation.hide()
            fabSearch.hide()

            mIsAllFabVisible = false
        }
    }

    private fun showFabOptions() {
        mBinding.apply {
            fabSearch.show()
            fabLocation.show()
            fabClover.show()

            mIsAllFabVisible = true
        }
    }

    private fun randomMovie(data: List<GenreItemsModel>?): Movie? {
        if (data == null) return null

        val posGenre = (data.indices).random()
        val genre = data.elementAt(posGenre)
        val posMovie = (genre.items.indices).random()

        return genre.items.elementAt(posMovie)
    }

    private fun redirectDetails() {
        findNavController(
            this,
            R.id.home_nav_host_fragment
        ).navigate(R.id.action_homeFragment_to_detailsFragment)
    }

    private fun signOut() {
        appAuth.signOut()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}