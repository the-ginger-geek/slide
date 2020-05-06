package app.messenger.slide.ui.core

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import app.messenger.slide.R

abstract class BaseViewModel : ViewModel() {
    fun navigate(context: Context, id: Int) {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
            .navigate(id)
    }
}