package app.messenger.slide.ui.conversation

import android.content.Context
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.ui.core.BaseViewModel

class ConversationViewModel : BaseViewModel() {

    val messages: MutableLiveData<List<Entity>> = MutableLiveData<List<Entity>>()

    fun init(context: Context, userEmail: String) {
        initializeBaseViewModel(context)
        repository?.getMessagesForUser(userEmail) { result ->
            if (result.isSuccessful()) messages.value = result.value
        }
    }
}