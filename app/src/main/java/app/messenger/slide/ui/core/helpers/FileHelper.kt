package app.messenger.slide.ui.core.helpers

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

object FileHelper {

    fun generateImageName(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    }

    @Throws(IOException::class)
    fun createImageFile(activity: Activity): File {
        val timeStamp: String = generateImageName()
        val storageDir: File? = activity.cacheDir
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri?): String? {
        if ("content".equals(uri?.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                if (uri != null) {
                    cursor = context.contentResolver.query(
                        uri, projection,
                        null, null, null
                    )
                    cursor?.let {
                        val columnIndex: Int = cursor.getColumnIndexOrThrow("_data")
                        if (cursor.moveToFirst()) {
                            return cursor.getString(columnIndex)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(FileHelper::class.java.simpleName, "ERROR", e)
            } finally {
                cursor?.close()
            }
        } else if ("file".equals(uri?.scheme, ignoreCase = true)) {
            return uri?.path
        }
        return null
    }
}