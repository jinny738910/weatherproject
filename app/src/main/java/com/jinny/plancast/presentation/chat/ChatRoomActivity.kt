package com.jinny.plancast.presentation.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.jinny.plancast.presentation.todo.view.ToDoAdapter
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class ChatRoomActivity : BaseActivity<ChatRoomViewModel>(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: DatabaseReference = Firebase.database.reference
    private val chatRoomAdapter = ChatRoomAdapter()
    private val chatRoomItems = mutableListOf<ChatRoomItem>()

    override val viewModel: ChatRoomViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addToDoButton.isGone =true
        binding.weatherDetailsButton.isGone =true

        userDB = Firebase.database.reference.child("users")

        getUnSelectedUsers()

        val currentUserDB = userDB.child(getCurrentUserID())
        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("name").value == null) {
//                    showNameInputPopup()
                    return
                }

                getUnSelectedUsers()

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ChatRoomActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = chatRoomAdapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    override fun observeData() {
    }

    fun getUnSelectedUsers() {
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child("userId").value != getCurrentUserID()
                    && snapshot.child("likedBy").child("like").hasChild(getCurrentUserID()).not()
                    && snapshot.child("likedBy").child("dislike").hasChild(getCurrentUserID()).not()
                ) {

                    val userId = snapshot.child("userId").value.toString()
                    var name = "undecided"
                    if (snapshot.child("name").value != null) {
                        name = snapshot.child("name").value.toString()
                    }
                    val message = "채팅을 시작해보세요!"

                    chatRoomItems.add(ChatRoomItem(userId, name, message))
                    chatRoomAdapter.setToChatRoom(chatRoomItems, chatRoomClickListener = { chatRoomItem ->
                    })
                    chatRoomAdapter.notifyDataSetChanged()

//                    cardItems.add(CardItem(userId, name))
//                    adapter.submitList(cardItems)
//                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                chatRoomItems.find { it.userId == dataSnapshot.key }?.let {
                    it.name = dataSnapshot.child("name").value.toString()
                }
                chatRoomAdapter.setToChatRoom(chatRoomItems, chatRoomClickListener = { chatRoomItem ->
                })
                chatRoomAdapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            return ""
        }

        return auth.currentUser!!.uid
    }

}