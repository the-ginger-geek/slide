package app.messenger.slide.ui.core

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import app.messenger.slide.R
import app.messenger.slide.BR
import app.messenger.slide.domain.entities.Conversation
import app.messenger.slide.domain.entities.Entity
import app.messenger.slide.domain.entities.Message
import app.messenger.slide.domain.entities.User
import app.messenger.slide.ui.view_models.ConversationViewModel
import app.messenger.slide.ui.view_models.MessageViewModel
import app.messenger.slide.ui.view_models.UserViewModel

class BaseRecyclerAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<Entity>()
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Entity.conversationType -> {
                val binding = viewDataBinding(R.layout.view_conversation_item, parent)
                ConversationViewHolder(binding.root, binding)
            }

            Entity.messageType -> {
                val binding = viewDataBinding(R.layout.view_left_message_item, parent)
                MessageViewHolder(binding.root, binding)
            }

            Entity.userType -> {
                val binding = viewDataBinding(R.layout.view_user_item, parent)
                UsersViewHolder(binding.root, binding)
            }
            else -> {
                BlankViewHolder(inflater.inflate(R.layout.view_blank, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as BaseViewHolder<Entity>).bind(data[position])

    override fun getItemViewType(position: Int): Int = data[position].type
    override fun getItemCount(): Int = data.size

    fun refreshData(incoming: List<Entity>) {
        data.clear()
        data.addAll(incoming)
        notifyDataSetChanged()
    }

    private fun viewDataBinding(layout: Int, parent: ViewGroup): ViewDataBinding {
        return DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            layout,
            parent,
            false
        )
    }
}

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(dataEntity: T)
}

class ConversationViewHolder(view: View, private val binding: ViewDataBinding) :
    BaseViewHolder<Conversation>(view) {
    override fun bind(dataEntity: Conversation) {
        binding.setVariable(BR.conversation, ConversationViewModel.parse(dataEntity))
    }
}

class UsersViewHolder(view: View, private val binding: ViewDataBinding) :
    BaseViewHolder<User>(view) {
    override fun bind(dataEntity: User) {
        binding.setVariable(BR.user, UserViewModel.parse(dataEntity))
    }
}

class MessageViewHolder(view: View, private val binding: ViewDataBinding) :
    BaseViewHolder<Message>(view) {
    override fun bind(dataEntity: Message) {
        binding.setVariable(BR.message, MessageViewModel.parse(dataEntity))
    }
}

class BlankViewHolder(view: View) : BaseViewHolder<Entity?>(view) {
    override fun bind(dataEntity: Entity?) {}
}