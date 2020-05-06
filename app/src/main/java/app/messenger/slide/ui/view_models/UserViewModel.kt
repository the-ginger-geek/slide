package app.messenger.slide.ui.view_models

import android.view.View
import androidx.databinding.Bindable
import app.messenger.slide.R
import app.messenger.slide.domain.entities.User

class UserViewModel(@Bindable val name: String) : Viewable() {

    fun onClick(view: View) {
        navigate(view.context, R.id.action_usersFragment_to_conversationFragment)
    }

    companion object {
        fun parse(data: User): UserViewModel {
            return UserViewModel(data.userName)
        }
    }
}