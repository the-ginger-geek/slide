package app.messenger.slide.ui
import androidx.fragment.app.Fragment

interface ActivityCallback {
    fun onFragmentChange(fragment: Fragment)
    fun setTitle(text: String)
}