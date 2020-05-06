package app.messenger.slide.infrastructure

import app.messenger.slide.domain.entities.Conversation
import app.messenger.slide.domain.entities.Message
import app.messenger.slide.domain.entities.User
import app.messenger.slide.domain.core.QueryResult
import com.google.firebase.auth.FirebaseUser

interface Repository {
    fun addNewUser(firebaseUser: FirebaseUser)
    fun getAllUsers(callback: (QueryResult<List<User>, Exception>) -> Unit)
    fun registerNewUser(user: User, callback: (QueryResult<Boolean, Exception>) -> Unit)
    fun addNewMessage(message: String, toUserEmail: String, callback: (QueryResult<Boolean, Exception>) -> Unit)
    fun getMessagesForUser(userEmail: String, callback: (QueryResult<List<Message>, Exception>) -> Unit)
    fun getRunningConversations(callback: (QueryResult<Set<Conversation>, Exception>) -> Unit)

    companion object {
        fun buildRepository(type: Type): Repository {
            when (type) {
                Type.FIRESTORE -> return FirestoreRepository()
                else -> throw NotImplementedError()
            }
        }
    }

    enum class Type {
        FIRESTORE
    }
}