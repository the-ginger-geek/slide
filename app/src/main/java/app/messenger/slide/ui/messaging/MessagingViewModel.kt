package app.messenger.slide.ui.messaging

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.application.MainApplication
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.infrastructure.Repository
import app.messenger.slide.ui.core.BaseViewModel
import javax.inject.Inject

class MessagingViewModel : BaseViewModel() {

    var repository: Repository? = null
        @Inject set

    private var userEmail: String = ""
    val messages: MutableLiveData<List<Entity>> = MutableLiveData<List<Entity>>()
    val input: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            it.value = ""
        }
    }
    val enabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            it.value = true
        }
    }

    fun onClickSend(view: View) {
        enabled.value = false
        repository?.addNewMessage(input.value?:"", userEmail) { result ->
            if (result.isSuccessful()) populateMessages()
            enabled.value = true
            input.value = ""
        }
    }

    fun onClickAttach(view: View) {

    }

    fun init(context: Context, userEmail: String) {
        (context.applicationContext as MainApplication).applicationComponent?.inject(this)
        this.userEmail = userEmail
        populateMessages()
    }

    private fun populateMessages() {
        repository?.getMessagesForUser(userEmail) { result ->
            if (result.isSuccessful()) messages.value = result.value?.toList() ?: listOf()
        }
    }
}