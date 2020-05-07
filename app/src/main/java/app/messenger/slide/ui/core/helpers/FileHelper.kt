package app.messenger.slide.ui.core.helpers

import android.app.Activity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileHelper {
    @Throws(IOException::class)
    fun createImageFile(activity: Activity): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = activity.cacheDir
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}