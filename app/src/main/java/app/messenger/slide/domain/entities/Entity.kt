package app.messenger.slide.domain.entities

interface Entity {
    val type: Int
    companion object {
        const val conversationType = 0
        const val messageType = 1
        const val userType = 2
    }
}