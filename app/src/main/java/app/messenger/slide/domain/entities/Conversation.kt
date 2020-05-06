package app.messenger.slide.domain.entities

import com.google.firebase.firestore.QueryDocumentSnapshot

class Conversation() : Entity {
    var userId: String? = ""
    var userName: String? = ""
    var body: String? = ""
    var timestamp: Long = 0

    private constructor(userId: String, body: String, timestamp: Long): this() {
        this.userId = userId
        this.userName = userName
        this.body = body
        this.timestamp = timestamp
    }

    override val type: Int
        get() = Entity.conversationType

    override fun equals(other: Any?): Boolean {
        if (other is Conversation) {
            return userId == other.userId
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = userId?.hashCode() ?: 0
        result = 31 * result + (userName?.hashCode() ?: 0)
        result = 31 * result + (body?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun parseFirestoreObject(currentUserEmail: String?, snapshot: QueryDocumentSnapshot): Conversation {
            val fromUserEmail = snapshot["fromUser"] as String
            val toUserEmail = snapshot["toUser"] as String
            val conversationId = if (fromUserEmail == currentUserEmail) fromUserEmail else toUserEmail
            val timestamp = snapshot["timestamp"] as Long
            return Conversation(conversationId, snapshot["message"] as String, timestamp)
        }
    }
}