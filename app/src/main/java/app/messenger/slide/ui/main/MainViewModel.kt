package app.messenger.slide.ui.main

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.R
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.ui.core.BaseViewModel

class MainViewModel : BaseViewModel() {

    val hasConversations: MutableLiveData<Boolean> = MutableLiveData()
    val conversations: LiveData<List<Entity>> by lazy {
        MutableLiveData<List<Entity>>().also { liveData ->
            repository?.getRunningConversations { result ->
                if (result.isSuccessful()) {
                    liveData.value = result.value?.toList() ?: mutableListOf()
                }

                hasConversations.value = liveData.value?.isNotEmpty()
            }
        }
    }

    override fun init(context: Context) {
        initializeBaseViewModel(context)
    }

    fun onClick(view: View) {
        navigate(view.context, R.id.action_mainFragment_to_usersFragment)
    }
}
