package app.messenger.slide.infrastructure.cloud_storage

import android.net.Uri
import app.messenger.slide.domain.core.QueryResult
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream

class FirebaseCloudStorage : CloudStorage {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    override fun uploadBitmap(
        file: File,
        callback: (QueryResult<Uri, Throwable?>) -> Unit
    ) {
        val imageRef = storageRef.child("images/${file.name}")
        val stream = FileInputStream(file)

        val uploadTask = imageRef.putStream(stream)
        uploadTask.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnSuccessListener { uri ->
            callback.invoke(QueryResult.success(uri))
        }.addOnFailureListener {
            callback.invoke(QueryResult.failure(it))
        }
    }

}