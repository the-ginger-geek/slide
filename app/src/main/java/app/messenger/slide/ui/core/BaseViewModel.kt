package app.messenger.slide.ui.core

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import app.messenger.slide.R
import app.messenger.slide.application.MainApplication
import app.messenger.slide.infrastructure.repository.Repository
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    var repository: Repository? = null
        @Inject set

    fun navigate(context: Context, id: Int) {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
            .navigate(id)
    }

    fun inject(context: Context) {
        (context.applicationContext as MainApplication).applicationComponent?.inject(this)
    }
}