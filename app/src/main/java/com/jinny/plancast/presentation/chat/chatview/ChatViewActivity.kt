package com.jinny.plancast.presentation.chat.chatview

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.databinding.ActivityListBinding
import com.jinny.plancast.presentation.chat.chatroom.ChatRoomActivity
import com.jinny.plancast.presentation.chat.chatuserlist.ChatUserListItem
import com.jinny.plancast.presentation.financial.password.PasswordActivity
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class ChatViewActivity : BaseActivity<ChatViewViewModel>(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: DatabaseReference = Firebase.database.reference
    private var chatDB: DatabaseReference = Firebase.database.reference.child("chats")
    private val chatViewAdapter = ChatViewAdapter()
    private val chatViewItems = mutableListOf<ChatViewItem>()

    private val chatRoomLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // 결과가 돌아왔을 때 이 람다 블록이 실행됩니다.
            if (result.resultCode == Activity.RESULT_OK) {
                // 성공적인 결과를 처리합니다.
                val data: Intent? = result.data
                val message = data?.getStringExtra("result_key")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                viewModel.fetchData()
            }
        }

    private val listener = object:  ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val chatViewItem = snapshot.getValue(ChatViewItem::class.java)
            chatViewItem ?: return

            Log.d("ChatListActivity", "${chatViewItem.title}, ${chatViewItem.userId}, ${chatViewItem.date}, ${chatViewItem.description}")

            chatViewItems.add(chatViewItem)
            chatViewAdapter.setToChatList(chatViewItems, chatItemClickListener = { ChatViewItem ->
                Log.d("ChatViewActivity", "onChatListItemClick : ${ChatViewItem.userId}")
                val intent = Intent(this@ChatViewActivity, ChatRoomActivity::class.java)
                intent.putExtra("chat_item", ChatViewItem)
                chatRoomLauncher.launch(intent)
            })
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    override val viewModel: ChatViewViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews(binding)

        binding.addToDoButton.isGone =true
        binding.weatherDetailsButton.isGone =true

        val userId = getCurrentUserID()
        val currentUserChatDB = chatDB.child(userId)

        currentUserChatDB.addChildEventListener(listener)

        Log.d("ChatViewActivity", "currentUserId: ${getCurrentUserID()}")
        currentUserChatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ChatViewActivity", "addListenerForSingleValueEvent -> onDataChange")
                Log.d("ChatViewActivity", "datasnapshot: $snapshot")

                val userId = snapshot.child("userId").value.toString()
                var name = "undecided"
                if (snapshot.child("name").value != null) {
                    name = snapshot.child("name").value.toString()
                }
                var title = "undecided"
                if (snapshot.child("title").value != null) {
                    title = snapshot.child("title").value.toString()
                }
                var date = "undecided"
                if (snapshot.child("date").value != null) {
                    date = snapshot.child("date").value.toString()
                }
                var destination = "undecided"
                if (snapshot.child("destination").value != null) {
                    destination = snapshot.child("destination").value.toString()
                }
                var message = "undecided"
                if (snapshot.child("message").value != null) {
                    message = snapshot.child("message").value.toString()
                }
                var imageUrl = "undecided"
                if (snapshot.child("imageUrl").value != null) {
                    imageUrl = snapshot.child("imageUrl").value.toString()
                }

                Log.d("onDataChange ChatListActivity", "userId: $userId, name: $name")


                if (userId.isEmpty() || name == "undecided") {
                    Log.d("ChatViewActivity", "setToChatList: userId가 undecided인 아이템이 있습니다.")
                } else {
                    chatViewItems.add(ChatViewItem(userId, name, "", "", "", "", ""))
                    chatViewAdapter.setToChatList(chatViewItems, chatItemClickListener = { chatViewItem ->
                        Log.d("ChatViewActivity", "onChatListItemClick : ${chatViewItem.userId}")
                    })
                    chatViewAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ChatViewActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = chatViewAdapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

    }

    override fun observeData() {
    }


    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            return ""
        }

        return auth.currentUser!!.uid
    }

}