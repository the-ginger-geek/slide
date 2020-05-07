package app.messenger.slide.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import app.messenger.slide.R
import app.messenger.slide.ui.core.helpers.FileHelper
import app.messenger.slide.ui.main.MainFragment
import app.messenger.slide.ui.messaging.MessagingFragment
import kotlinx.android.synthetic.main.main_activity.*
import java.io.FileDescriptor


class MainActivity : AppCompatActivity(),
    ActivityCallback {

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onFragmentChange(fragment: Fragment) {
        val showNavIcon = fragment !is MainFragment
        supportActionBar?.setDisplayHomeAsUpEnabled(showNavIcon)
        supportActionBar?.setDisplayShowHomeEnabled(showNavIcon)
        currentFragment = fragment
    }

    override fun setTitle(text: String) {
        toolbar_title.text = text
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == MessagingFragment.TAKE_PHOTO) {
                if (currentFragment is MessagingFragment) {
                    (currentFragment as MessagingFragment).postCameraCompleted()
                }
            } else if (requestCode == MessagingFragment.PICK_FILE) {
                if (currentFragment is MessagingFragment) {
                    val uri: Uri? = data?.data
                    uri?.let {
                        val parcelFileDescriptor: ParcelFileDescriptor? =
                            contentResolver.openFileDescriptor(uri, "r")
                        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
                        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                        parcelFileDescriptor?.close()
                        (currentFragment as MessagingFragment).postBitmapToPicker(image)
                    }
                }
            }
        }
    }
}
