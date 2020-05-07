package app.messenger.slide.infrastructure

import app.messenger.slide.domain.entities.Conversation
import app.messenger.slide.domain.entities.Message
import app.messenger.slide.domain.entities.User
import app.messenger.slide.domain.core.QueryResult
import com.google.firebase.auth.FirebaseUser

interface Repository {
    fun addNewUser(firebaseUser: FirebaseUser)
    fun getAllUsers(callback: (QueryResult<List<User>, Throwable?>) -> Unit)

    fun addNewMessage(message: String, toUserEmail: String, callback: (QueryResult<Boolean, Throwable?>) -> Unit)
    fun getMessagesForUser(userEmail: String, callback: (QueryResult<Set<Message>, Throwable?>) -> Unit)

    fun getRunningConversations(callback: (QueryResult<Set<Conversation>, Throwable?>) -> Unit)

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