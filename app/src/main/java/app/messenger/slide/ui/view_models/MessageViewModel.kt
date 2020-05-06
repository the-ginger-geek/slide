package app.messenger.slide.ui.view_models

import androidx.databinding.Bindable
import app.messenger.slide.domain.entities.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageViewModel(
    @Bindable val message: String,
    val fromUser: String,
    val toUser: String,
    val time: String
) :
    Viewable() {
    companion object {
        private var simpleDateFormat: SimpleDateFormat? = null
        fun parse(data: Message): MessageViewModel {
            if (simpleDateFormat == null) {
                simpleDateFormat = SimpleDateFormat("MM:dd HH:mm", Locale.getDefault())
            }
            return MessageViewModel(
                data.message,
                data.fromUserEmail,
                data.toUserEmail,
                simpleDateFormat?.format(Date(data.timestamp)) ?: ""
            )
        }
    }
}