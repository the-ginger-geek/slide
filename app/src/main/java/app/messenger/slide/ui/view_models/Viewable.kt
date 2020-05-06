package app.messenger.slide.ui.view_models

import android.app.Activity
import android.content.Context
import androidx.databinding.BaseObservable
import androidx.navigation.Navigation
import app.messenger.slide.R

abstract class Viewable : BaseObservable() {

    fun navigate(context: Context, id: Int) {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
            .navigate(id)
    }
}