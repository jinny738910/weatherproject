package com.jinny.plancast.presentation.chat.chatroom

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
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
import com.jinny.plancast.presentation.chat.chatview.ChatViewItem
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class ChatRoomActivity : BaseActivity<ChatRoomViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: DatabaseReference = Firebase.database.reference
    private var chatDB: DatabaseReference = Firebase.database.reference.child("chats")
    private val chatRoomAdapter = ChatRoomAdapter()
    private val chatRoomInfos = mutableListOf<ChatRoomInfo>()
    private val chatRoomItems = mutableListOf<MessageItem>()
    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val chatRoomInfo = snapshot.getValue(ChatRoomInfo::class.java)
            chatRoomInfo ?: return

            chatRoomInfos.add(chatRoomInfo)
            chatRoomAdapter.setToChatRoom(chatRoomInfos, chatRoomClickListener = { ChatRoomInfo ->
            })
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    override val viewModel: ChatRoomViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        getChatViewItem()
//        getChatRoomInfo()

        setContent {
            MaterialTheme {
                ChatRoomScreen(
                    chatTitle= "채팅방" ,
                    initialMessages= emptyList()
                )
            }
        }

    }

    fun getChatViewItem() {
        // Intent에서 Parcelable 객체를 꺼냅니다. (Key: "chat_item")
        val chatItem: ChatViewItem? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33) 이상
            intent.getParcelableExtra("chat_item", ChatViewItem::class.java)
        } else {
            // 하위 버전
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("chat_item")
        }

        chatItem?.let {
            // 성공적으로 데이터를 받았습니다.
            Log.d("ChatRoomActivity", "받은 사용자 ID: ${it.userId}")
            // 받은 데이터를 사용하여 화면 구성 또는 로직 처리

        } ?: run {
            // 데이터가 null일 경우의 예외 처리
            Log.e("ChatRoomActivity", "ChatViewItem 데이터를 받지 못했습니다.")
            finish() // 액티비티 종료
        }
    }

    fun getChatRoomInfo() {
        // Intent에서 Parcelable 객체를 꺼냅니다. (Key: "chat_item")
        val chatRoomInfo: ChatRoomInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33) 이상
            intent.getParcelableExtra("chat_item", ChatRoomInfo::class.java)
        } else {
            // 하위 버전
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("chat_item")
        }

        chatRoomInfo?.let {
            // 성공적으로 데이터를 받았습니다.
            Log.d("ChatRoomActivity", "받은 사용자 ID: ${it.userId}")
            // 받은 데이터를 사용하여 화면 구성 또는 로직 처리

        } ?: run {
            // 데이터가 null일 경우의 예외 처리
            Log.e("ChatRoomActivity", "ChatViewItem 데이터를 받지 못했습니다.")
            finish() // 액티비티 종료
        }
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

    fun getChatRoomUsers() {
        userDB.addChildEventListener(object : ChildEventListener {
            @SuppressLint("NotifyDataSetChanged")
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

                    chatRoomInfos.add(ChatRoomInfo(userId, name, message, "", "fasf", "Afdadf", "dfadsf", "sdafsd"))
                    chatRoomAdapter.setToChatRoom(chatRoomInfos, chatRoomClickListener = { ChatRoomInfo ->
                    })
                    chatRoomAdapter.notifyDataSetChanged()

//                    cardItems.add(CardItem(userId, name))
//                    adapter.submitList(cardItems)
//                    adapter.notifyDataSetChanged()
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                chatRoomInfos.find { it.userId == dataSnapshot.key }?.let {
                }
                chatRoomAdapter.setToChatRoom(chatRoomInfos, chatRoomClickListener = { chatRoomItem ->
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