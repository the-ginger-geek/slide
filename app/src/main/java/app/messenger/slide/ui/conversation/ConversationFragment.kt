package app.messenger.slide.ui.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.messenger.slide.R
import app.messenger.slide.databinding.ConversationFragmentBinding
import app.messenger.slide.ui.core.BaseFragment
import kotlinx.android.synthetic.main.users_fragment.*

class ConversationFragment : BaseFragment() {

    private val viewModel: ConversationViewModel by viewModels()
    private lateinit var binding: ConversationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.conversation_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        activityCallback?.setTitle(getString(R.string.conversation_screen_title))

        context?.let { context ->
            arguments?.let { bundle ->
                viewModel.init(context, bundle.getString("user_email") ?: "")
                setupAdapter(
                    recycler,
                    viewModel.messages,
                    LinearLayoutManager(context, RecyclerView.VERTICAL, true)
                )
            }
        }
    }
}