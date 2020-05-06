package app.messenger.slide.ui.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.ui.core.BaseViewModel

class UsersViewModel : BaseViewModel() {

    val users: LiveData<List<Entity>> by lazy {
        MutableLiveData<List<Entity>>().also { liveData ->
            repository?.getAllUsers { result ->
                if (result.isSuccessful()) liveData.value = result.value
            }
        }
    }

    override fun init(context: Context) {
        initializeBaseViewModel(context)
    }
}