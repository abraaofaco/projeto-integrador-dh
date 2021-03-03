package com.pidh.a5plus.screen.fragment.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.pidh.a5plus.R
import com.pidh.a5plus.databinding.FragmentSignUpBinding
import com.pidh.a5plus.provider.IAuthProvider
import com.pidh.a5plus.screen.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    @Inject
    lateinit var appAuth: IAuthProvider
    private lateinit var mBinding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentSignUpBinding.bind(view)

        mBinding.apply {
            txtPasswordConfirmation.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                val re = actionId == EditorInfo.IME_ACTION_DONE
                if (re) createAccount()
                return@OnEditorActionListener re
            })

            btnSignInUp.setOnClickListener {
               createAccount()
            }
        }
    }

    private fun createAccount() {
        val email = mBinding.txtEmail.text.toString()
        val password = mBinding.txtPassword.text.toString()
        val passwordConfirmation = mBinding.txtPasswordConfirmation.text.toString()

        when {
            email.isNullOrBlank() -> {
                Toast.makeText(context, "Informe o email", Toast.LENGTH_LONG).show()
                return
            }
            password.isNullOrBlank() -> {
                Toast.makeText(context, "Informe a senha", Toast.LENGTH_LONG).show()
                return
            }
            password != passwordConfirmation -> {
                Toast.makeText(context, "As senhas não conferem", Toast.LENGTH_LONG).show()
                return
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val activity = this@SignUpFragment.activity as AuthActivity

            try {
                appAuth.signUp(email, password)
                activity.navigateUpToHome()
            } catch (e: Exception) {
                Log.i("SignUp@createAccount", e.message, e)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}