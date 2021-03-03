package com.pidh.a5plus.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pidh.a5plus.databinding.ActivityAuthBinding
import com.pidh.a5plus.provider.IAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var appAuth: IAuthProvider

    private var mNMovieId: String? = null
    private var mNMediaType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityAuthBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        //Push notification
        intent?.let {
            mNMovieId = it.getStringExtra("id")
            mNMediaType = it.getStringExtra("media_type")

            if(!mNMovieId.isNullOrBlank() && appAuth.currentUser() != null)
                navigateUpToHome()
        }
    }

    fun navigateUpToHome() {
        val intent = Intent(this, HomeActivity::class.java)

        intent.putExtra("id", mNMovieId)
        intent.putExtra("media_type", mNMediaType)

        startActivity(intent)
        finish()
    }
}