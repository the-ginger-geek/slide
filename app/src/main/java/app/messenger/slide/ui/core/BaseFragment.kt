package app.messenger.slide.ui.core

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.messenger.slide.ui.ActivityCallback
import app.messenger.slide.domain.entities.Entity

abstract class BaseFragment : Fragment() {
    protected var activityCallback: ActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityCallback) {
            activityCallback = context
        }
    }

    override fun onResume() {
        super.onResume()
        activityCallback?.onFragmentChange(this)
    }

    fun setupAdapter(
        recycler: RecyclerView,
        dataSet: LiveData<List<Entity>>,
        layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
    ): BaseRecyclerAdapter? {
        return context?.let { context ->
            val adapter = BaseRecyclerAdapter(context)
            recycler.adapter = adapter
            recycler.layoutManager = layoutManager
            dataSet.observe(viewLifecycleOwner, Observer {
                adapter.refreshData(it ?: listOf())
            })
            adapter
        } ?: run { null }
    }
}