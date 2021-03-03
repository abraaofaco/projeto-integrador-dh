package com.pidh.a5plus.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.ActivitySeriesBinding
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.screen.viewmodels.SeriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeriesActivity : AppCompatActivity() {

    private val mViewModel: SeriesViewModel by viewModels()
    lateinit var mBinding: ActivitySeriesBinding
    lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySeriesBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        movie = intent.getSerializableExtra(Constants.MOVIE_PUT_EXTRA) as Movie
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}