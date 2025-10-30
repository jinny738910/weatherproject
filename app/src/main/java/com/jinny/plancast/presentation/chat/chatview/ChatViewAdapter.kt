package com.jinny.plancast.presentation.chat.chatview

import android.annotation.SuppressLint
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jinny.plancast.R
import com.jinny.plancast.databinding.ViewholderChatlistItemBinding

class ChatViewAdapter : RecyclerView.Adapter<ChatViewAdapter.ToDoItemViewHolder>() {

    private var toDoList: List<ChatViewItem> = listOf()
    private lateinit var toDoItemClickListener: (ChatViewItem) -> Unit
    private lateinit var toDoCheckListener: (ChatViewItem) -> Unit

    inner class ToDoItemViewHolder(
        private val binding: ViewholderChatlistItemBinding,
        val toDoItemClickListener: (ChatViewItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: ChatViewItem) = with(binding) {

            Log.d("ChatListAdapter", "onBindViewHolder: ${data.title}")
            Log.d("ChatListAdapter", "onBindViewHolder: ${data.userId}")

            textView.text = data.title
            textView.setTextColor(ContextCompat.getColor(root.context, R.color.black)
            )

            // Optionally show userId or other info
            textView2.text = data.userId
            textView2.setTextColor(ContextCompat.getColor(root.context, R.color.black))

            inviteButton.setTextColor(ContextCompat.getColor(root.context, R.color.white))
            inviteButton.setText(R.string.gotoChat)

        }

        fun bindViews(data: ChatViewItem) {
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
    fun setToChatList(chatList: List<ChatViewItem>, chatItemClickListener: (ChatViewItem) -> Unit) {
        this.toDoList = chatList
        this.toDoItemClickListener = chatItemClickListener
        notifyDataSetChanged()

    }
}