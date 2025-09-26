package com.jinny.plancast.presentation.chat

import android.annotation.SuppressLint

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class ChatListActivity : BaseActivity<ChatListViewModel>(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: DatabaseReference = Firebase.database.reference.child("users")
    private val chatListAdapter = ChatListAdapter()
    private val chatListItems = mutableListOf<ChatListItem>()

    override val viewModel: ChatListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews(binding)

        binding.addToDoButton.isGone =true
        binding.weatherDetailsButton.isGone =true


        Log.d("ChatListActivity", "currentUserId: ${getCurrentUserID()}")
        userDB.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ChatListActivity", "show 유저! ")
                Log.d("ChatListActivity", "datasnapshot: $snapshot")

                val userId = snapshot.child("userId").value.toString()
                var name = "undecided"
                if (snapshot.child("name").value != null) {
                    name = snapshot.child("name").value.toString()
                }

                Log.d("onDataChange ChatListActivity", "userId: $userId, name: $name")

                chatListItems.add(ChatListItem(userId, name))
                chatListAdapter.setToChatList(chatListItems, chatItemClickListener = { chatListItem ->
                })
                chatListAdapter.notifyDataSetChanged()


                getUnSelectedUsers()

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ChatListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = chatListAdapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    override fun observeData() {
    }

    fun getUnSelectedUsers() {

        userDB.addChildEventListener(object : ChildEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child("userId").value != getCurrentUserID()
                    && snapshot.child("likedBy").child("like").hasChild(getCurrentUserID()).not()
                    && snapshot.child("likedBy").child("dislike").hasChild(getCurrentUserID()).not()
                ) {

                    Log.d("ChatListActivity", "snapshot: $snapshot")
                    val userId = snapshot.child("userId").value.toString()
                    var name = "undecided"
                    if (snapshot.child("name").value != null) {
                        name = snapshot.child("name").value.toString()
                    }

                    Log.d("ChatListActivity", "userId: $userId, name: $name")

                    chatListItems.add(ChatListItem(userId, name))
                    chatListAdapter.setToChatList(chatListItems, chatItemClickListener = { chatListItem ->
                    })
                    Log.d("ChatListActivity", "itemcount: ${chatListAdapter.getItemCount()}")

                    chatListAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                chatListItems.find { it.userId == dataSnapshot.key }?.let {
                    it.name = dataSnapshot.child("name").value.toString()
                    Log.d("ChatListActivity", "onChildChanged - name: ${it.name}")
                }
                chatListAdapter.setToChatList(chatListItems, chatItemClickListener = { chatListItem ->
                })
                chatListAdapter.notifyDataSetChanged()
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