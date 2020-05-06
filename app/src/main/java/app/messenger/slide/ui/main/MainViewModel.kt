package app.messenger.slide.ui.main

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.R
import app.messenger.slide.application.MainApplication
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.infrastructure.Repository
import app.messenger.slide.ui.core.BaseViewModel
import javax.inject.Inject

class MainViewModel : BaseViewModel() {

    var repository: Repository? = null
        @Inject set

    val hasConversations: MutableLiveData<Boolean> = MutableLiveData()
    val conversations: LiveData<List<Entity>> by lazy {
        MutableLiveData<List<Entity>>().also { liveData ->
            repository?.getRunningConversations { result ->
                Log.d("xxxxx", "success? ${result.isSuccessful()}")
                if (result.isSuccessful()) {
                    liveData.value = result.value?.toList() ?: mutableListOf()
                }

                hasConversations.value = liveData.value?.isNotEmpty()
            }
        }
    }

    fun init(context: Context) {
        (context.applicationContext as MainApplication).applicationComponent?.inject(this)
    }

    fun onClick(view: View) {
        navigate(view.context, R.id.action_mainFragment_to_usersFragment)
    }
}
