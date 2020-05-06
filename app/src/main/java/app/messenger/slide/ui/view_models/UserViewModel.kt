package app.messenger.slide.ui.view_models

import android.os.Bundle
import android.view.View
import androidx.databinding.Bindable
import app.messenger.slide.R
import app.messenger.slide.domain.entities.User

class UserViewModel(@Bindable val name: String, private val email: String) : Viewable() {

    fun onClick(view: View) {
        val bundle = Bundle()
        bundle.putString("user_email", email)
        navigate(view.context, R.id.action_usersFragment_to_conversationFragment, bundle)
    }

    companion object {
        fun parse(data: User): UserViewModel {
            return UserViewModel(data.userName, data.email)
        }
    }
}