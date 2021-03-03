package com.pidh.a5plus.screen.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.switchmaterial.SwitchMaterial
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentSettingsBinding
import com.pidh.a5plus.other.Constants
import com.pidh.a5plus.helper.Util.AlertDialog
import com.pidh.a5plus.provider.IAuthProvider
import com.pidh.a5plus.provider.database.local.entities.Genre
import com.pidh.a5plus.screen.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var appAuth: IAuthProvider

    private val mViewModel: HomeViewModel by activityViewModels()
    private lateinit var mBinding: FragmentSettingsBinding
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!

        //Facebook
        mCallbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance()
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    linkFacebook(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d("SignIn@onCancelFacebook", "onCancel")
                }

                override fun onError(e: FacebookException) {
                    Log.d("Setting@onErrorFacebook", e.message, e)
                    Toast.makeText(activity, e.message.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentSettingsBinding.bind(view)

        mBinding.apply {
            (activity as AppCompatActivity).apply {
                toolbar.title = getString(R.string.title_settings)
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            btnLinkGoogle.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, Constants.SIGN_IN_GOOGLE)
            }

            btnLinkFacebook.setOnClickListener {
                LoginManager.getInstance().logInWithReadPermissions(
                    this@SettingsFragment, listOf(
                        "public_profile",
                        "email"
                    )
                )
            }
        }

        createSettingsOptions(getString(R.string.title_movie), mViewModel.genreListMovie)
        createSettingsOptions(getString(R.string.title_tv_series), mViewModel.genreListTvSeries)
    }

    private fun linkGoogle(data: Intent?) {
        lifecycleScope.launch {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val exception = task.exception

                if (task.isSuccessful) {
                    val account = task.getResult(ApiException::class.java)!!
                    appAuth.linkGoogle(account.idToken!!)

                    AlertDialog(activity, resources.getString(R.string.success))
                } else {
                    throw Exception(exception?.message.toString())
                }
            } catch (e: Exception) {
                Log.d("Settings@linkGoogle", e.message, e)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun linkFacebook(accessToken: AccessToken) {
        lifecycleScope.launch {
            try {
                appAuth.linkFacebook(accessToken.token)

                AlertDialog(activity, resources.getString(R.string.success))
            } catch (e: Exception) {
                Log.d("Settings@linkFacebook", e.message, e)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createSettingsOptions(title: String, genres: List<Genre>) {
        val textView = TextView(context).apply {
            text = title
            setTextAppearance(R.style.Theme_A5Plus_FontTitleH3)
            setPadding(0, 0, 0, 30)
        }

        mBinding.settingsContainer.addView(textView)

        val line = View(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )
            setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
        }

        mBinding.settingsContainer.addView(line)

        genres.forEach { genre ->
            val switch = context?.let { context ->
                SwitchMaterial(context).apply {
                    text = genre.name
                    setTextColor(ContextCompat.getColor(context, R.color.primary_light))
                    isChecked = genre.enable

                    setOnClickListener {
                        genre.enable = isChecked
                        mViewModel.updateGenre(genre)
                    }
                }
            }

            mBinding.settingsContainer.addView(switch)
        }

        val space = Space(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50
            )
        }

        mBinding.settingsContainer.addView(space)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.SIGN_IN_GOOGLE) {
            linkGoogle(data)
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
}