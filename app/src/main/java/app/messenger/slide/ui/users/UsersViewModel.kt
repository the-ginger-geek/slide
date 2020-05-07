package app.messenger.slide.ui.users

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.application.MainApplication
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.infrastructure.Repository
import app.messenger.slide.ui.core.BaseViewModel
import javax.inject.Inject

class UsersViewModel : BaseViewModel() {

    var repository: Repository? = null
        @Inject set

    val users: LiveData<List<Entity>> by lazy {
        MutableLiveData<List<Entity>>().also { liveData ->
            repository?.getAllUsers { result ->
                if (result.isSuccessful()) liveData.value = result.value
            }
        }
    }

    fun init(context: Context) {
        (context.applicationContext as MainApplication).applicationComponent?.inject(this)
    }
}