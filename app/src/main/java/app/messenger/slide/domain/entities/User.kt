package app.messenger.slide.domain.entities

import com.google.firebase.firestore.QueryDocumentSnapshot

data class User(val email: String, val userName: String) {
    companion object {
        fun parseFirestoreObj(snapshot: QueryDocumentSnapshot): User {
            return User(
                snapshot["email"] as String,
                snapshot["userName"] as String
            )
        }
    }
}