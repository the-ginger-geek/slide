package app.messenger.slide.infrastructure.cloud_storage

import android.graphics.Bitmap
import android.net.Uri
import app.messenger.slide.domain.core.QueryResult
import app.messenger.slide.ui.core.helpers.FileHelper
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class FirebaseCloudStorage : CloudStorage {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    override fun uploadFile(
        file: File,
        callback: (QueryResult<Uri, Throwable?>) -> Unit
    ) {
        val imageRef = storageRef.child("images/${file.name}")
        val stream = FileInputStream(file)
        val uploadTask = imageRef.putStream(stream)
        handleUploadTask(uploadTask, imageRef, callback)
    }

    override fun uploadBitmap(bitmap: Bitmap, callback: (QueryResult<Uri, Throwable?>) -> Unit) {
        val imageRef = storageRef.child("images/${FileHelper.generateImageName()}")
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        handleUploadTask(uploadTask, imageRef, callback)
    }

    private fun handleUploadTask(
        uploadTask: UploadTask,
        imageRef: StorageReference,
        callback: (QueryResult<Uri, Throwable?>) -> Unit
    ) {
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