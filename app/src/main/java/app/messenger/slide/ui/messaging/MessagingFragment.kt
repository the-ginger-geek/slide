package app.messenger.slide.ui.messaging

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.messenger.slide.R
import app.messenger.slide.databinding.MessagingFragmentBinding
import app.messenger.slide.ui.core.BaseFragment
import kotlinx.android.synthetic.main.messaging_fragment.*
import kotlinx.android.synthetic.main.users_fragment.*
import kotlinx.android.synthetic.main.users_fragment.recycler


class MessagingFragment : BaseFragment() {

    private val viewModel: MessagingViewModel by viewModels()
    private lateinit var binding: MessagingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.messaging_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        activityCallback?.setTitle(getString(R.string.messaging_screen_title))

        context?.let { context ->
            arguments?.let { bundle ->
                setupAdapter(
                    recycler,
                    viewModel.messages,
                    LinearLayoutManager(context, RecyclerView.VERTICAL, true)
                )?.setDataChangedCallback {
                    if (it.isNotEmpty()) {
                        recycler.scrollToPosition(0)
                    } else {
                        no_data_layout.visibility = View.VISIBLE
                    }
                }
                viewModel.init(context, bundle.getString("user_email") ?: "")
            }
        }
    }

    fun postCameraCompleted() {
        viewModel.sendImageToCloud()
    }

    fun postBitmapToPicker(image: Bitmap) {
        viewModel.sendImageToCloud(image)
    }

    companion object {
        const val PICK_FILE = 120
        const val TAKE_PHOTO = 121
    }
}