package com.jinny.plancast.presentation.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jinny.plancast.R
import com.jinny.plancast.databinding.ViewholderChatlistItemBinding
import com.jinny.plancast.databinding.ViewholderTodoItemBinding

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ToDoItemViewHolder>() {

    private var toDoList: List<ChatListItem> = listOf()
    private lateinit var toDoItemClickListener: (ChatListItem) -> Unit
    private lateinit var toDoCheckListener: (ChatListItem) -> Unit

    inner class ToDoItemViewHolder(
        private val binding: ViewholderChatlistItemBinding,
        val toDoItemClickListener: (ChatListItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: ChatListItem) = with(binding) {

            Log.d("ChatListAdapter", "onBindViewHolder: ${data.name}")
            Log.d("ChatListAdapter", "onBindViewHolder: ${data.userId}")

            // Show primary text as chat user's name
            textView.text = data.name
            textView.setTextColor(ContextCompat.getColor(root.context, R.color.white))

            // Optionally show userId or other info
            textView2.text = data.userId
            textView2.setTextColor(ContextCompat.getColor(root.context, R.color.white))
        }

        fun bindViews(data: ChatListItem) {
            binding.root.setOnClickListener {
                toDoItemClickListener(data)
            }
        }

        private fun checkToDoComplete(isChecked: Boolean) = with(binding) {
            container.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    if (isChecked) {
                        R.color.gray_300
                    } else {
                        R.color.white
                    }
                )
            )
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
    fun setToChatList(chatList: List<ChatListItem>, chatItemClickListener: (ChatListItem) -> Unit) {
        this.toDoList = chatList
        this.toDoItemClickListener = chatItemClickListener
        notifyDataSetChanged()
    }
}