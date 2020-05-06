package app.messenger.slide.domain.entities

import com.google.firebase.firestore.QueryDocumentSnapshot

data class Message(
    val fromUserEmail: String,
    val toUserEmail: String,
    val message: String,
    val timestamp: Long
) : Entity {
    override val type: Int
        get() = Entity.messageType

    companion object {
        fun parseFirestoreObj(snapshot: QueryDocumentSnapshot): Message {
            return Message(
                snapshot["fromUserEmail"] as String,
                snapshot["toUserEmail"] as String,
                snapshot["message"] as String,
                snapshot["timestamp"] as Long
            )
        }
    }
}