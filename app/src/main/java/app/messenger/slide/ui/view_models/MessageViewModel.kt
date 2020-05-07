package app.messenger.slide.ui.view_models

import androidx.databinding.Bindable
import app.messenger.slide.R
import app.messenger.slide.domain.entities.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*

class MessageViewModel(
    @Bindable val message: String,
    val fromUser: String,
    val toUser: String,
    @Bindable val time: String,
    @Bindable val horizontalBias: Float,
    @Bindable val boxColor: Int
) :
    Viewable() {
    companion object {
        private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        private var simpleDateFormat: SimpleDateFormat? = null
        fun parse(data: Message): MessageViewModel {
            if (simpleDateFormat == null) {
                simpleDateFormat = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
            }
            val currentUser = data.toUserEmail == currentUser?.email
            return MessageViewModel(
                data.message,
                data.fromUserEmail,
                data.toUserEmail,
                simpleDateFormat?.format(Date(data.timestamp)) ?: "",
                if (currentUser) 1f else 0f,
                if (currentUser) R.color.otherUserMessageBox else R.color.currentUserMessageBox
            )
        }
    }
}