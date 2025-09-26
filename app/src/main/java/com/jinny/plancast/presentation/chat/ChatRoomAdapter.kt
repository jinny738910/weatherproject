package com.jinny.plancast.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jinny.plancast.R
import com.jinny.plancast.databinding.ViewholderTodoItemBinding

class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomAdapter.ToDoItemViewHolder>() {

    private var toDoList: List<ChatRoomItem> = listOf()
    private lateinit var toDoItemClickListener: (ChatRoomItem) -> Unit
    private lateinit var toDoCheckListener: (ChatRoomItem) -> Unit

    inner class ToDoItemViewHolder(
        private val binding: ViewholderTodoItemBinding,
        val toDoItemClickListener: (ChatRoomItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: ChatRoomItem) = with(binding) {
        }

        fun bindViews(data: ChatRoomItem) {
            binding.root.setOnClickListener {
                toDoItemClickListener(data)
            }
        }

        private fun checkToDoComplete(isChecked: Boolean) = with(binding) {
            checkBox.isChecked = isChecked
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
        val view = ViewholderTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoItemViewHolder(view, toDoItemClickListener)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        holder.bindData(toDoList[position])
        holder.bindViews(toDoList[position])
    }

    override fun getItemCount(): Int = toDoList.size

    fun setToChatRoom(chatRoom: List<ChatRoomItem>, chatRoomClickListener: (ChatRoomItem) -> Unit) {
        this.toDoList = chatRoom
        this.toDoItemClickListener = chatRoomClickListener
        notifyDataSetChanged()
    }
}