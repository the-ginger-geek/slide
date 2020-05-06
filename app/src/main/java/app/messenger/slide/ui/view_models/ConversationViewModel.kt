package app.messenger.slide.ui.view_models

import android.view.View
import androidx.databinding.Bindable
import app.messenger.slide.domain.entities.Conversation
import java.text.SimpleDateFormat
import java.util.*

class ConversationViewModel(
    @Bindable val heading: String,
    @Bindable val body: String,
    @Bindable val time: String
) : Viewable() {

    fun onClick(view: View) {

    }

    companion object {
        private var simpleDateFormat: SimpleDateFormat? = null
        fun parse(data: Conversation): ConversationViewModel {
            if (simpleDateFormat == null) {
                simpleDateFormat = SimpleDateFormat("dd HH:mm", Locale.getDefault())
            }
            val time = simpleDateFormat?.format(Date(data.timestamp)) ?: ""
            return ConversationViewModel(data.userName ?: "", data.body ?: "", time)
        }
    }
}