package app.messenger.slide.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import app.messenger.slide.R
import app.messenger.slide.databinding.UsersFragmentBinding
import app.messenger.slide.ui.core.BaseFragment
import kotlinx.android.synthetic.main.users_fragment.*

class UsersFragment : BaseFragment() {

    private val viewModel: UsersViewModel by viewModels()
    private lateinit var binding: UsersFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.users_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        activityCallback?.setTitle(getString(R.string.user_screen_title))

        context?.let { viewModel.init(it) }
        setupAdapter(recycler, viewModel.users)
    }
}