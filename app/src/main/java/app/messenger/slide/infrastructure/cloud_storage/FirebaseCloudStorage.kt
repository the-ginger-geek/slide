package app.messenger.slide.infrastructure.cloud_storage

import android.graphics.Bitmap
import app.messenger.slide.domain.core.QueryResult
import com.google.firebase.storage.FirebaseStorage

class FirebaseCloudStorage : CloudStorage {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    override fun uploadBitmap(
        bitmap: Bitmap,
        callback: (QueryResult<Boolean, Throwable?>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

}