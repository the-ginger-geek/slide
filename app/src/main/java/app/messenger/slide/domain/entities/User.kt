package app.messenger.slide.domain.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot

data class User(
    val email: String,
    val userName: String,
    val timestamp: Long = System.currentTimeMillis()
) : Entity {
    override val type: Int
        @Exclude get() = Entity.userType

    companion object {
        fun parseFirestoreObj(snapshot: QueryDocumentSnapshot): User {
            return User(
                snapshot["email"] as String,
                snapshot["userName"] as String,
                snapshot["timestamp"] as Long
            )
        }
    }
}