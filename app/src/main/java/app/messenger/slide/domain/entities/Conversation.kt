package app.messenger.slide.domain.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot

class Conversation() : Entity {
    var userEmail: String? = ""
    var body: String? = ""
    var timestamp: Long = 0

    private constructor(userId: String?, body: String?, timestamp: Long): this() {
        this.userEmail = userId
        this.body = body
        this.timestamp = timestamp
    }

    override val type: Int
        @Exclude get() = Entity.conversationType

    override fun hashCode(): Int {
        return userEmail?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Conversation

        if (userEmail != other.userEmail) return false

        return true
    }

    companion object {
        fun parseFirestoreObject(currentUserEmail: String?, snapshot: QueryDocumentSnapshot): Conversation {
            val fromUserEmail = snapshot["fromUserEmail"] as String?
            val toUserEmail = snapshot["toUserEmail"] as String?
            val conversationId = if (fromUserEmail == currentUserEmail) fromUserEmail else toUserEmail
            val timestamp = snapshot["timestamp"] as Long
            return Conversation(conversationId, snapshot["message"] as String?, timestamp)
        }
    }
}