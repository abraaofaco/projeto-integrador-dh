package com.pidh.a5plus.screen.fragment.auth

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentSplashScreenBinding
import com.pidh.a5plus.provider.IAuthProvider
import com.pidh.a5plus.screen.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    @Inject
    lateinit var appAuth: IAuthProvider
    private lateinit var mySong: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAnim = AnimationUtils.loadAnimation(activity, R.anim.top_splash_animation)
        val bottomAnim = AnimationUtils.loadAnimation(activity, R.anim.bottom_splash_animation)

        val binding = FragmentSplashScreenBinding.bind(view)

        mySong = MediaPlayer.create(activity, R.raw.app_soung);
        mySong.start();

        binding.apply {
            imgLogo.animation = topAnim
            txtAppName.animation = bottomAnim
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            delay(3000)

            val activity = this@SplashScreenFragment.activity as AuthActivity

            if (appAuth.currentUser() != null) {
                activity.navigateUpToHome()
            } else {
                findNavController().navigate(R.id.action_splashScreenFragment_to_signInFragment)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mySong.release()
    }
}