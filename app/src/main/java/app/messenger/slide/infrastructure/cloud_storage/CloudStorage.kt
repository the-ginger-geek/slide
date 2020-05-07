package app.messenger.slide.infrastructure.cloud_storage

import android.graphics.Bitmap
import app.messenger.slide.domain.core.QueryResult

interface CloudStorage {
    fun uploadBitmap(bitmap: Bitmap, callback: (QueryResult<Boolean, Throwable?>) -> Unit)

    companion object {
        fun get(type: Type): CloudStorage {
            when (type) {
                Type.FIRESTORE -> return FirebaseCloudStorage()
                else -> throw NotImplementedError()
            }
        }
    }

    enum class Type {
        FIRESTORE
    }
}