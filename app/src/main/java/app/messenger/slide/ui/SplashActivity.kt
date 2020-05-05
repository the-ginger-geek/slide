package app.messenger.slide.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.messenger.slide.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.splash_activity.*

class SplashActivity : AppCompatActivity() {
    private val rcSignIn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        checkSignedIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == rcSignIn) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                checkSignedIn()
            } else {
                val error = response?.error?.localizedMessage ?: "Failed to sign in"
                Snackbar.make(base_layout, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun checkSignedIn() {
        val authUIInstance = AuthUI.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            sign_in_card.visibility = View.GONE
            goToMainActivity()
        } else {
            sign_in_card.visibility = View.VISIBLE
            sign_in_card.setOnClickListener { goToSignInScreen(authUIInstance) }
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToSignInScreen(authUIInstance: AuthUI) {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
            authUIInstance
                .createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher_round)
                .setAvailableProviders(providers)
                .build(),
            rcSignIn
        )
    }
}