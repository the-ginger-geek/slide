package app.messenger.slide.infrastructure.cloud_storage

import android.net.Uri
import app.messenger.slide.domain.core.QueryResult
import java.io.File

interface CloudStorage {
    fun uploadBitmap(file: File, callback: (QueryResult<Uri, Throwable?>) -> Unit)

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