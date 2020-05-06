package app.messenger.slide.domain.entities

import app.messenger.slide.domain.core.QueryResult
import com.google.firebase.firestore.QueryDocumentSnapshot

class Conversation() {
    var userId: String? = ""
    var userName: String? = ""
    var body: String? = ""

    private constructor(userId: String, body: String): this() {
        this.userId = userId
        this.userName = userName
        this.body = body
    }

    override fun equals(other: Any?): Boolean {
        if (other is Conversation) {
            return userId == other.userId
        }

        return super.equals(other)
    }

    companion object {
        fun parseFirestoreObject(currentUserEmail: String?, snapshot: QueryDocumentSnapshot): Conversation {
            val fromUserEmail = snapshot["fromUser"] as String
            val toUserEmail = snapshot["toUser"] as String
            val conversationId = if (fromUserEmail == currentUserEmail) fromUserEmail else toUserEmail
            return Conversation(conversationId, snapshot["message"] as String)
        }
    }
}