package app.messenger.slide.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import app.messenger.slide.R
import app.messenger.slide.ui.main.MainFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(),
    ActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onFragmentChange(fragment: Fragment) {
        val showNavIcon = fragment !is MainFragment
        supportActionBar?.setDisplayHomeAsUpEnabled(showNavIcon)
        supportActionBar?.setDisplayShowHomeEnabled(showNavIcon)
    }

    override fun setTitle(text: String) {
        toolbar_title.text = text
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp()
}
