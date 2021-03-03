package com.pidh.a5plus.screen.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentSignInBinding
import com.pidh.a5plus.other.Constants.SIGN_IN_GOOGLE
import com.pidh.a5plus.helper.Util
import com.pidh.a5plus.provider.IAuthProvider
import com.pidh.a5plus.screen.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    @Inject
    lateinit var appAuth: IAuthProvider

    private lateinit var mBinding: FragmentSignInBinding
    private lateinit var mActivity: AuthActivity
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var mLoginManager: LoginManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = activity as AuthActivity

        //Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!

        //Facebook
        mCallbackManager = CallbackManager.Factory.create()
        mLoginManager = LoginManager.getInstance()

        mLoginManager.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                signInFacebook(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("SignIn@onCancelFacebook", "onCancel")
            }

            override fun onError(e: FacebookException) {
                Log.d("SignIn@onErrorFacebook", e.message, e)
                Toast.makeText(activity, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentSignInBinding.bind(view)

        mBinding.apply {
            txtPassword.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                val re = actionId == EditorInfo.IME_ACTION_DONE
                if (re) sigInEmailPassword()

                return@OnEditorActionListener re
            })

            btnSignInEmailPassword.setOnClickListener {
                sigInEmailPassword()
            }

            btnSignInGoogle.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, SIGN_IN_GOOGLE)
            }

            btnSignInFacebook.setOnClickListener {
                mLoginManager.logInWithReadPermissions(
                    this@SignInFragment, listOf(
                        "public_profile",
                        "email"
                    )
                )
            }

            btnSignInUp.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            btnForgotPassword.setOnClickListener {
                forgotPassword()
            }
        }
    }

    private fun sigInEmailPassword() {
        val email = mBinding.txtEmail.text.toString()
        val password = mBinding.txtPassword.text.toString()

        when {
            email.isNullOrBlank() -> {
                Toast.makeText(context, "Informe o email", Toast.LENGTH_LONG).show()
                return
            }
            password.isNullOrBlank() -> {
                Toast.makeText(context, "Informe a senha", Toast.LENGTH_LONG).show()
                return
            }
        }

        lifecycleScope.launch {
            try {
                appAuth.signIn(email, password)
                mActivity.navigateUpToHome()
            } catch (e: Exception) {
                Log.i("SignIn@emailPassword", e.message, e)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInGoogle(data: Intent?) {
        lifecycleScope.launch {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val exception = task.exception

                if (task.isSuccessful) {
                    val account = task.getResult(ApiException::class.java)!!
                    appAuth.signInGoogle(account.idToken!!)
                    mActivity.navigateUpToHome()
                } else {
                    throw Exception(exception?.message.toString())
                }
            } catch (e: Exception) {
                Log.d("SignIn@signInGoogle", e.message, e)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInFacebook(accessToken: AccessToken) {
        lifecycleScope.launch {
            try {
                appAuth.signInFacebook(accessToken.token)
                mActivity.navigateUpToHome()
            } catch (e: Exception) {
                Log.d("SignIn@signInFacebook", e.message, e)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun forgotPassword() {
        val email = mBinding.txtEmail.text.toString()

        lifecycleScope.launch {
            try {
                appAuth.forgotPassword(email)
                mBinding.txtEmail.setText("")
                Util.AlertDialog(context, getString(R.string.msg_forgot_password))
            } catch (e: Exception) {
                Log.i("SignIn@forgotPassword", e.message, e)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_GOOGLE) {
            signInGoogle(data)
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
}