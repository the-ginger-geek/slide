package app.messenger.slide.ui.view_models

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.Bindable
import app.messenger.slide.R
import app.messenger.slide.domain.entities.Conversation
import java.text.SimpleDateFormat
import java.util.*

class ConversationViewModel(
    @Bindable val heading: String,
    @Bindable val body: String,
    @Bindable val time: String,
    @Bindable val iconTint: String,
    private val email: String
) : Viewable() {

    fun onClick(view: View) {
        val bundle = Bundle()
        bundle.putString("user_email", email)
        bundle.putString("user_name", heading)
        navigate(view.context, R.id.action_mainFragment_to_conversationFragment, bundle)
    }

    companion object {
        private var simpleDateFormat: SimpleDateFormat? = null
        private var colorInts: Array<String> = arrayOf()
        fun parse(context: Context, data: Conversation): ConversationViewModel {
            init(context)
            val time = simpleDateFormat?.format(Date(data.timestamp)) ?: ""
            val conversationEmail = data.conversationEmail ?: ""
            val conversationName = data.conversationName ?: ""
            val body = data.body ?: ""
            val color = colorInts[Random().nextInt(colorInts.size - 1)]
            return ConversationViewModel(
                conversationName,
                body,
                time,
                color,
                conversationEmail
            )
        }

        private fun init(context: Context) {
            if (simpleDateFormat == null) {
                simpleDateFormat = SimpleDateFormat("EEE HH:mm", Locale.getDefault())
                colorInts = context.resources.getStringArray(R.array.rainbow_colors)
            }
        }
    }
}