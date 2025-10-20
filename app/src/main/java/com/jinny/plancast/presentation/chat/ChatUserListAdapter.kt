package com.jinny.plancast.presentation.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jinny.plancast.R
import com.jinny.plancast.databinding.ViewholderChatlistItemBinding

class ChatUserListAdapter : RecyclerView.Adapter<ChatUserListAdapter.ToDoItemViewHolder>() {

    private var toDoList: List<ChatUserListItem> = listOf()
    private lateinit var toDoItemClickListener: (ChatUserListItem) -> Unit
    private lateinit var toDoCheckListener: (ChatUserListItem) -> Unit

    inner class ToDoItemViewHolder(
        private val binding: ViewholderChatlistItemBinding,
        val toDoItemClickListener: (ChatUserListItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: ChatUserListItem) = with(binding) {

            Log.d("ChatListAdapter", "onBindViewHolder: ${data.name}")
            Log.d("ChatListAdapter", "onBindViewHolder: ${data.userId}")

            textView.text = data.name
            textView.setTextColor(ContextCompat.getColor(root.context, R.color.black)
            )

            // Optionally show userId or other info
            textView2.text = data.userId
            textView2.setTextColor(ContextCompat.getColor(root.context, R.color.black))

            inviteButton.setTextColor(ContextCompat.getColor(root.context, R.color.white))

        }

        fun bindViews(data: ChatUserListItem) {
            binding.root.setOnClickListener {
                toDoItemClickListener(data)
            }
            binding.inviteButton.setOnClickListener {
                toDoItemClickListener(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val view = ViewholderChatlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoItemViewHolder(view, toDoItemClickListener)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        holder.bindData(toDoList[position])
        holder.bindViews(toDoList[position])
    }

    override fun getItemCount(): Int = toDoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setToChatList(chatList: MutableList<ChatUserListItem>, chatItemClickListener: (ChatUserListItem) -> Unit) {
        this.toDoList = chatList
        this.toDoItemClickListener = chatItemClickListener
        notifyDataSetChanged()

    }
}