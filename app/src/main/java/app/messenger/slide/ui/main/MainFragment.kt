package app.messenger.slide.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import app.messenger.slide.R
import app.messenger.slide.databinding.MainFragmentBinding
import app.messenger.slide.ui.core.BaseFragment
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.no_data_layout
import kotlinx.android.synthetic.main.main_fragment.recycler
import kotlinx.android.synthetic.main.messaging_fragment.*

class MainFragment : BaseFragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        activityCallback?.setTitle(getString(R.string.app_name))

        context?.let { viewModel.init(it) }
        setupAdapter(recycler, viewModel.conversations)?.setDataChangedCallback {
            if (!it.isNotEmpty()) {
                no_data_layout.visibility = View.VISIBLE
            } else {
                no_data_layout.visibility = View.GONE
            }
        }
    }

}
