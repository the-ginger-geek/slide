package app.messenger.slide.ui.messaging

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.ui.core.BaseViewModel
import app.messenger.slide.ui.core.helpers.FileHelper
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException

class MessagingViewModel : BaseViewModel() {

    private var userEmail: String = ""
    lateinit var currentPhotoPath: String

    val messages: MutableLiveData<List<Entity>> = MutableLiveData<List<Entity>>()
    val input: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            it.value = ""
        }
    }
    val enabled: MutableLiveData<Boolean> = MutableLiveData()
    val popupVisible: MutableLiveData<Boolean> = MutableLiveData()
    val uploadingPhoto: MutableLiveData<Boolean> = MutableLiveData()

    fun init(context: Context, userEmail: String) {
        this.userEmail = userEmail
        inject(context)
        populateMessages()
    }

    fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        enabled.value = s.isNotEmpty()
    }

    fun onClickSend(view: View) {
        enabled.value = false
        val text = input.value ?: ""
        repository?.addNewMessage(text, userEmail) { result ->
            if (!result.isSuccessful())  {
                Snackbar.make(view, "Failed to send please try again", Snackbar.LENGTH_LONG).show()
            }
            enabled.value = true
        }
        input.value = ""
    }

    fun onClickAttach(view: View) {
        popupVisible.value = true
    }

    fun closePopup(view: View) {
        popupVisible.value = false
    }

    fun pickFile(view: View) {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        ).also { galleryIntent ->
            (view.context as Activity).startActivityForResult(
                galleryIntent,
                MessagingFragment.PICK_FILE
            )
        }
    }

    fun openCamera(view: View) {
        val activity = (view.context as Activity)
        startCameraIntent(activity)
    }

    fun postBitmapToCloud() {
        val file = File(currentPhotoPath)
        uploadingPhoto.value = true
        cloudStorage?.uploadBitmap(file) { uploadResult ->
            uploadingPhoto.value = false
            popupVisible.value = false
            if (uploadResult.isSuccessful()) {
                repository?.addNewImageMessage(uploadResult.value?.toString() ?: "", userEmail) {
                    enabled.value = true
                }
            }
        }
    }

    private fun startCameraIntent(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                val photoFile: File? = try {
                    FileHelper.createImageFile(activity).apply {
                        currentPhotoPath = absolutePath
                    }
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        activity,
                        "app.messenger.slide.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureIntent.putExtra("aspectX", 1);
                    takePictureIntent.putExtra("aspectY", 1);
                    activity.startActivityForResult(takePictureIntent, MessagingFragment.TAKE_PHOTO)
                }
            }
        }
    }

    private fun populateMessages() {
        repository?.getMessagesForUser(userEmail) { result ->
            if (result.isSuccessful()) messages.value = result.value?.toList() ?: listOf()
        }
    }
}