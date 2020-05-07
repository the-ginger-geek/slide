package app.messenger.slide.ui.messaging

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.ui.core.BaseViewModel
import com.google.android.material.snackbar.Snackbar

class MessagingViewModel : BaseViewModel() {

    private var userEmail: String = ""
    val messages: MutableLiveData<List<Entity>> = MutableLiveData<List<Entity>>()
    val input: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            it.value = ""
        }
    }
    val enabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            it.value = false
        }
    }

    fun init(context: Context, userEmail: String) {
        this.userEmail = userEmail
        inject(context)
        populateMessages()
    }

    fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        enabled.value = s.isNotEmpty()
    }

    fun onClickSend(view: View) {
        enabled.value = false
        val text = input.value?:""
        repository?.addNewMessage(text, userEmail) { result ->
            if (result.isSuccessful()) {
                populateMessages()
            } else {
            }
            Snackbar.make(view, "Failed to send {$text}", Snackbar.LENGTH_SHORT).show()
            enabled.value = true
        }
        input.value = ""
    }

    fun onClickAttach(view: View) {

    }

    private fun populateMessages() {
        repository?.getMessagesForUser(userEmail) { result ->
            if (result.isSuccessful()) messages.value = result.value?.toList() ?: listOf()
        }
    }
}