package app.messenger.slide.ui.main

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.R
import app.messenger.slide.application.MainApplication
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.infrastructure.repository.Repository
import app.messenger.slide.ui.core.BaseViewModel
import javax.inject.Inject

class MainViewModel : BaseViewModel() {

    val conversations: LiveData<List<Entity>> by lazy {
        MutableLiveData<List<Entity>>().also { liveData ->
            repository?.getRunningConversations { result ->
                if (result.isSuccessful()) {
                    liveData.value = result.value?.toList() ?: mutableListOf()
                }
            }
        }
    }

    fun init(context: Context) {
        inject(context)
    }

    fun onClick(view: View) {
        navigate(view.context, R.id.action_mainFragment_to_usersFragment)
    }
}
