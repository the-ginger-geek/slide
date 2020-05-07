package app.messenger.slide.ui.messaging

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import app.messenger.slide.domain.core.QueryResult
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.ui.core.BaseViewModel
import app.messenger.slide.ui.core.helpers.FileHelper
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException


class MessagingViewModel : BaseViewModel() {

    private var userEmail: String = ""
    var currentPhotoPath: String = ""

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
        val activity = (view.context as Activity)
        showFileChooser(activity)
    }

    fun openCamera(view: View) {
        val activity = (view.context as Activity)
        startCameraIntent(activity)
    }

    fun sendImageToCloud() {
        val file = File(currentPhotoPath)
        uploadingPhoto.value = true
        cloudStorage?.uploadFile(file) { uploadResult ->
            handleImageUploadResult(uploadResult)
        }
    }

    fun sendImageToCloud(bitmap: Bitmap) {
        uploadingPhoto.value = true
        cloudStorage?.uploadBitmap(bitmap) { uploadResult ->
            handleImageUploadResult(uploadResult)
        }
    }

    private fun handleImageUploadResult(uploadResult: QueryResult<Uri, Throwable?>) {
        uploadingPhoto.value = false
        popupVisible.value = false
        if (uploadResult.isSuccessful()) {
            repository?.addNewImageMessage(uploadResult.value?.toString() ?: "", userEmail) {
                enabled.value = true
            }
        }
    }

    private fun showFileChooser(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            activity.startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                MessagingFragment.PICK_FILE
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                activity, "Please install a File Manager.",
                Toast.LENGTH_SHORT
            ).show()
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