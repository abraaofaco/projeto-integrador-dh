package com.pidh.a5plus.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.pidh.a5plus.databinding.ActivityTrailerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailerActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityTrailerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityTrailerBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}