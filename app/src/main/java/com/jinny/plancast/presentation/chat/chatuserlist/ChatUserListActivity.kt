package com.jinny.plancast.presentation.chat.chatuserlist

import android.annotation.SuppressLint
import android.app.AlertDialog

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

class ChatUserListActivity : BaseActivity<ChatUserListViewModel>(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: DatabaseReference = Firebase.database.reference.child("users")
    private val chatUserListAdapter = ChatUserListAdapter()
    private val chatUserListItems = mutableListOf<ChatUserListItem>()

    override val viewModel: ChatUserListViewModel by viewModel()

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
                Log.d("ChatListActivity", "addListenerForSingleValueEvent -> onDataChange")
                Log.d("ChatListActivity", "datasnapshot: $snapshot")

                val userId = snapshot.child("userId").value.toString()
                var name = "undecided"
                if (snapshot.child("name").value != null) {
                    name = snapshot.child("name").value.toString()
                }

                Log.d("onDataChange ChatListActivity", "userId: $userId, name: $name")


                if (userId.isEmpty() || name == "undecided") {
                    Log.d("ChatListActivity", "setToChatList: userId가 undecided인 아이템이 있습니다.")
                } else {
                    chatUserListItems.add(ChatUserListItem(userId, name))
                    chatUserListAdapter.setToChatList(chatUserListItems, chatItemClickListener = { chatListItem ->
                        Log.d("ChatListActivity", "onChatListItemClick : ${chatListItem.userId}")
                        invite(chatListItem .userId)
                    })
                    chatUserListAdapter.notifyDataSetChanged()
                }

                getUnSelectedUsers()
                getInviteMessage()
                getMatchMessage()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ChatUserListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = chatUserListAdapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

    }

    override fun observeData() {
    }

    private fun getUnSelectedUsers() {

        userDB.addChildEventListener(object : ChildEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child("userId").value != getCurrentUserID()
                    && snapshot.child("likedBy").child("like").hasChild(getCurrentUserID()).not()
                    && snapshot.child("likedBy").child("dislike").hasChild(getCurrentUserID()).not()
                ) {

                    Log.d("ChatListActivity", "addChildEventListener -> onChildAdded")
                    Log.d("ChatListActivity", "snapshot: $snapshot")
                    val userId = snapshot.child("userId").value.toString()
                    var name = "undecided"
                    if (snapshot.child("name").value != null) {
                        name = snapshot.child("name").value.toString()
                    }

                    Log.d("ChatListActivity", "userId: $userId, name: $name")

                    chatUserListItems.add(ChatUserListItem(userId, name))
                    chatUserListAdapter.setToChatList(chatUserListItems, chatItemClickListener = { chatListItem ->
                        Log.d("ChatListActivity", "onChatListItemClick : ${chatListItem.userId}")
                        invite(chatListItem.userId)
                    })
                    Log.d("ChatListActivity", "itemcount: ${chatUserListAdapter.getItemCount()}")

                    chatUserListAdapter.notifyDataSetChanged()
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                chatUserListItems.find { it.userId == dataSnapshot.key }?.let {
                    it.name = dataSnapshot.child("name").value.toString()
                    Log.d("ChatListActivity", "onChildChanged - name: ${it.name}")
                }
                chatUserListAdapter.setToChatList(chatUserListItems, chatItemClickListener = { chatListItem ->
                    Log.d("ChatListActivity", "onChatListItemClick : ${chatListItem.userId}")
                    invite(chatListItem.userId)
                })
                chatUserListAdapter.notifyDataSetChanged()

            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getInviteMessage() {

        val inviteDB = userDB.child(getCurrentUserID()).child("invitedBy").child("invite")
        inviteDB.addChildEventListener(object : ChildEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val isInvited = snapshot.getValue(Boolean::class.java) ?: false
                if (isInvited == true) {
                    val inviteUser = snapshot.key.toString()
                    Toast.makeText(this@ChatUserListActivity, "$inviteUser 님이 초대했습니다.", Toast.LENGTH_SHORT).show()
                    showInviteDialog(inviteUser)
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onChildChanged(snapshot: DataSnapshot, s: String?) {

                val isInvited = snapshot.getValue(Boolean::class.java) ?: false
                if (isInvited == true) {
                    val inviteUser = snapshot.key.toString()
                    Toast.makeText(this@ChatUserListActivity, "$inviteUser 님이 초대했습니다.", Toast.LENGTH_SHORT).show()
                    showInviteDialog(inviteUser)
                }
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getMatchMessage() {

        val matchDB = userDB.child(getCurrentUserID()).child("invitedBy").child("match")
        matchDB.addChildEventListener(object : ChildEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val isMatched = snapshot.getValue(Boolean::class.java) ?: false
                if (isMatched == true) {
                    val matchedUser = snapshot.key.toString()
                    Toast.makeText(this@ChatUserListActivity, "$matchedUser 님이 매치됐습니다.", Toast.LENGTH_SHORT).show()
                    chatUserListAdapter.setToChatList(chatUserListItems, chatItemClickListener = { chatListItem ->
                        Log.d("ChatListActivity", "onChatListItemClick : ${chatListItem.userId}")

                    })
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onChildChanged(snapshot: DataSnapshot, s: String?) {

                val isMatched = snapshot.getValue(Boolean::class.java) ?: false
                if (isMatched == true) {
                    val matchedUser = snapshot.key.toString()
                    Toast.makeText(this@ChatUserListActivity, "$matchedUser 님이 매치됐습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun showInviteDialog(inviteUser: String) {
        Log.d("ChatListActivity", "showInviteDialog !!")

        val builder = AlertDialog.Builder(this)

        // 팝업창의 제목과 메시지 설정
        builder.setTitle("할일 초대 요청")
        builder.setMessage("$inviteUser 님이 초대 했습니다.")

        // '예' 버튼 (Positive Button)
        builder.setPositiveButton("수락하기") { dialog, which ->
            // '예' 버튼을 눌렀을 때 실행될 코드
            invite(inviteUser)
            Toast.makeText(this, "채팅방으로 이동합니다.", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@DetailActivity, PaymentActivity::class.java)
//            paymentLauncher.launch(intent)
        }

        // '아니오' 버튼 (Negative Button)
        builder.setNegativeButton("거절하기") { dialog, which ->
            // '아니오' 버튼을 눌렀을 때 실행될 코드
            Toast.makeText(this, "초대를 거절하였습니다.", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@DetailActivity, TransferActivity::class.java)
//            transferLauncher.launch(intent)
        }
        // 팝업창 생성 및 표시
        val dialog = builder.create()
        dialog.show()
    }


    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            return ""
        }

        return auth.currentUser!!.uid
    }

    private fun invite(invitedUserId: String) {

        Log.d(
            "ChatListActivity",
            "invite !! : invitedUserId: $invitedUserId, currentUserId: ${getCurrentUserID()}"
        )

        userDB.child(invitedUserId)
            .child("invitedBy")
            .child("invite")
            .child(getCurrentUserID())
            .setValue(true)

        saveMatchIfOtherLikeMe(invitedUserId)

        Toast.makeText(this, "Invite 하셨습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun saveMatchIfOtherLikeMe(invitedUserId: String) {
        val invitedUserDB = userDB.child(getCurrentUserID()).child("invitedBy").child("invite")
            .child(invitedUserId)
        invitedUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    userDB.child(getCurrentUserID())
                        .child("invitedBy")
                        .child("match")
                        .child(invitedUserId)
                        .setValue(true)

                    userDB.child(invitedUserId)
                        .child("invitedBy")
                        .child("match")
                        .child(getCurrentUserID())
                        .setValue(true)
                }
                Log.d("ChatListActivitiy", "match!! $invitedUserId <-> ${getCurrentUserID()}")
            }

            override fun onCancelled(error: DatabaseError) {}

        })



    }
}