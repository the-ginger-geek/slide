package app.messenger.slide.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.messenger.slide.R
import app.messenger.slide.application.MainApplication
import app.messenger.slide.infrastructure.Repository
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.splash_activity.*
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    var repository: Repository? = null
        @Inject set

    private val rcSignIn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        if(FirebaseAuth.getInstance().currentUser == null) {
            startSignInFlow()
        } else {
            goToMainActivity()
        }
        (application as MainApplication).applicationComponent?.inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == rcSignIn) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    sign_in_card.visibility = View.GONE
                    repository?.addNewUser(user)
                    goToMainActivity()
                }
            } else {
                val error = response?.error?.localizedMessage ?: "Failed to sign in"
                Snackbar.make(base_layout, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun startSignInFlow() {
        val authUIInstance = AuthUI.getInstance()
        sign_in_card.visibility = View.VISIBLE
        sign_in_card.setOnClickListener { goToSignInScreen(authUIInstance) }
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