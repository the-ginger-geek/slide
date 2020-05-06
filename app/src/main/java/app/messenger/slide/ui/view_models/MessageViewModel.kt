package app.messenger.slide.ui.view_models

import app.messenger.slide.domain.entities.Conversation

class MessageViewModel : Viewable() {
    companion object {
        fun parse(data: Conversation): MessageViewModel {
            return MessageViewModel()
        }
    }
}