package app.messenger.slide.domain.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Message(
    val fromUserEmail: String,
    val toUserEmail: String,
    val message: String,
    val searchField: String,
    val timestamp: Long = System.currentTimeMillis()
) : Entity {
    override val type: Int
        @Exclude get() = Entity.messageType


    override fun hashCode(): Int {
        var result = fromUserEmail.hashCode()
        result = 31 * result + toUserEmail.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (fromUserEmail != other.fromUserEmail) return false
        if (toUserEmail != other.toUserEmail) return false
        if (message != other.message) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    companion object {
        fun parseFirestoreObj(snapshot: QueryDocumentSnapshot): Message {
            val fromUserEmail = snapshot["fromUserEmail"] as String
            val toUserEmail = snapshot["toUserEmail"] as String
            val searchField = "$fromUserEmail:$toUserEmail"
            return Message(
                fromUserEmail,
                toUserEmail,
                snapshot["message"] as String,
                searchField,
                snapshot["timestamp"] as Long
            )
        }
    }
}