package app.messenger.slide.infrastructure

import android.util.Log
import app.messenger.slide.domain.entities.Conversation
import app.messenger.slide.domain.entities.Message
import app.messenger.slide.domain.entities.User
import app.messenger.slide.domain.core.QueryResult
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepository : Repository {
    private val tag = FirestoreRepository::class.java.simpleName
    private val firestore: FirebaseFirestore = Firebase.firestore;
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser;

    override fun getAllUsers(callback: (QueryResult<List<User>, Exception>) -> Unit) {
        firestore.collection("users").get()
            .addOnSuccessListener { result ->
                callback.invoke(QueryResult.success(mutableListOf<User>().apply {
                    result?.forEach { snapshot ->
                        snapshot?.let { add(User.parseFirestoreObj(it)) }
                    }
                }))
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error getting documents.", e)
                callback.invoke(QueryResult.failure(e))
            }
    }

    override fun registerNewUser(user: User, callback: (QueryResult<Boolean, Exception>) -> Unit) {
        firestore.collection("users").add(user)
            .addOnSuccessListener { documentReference ->
                callback.invoke(QueryResult.success(true))
                Log.d(tag, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                callback.invoke(QueryResult.failure(e))
                Log.w(tag, "Error adding document", e)
            }
    }

    override fun addNewMessage(
        message: Message,
        toUser: User,
        callback: (QueryResult<Boolean, Exception>) -> Unit
    ) {
        firestore.collection("messages")
            .add(message)
            .addOnSuccessListener { documentReference ->
                callback.invoke(QueryResult.success(true))
                Log.d(tag, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
    }

    override fun getMessagesForUser(
        user: User,
        callback: (QueryResult<List<Message>, Exception>) -> Unit
    ) {
        val docRef = firestore.collection("messages")
        val messages = mutableListOf<Message>()
        val onFailureListener = OnFailureListener { e ->
            callback.invoke(QueryResult.failure(e))
        }

        docRef.whereEqualTo("fromUser", user.email)
            .whereEqualTo("toUser", currentUser?.email).get()
            .addOnSuccessListener { result ->
                messages.addAll(getMessagesFromResult(result))
            }.addOnFailureListener(onFailureListener)
        docRef.whereEqualTo("fromUser", currentUser?.email)
            .whereEqualTo("toUser", user.email).get()
            .addOnSuccessListener { result ->
                messages.addAll(getMessagesFromResult(result))
            }.addOnFailureListener(onFailureListener)

        callback.invoke(QueryResult.success(messages))
    }

    private fun getMessagesFromResult(snapshot: QuerySnapshot?): List<Message> {
        return mutableListOf<Message>().apply {
            snapshot?.forEach { documentSnapshot ->
                documentSnapshot?.let { add(Message.parseFirestoreObj(it)) }
            }
        }
    }

    override fun getRunningConversations(
        toUser: User,
        callback: (QueryResult<Set<Conversation>, Exception>) -> Unit
    ) {
        val conversations = mutableSetOf<Conversation>()
        firestore.collection("messages")
            .whereEqualTo("fromUser", currentUser?.email)
            .whereEqualTo("toUser", currentUser?.email)
            .orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { result ->
                result?.forEach { snapshot ->
                    snapshot?.let { conversations.add(Conversation.parseFirestoreObject(currentUser?.email, it)) }
                }
            }
    }
}