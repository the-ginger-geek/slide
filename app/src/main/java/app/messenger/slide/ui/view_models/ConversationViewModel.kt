package app.messenger.slide.ui.view_models

import android.os.Bundle
import android.view.View
import androidx.databinding.Bindable
import app.messenger.slide.R
import app.messenger.slide.domain.entities.Conversation
import java.text.SimpleDateFormat
import java.util.*

class ConversationViewModel(
    @Bindable val heading: String,
    @Bindable val body: String,
    @Bindable val time: String,
    private val email: String
) : Viewable() {

    fun onClick(view: View) {
        val bundle = Bundle()
        bundle.putString("user_email", email)
        navigate(view.context, R.id.action_mainFragment_to_conversationFragment, bundle)
    }

    companion object {
        private var simpleDateFormat: SimpleDateFormat? = null
        fun parse(data: Conversation): ConversationViewModel {
            if (simpleDateFormat == null) {
                simpleDateFormat = SimpleDateFormat("EEEE HH:mm", Locale.getDefault())
            }
            val time = simpleDateFormat?.format(Date(data.timestamp)) ?: ""
            return ConversationViewModel(data.userEmail ?: "", data.body ?: "", time, data.userEmail ?: "")
        }
    }
}