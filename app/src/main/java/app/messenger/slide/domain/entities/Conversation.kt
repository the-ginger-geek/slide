package app.messenger.slide.domain.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot

class Conversation(
    var ownerEmail: String?,
    var conversationEmail: String?,
    var body: String?,
    var timestamp: Long = 0
) : Entity {

    override val type: Int
        @Exclude get() = Entity.conversationType

    companion object {
        fun parseFirestoreObject(snapshot: QueryDocumentSnapshot): Conversation {
            return Conversation(
                snapshot["ownerEmail"] as String?,
                snapshot["conversationEmail"] as String?,
                snapshot["body"] as String?,
                snapshot["timestamp"] as Long
            )
        }
    }
}