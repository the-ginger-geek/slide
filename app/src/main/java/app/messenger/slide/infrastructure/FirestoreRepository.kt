package app.messenger.slide.infrastructure

import android.util.Log
import app.messenger.slide.domain.entities.Conversation
import app.messenger.slide.domain.entities.Message
import app.messenger.slide.domain.entities.User
import app.messenger.slide.domain.core.QueryResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.asDeferred
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class FirestoreRepository : Repository, CoroutineScope {
    private val tag = FirestoreRepository::class.java.simpleName
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        firestore.firestoreSettings = settings
    }

    /**
     * Will add a new user to firestore so that new
     * conversations with other users can be started
     *
     * @param firebaseUser
     */
    override fun addNewUser(firebaseUser: FirebaseUser) {
        val user = User(firebaseUser.email ?: "", firebaseUser.displayName ?: "")
        firestore.collection("users").document(user.email).set(user)
    }

    /**
     * Fetches all persisted users from firestore and returns the results via callback
     * to the caller
     *
     * @param callback
     */
    override fun getAllUsers(callback: (QueryResult<List<User>, Throwable?>) -> Unit) {
        launch {
            callback(withContext(Dispatchers.IO) {
                val deferred = firestore.collection("users").get().asDeferred()
                val result = deferred.await()
                if (deferred.getCompletionExceptionOrNull() != null) {
                    QueryResult.failure<List<User>, Throwable?>(deferred.getCompletionExceptionOrNull())
                } else {
                    QueryResult.success<List<User>, Throwable?>(mutableListOf<User>().apply {
                        result?.forEach { snapshot ->
                            snapshot?.let { query ->
                                val user = User.parseFirestoreObj(query)
                                if (user.email != currentUser?.email) {
                                    add(user)
                                }
                            }
                        }
                    })
                }
            })
        }
    }

    /**
     * New message between the current signed in user and the intended user.
     *
     * @param message
     * @param toUserEmail
     * @param callback will be invoked when completed
     */
    override fun addNewMessage(
        message: String,
        toUserEmail: String,
        callback: (QueryResult<Boolean, Throwable?>) -> Unit
    ) {
        launch {
            callback(withContext(Dispatchers.IO) {
                val timestamp = System.currentTimeMillis()
                val deferredList = listOf(
                    firestore.collection("messages")
                        .add(
                            Message(
                                currentUser?.email ?: "",
                                toUserEmail,
                                message,
                                getHashCode(listOf(currentUser?.email ?: "", toUserEmail)),
                                timestamp
                            )
                        )
                        .asDeferred(),

                    firestore.collection("conversations").document("${currentUser?.email}:$toUserEmail")
                        .set(Conversation(currentUser?.email, toUserEmail, message, timestamp))
                        .asDeferred(),

                    firestore.collection("conversations").document("$toUserEmail:${currentUser?.email}")
                        .set(Conversation(toUserEmail, currentUser?.email, message, timestamp))
                        .asDeferred()
                )
                deferredList.awaitAll()
                deferredList.forEach {
                    val error = it.getCompletionExceptionOrNull()
                    if (error != null) {
                        return@withContext QueryResult.failure<Boolean, Throwable?>(error)
                    }
                }

                return@withContext QueryResult.success<Boolean, Throwable?>(true)
            })
        }
    }

    /**
     * Fetches messages in a conversation between the current user and the user passed in.
     *
     * @param userEmail
     * @param callback will be invoked when result is retrieved
     */
    override fun getMessagesForUser(
        userEmail: String,
        callback: (QueryResult<Set<Message>, Throwable?>) -> Unit
    ) {
        val query = firestore
            .collection("messages")
            .whereEqualTo(
                "searchField",
                getHashCode(listOf(currentUser?.email ?: "", userEmail))
            )
            .orderBy("timestamp", Query.Direction.DESCENDING)

        query.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    callback.invoke(QueryResult.failure(e))
                } else {
                    callback.invoke(QueryResult.success<Set<Message>, Throwable?>(mutableSetOf<Message>().apply {
                        snapshot?.forEach { documentSnapshot ->
                            documentSnapshot?.let { add(Message.parseFirestoreObj(it)) }
                        }
                    }))
                }
            }
    }

    /**
     * Find all conversations for this current user
     *
     * @param callback will be invoked when result is retrieved
     */
    override fun getRunningConversations(
        callback: (QueryResult<Set<Conversation>, Throwable?>) -> Unit
    ) {
        val query = firestore.collection("conversations")
            .whereEqualTo("ownerEmail", currentUser?.email ?: "")
        query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                callback.invoke(QueryResult.failure(e))
            } else {
                callback.invoke(QueryResult.success<Set<Conversation>, Throwable?>(
                    mutableSetOf<Conversation>().apply {
                        snapshot?.forEach { documentSnapshot ->
                            documentSnapshot?.let { add(Conversation.parseFirestoreObject(it)) }
                        }
                    }))
            }
        }
    }

    private fun getHashCode(input: List<String>): String {
        var code = 0
        input.forEach { code += it.hashCode() }
        return code.toString()
    }
}