package com.jinny.plancast.presentation.todo.list

import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.domain.usecase.todoUseCase.DeleteAllToDoItemUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.GetToDoListUseCase
import com.jinny.plancast.domain.usecase.todoUseCase.UpdateToDoUseCase
import com.jinny.plancast.presentation.BaseViewModel
import kotlinx.coroutines.*

class ListViewModel(
    private val getToDoListUseCase: com.jinny.plancast.domain.usecase.todoUseCase.GetToDoListUseCase,
    private val updateToDoUseCase: com.jinny.plancast.domain.usecase.todoUseCase.UpdateToDoUseCase,
    private val deleteAllToDoItemUseCase: com.jinny.plancast.domain.usecase.todoUseCase.DeleteAllToDoItemUseCase
): BaseViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: DatabaseReference = Firebase.database.reference.child("users")

    private var _toDoListLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)
    val toDoListLiveData: LiveData<ToDoListState> = _toDoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        Log.d("ListViewModel", "fetch data ! ")
        try {
            _toDoListLiveData.postValue(ToDoListState.Loading)
            val toDoList = getToDoListUseCase()
            _toDoListLiveData.postValue(ToDoListState.Suceess(toDoList))
        } catch (e: Exception) {
            _toDoListLiveData.postValue(ToDoListState.Error(e.message ?: "데이터를 불러오는데 실패했습니다"))
        }
    }

    fun updateEntity(toDoEntity: ToDoEntity) = viewModelScope.launch {
        try {
            updateToDoUseCase(toDoEntity)
        } catch (e: Exception) {
            _toDoListLiveData.postValue(ToDoListState.Error(e.message ?: "업데이트에 실패했습니다"))
        }
    }

    fun deleteAll() = viewModelScope.launch {
        try {
            _toDoListLiveData.postValue(ToDoListState.Loading)
            deleteAllToDoItemUseCase()
            val toDoList = getToDoListUseCase()
            _toDoListLiveData.postValue(ToDoListState.Suceess(toDoList))
        } catch (e: Exception) {
            _toDoListLiveData.postValue(ToDoListState.Error(e.message ?: "삭제에 실패했습니다"))
        }
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            return ""
        }
        return auth.currentUser!!.uid
    }


    fun saveUserName(name: String) {
        val userId = getCurrentUserID()
        val currentUserDB = userDB.child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        user["name"] = name
        currentUserDB.updateChildren(user)

        getUnSelectedUsers()
    }

    fun getUnSelectedUsers() {
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child("userId").value != getCurrentUserID()
                    && snapshot.child("likedBy").child("like").hasChild(getCurrentUserID()).not()
                    && snapshot.child("likedBy").child("disLike").hasChild(getCurrentUserID()).not()
                ) {

                    val userId = snapshot.child("userId").value.toString()
                    var name = "undecided"
                    if (snapshot.child("name").value != null) {
                        name = snapshot.child("name").value.toString()
                    }

//                    cardItems.add(CardItem(userId, name))
//                    adapter.submitList(cardItems)
//                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
//                cardItems.find { it.userId == dataSnapshot.key }?.let {
//                    it.name = dataSnapshot.child("name").value.toString()
//                }
//                adapter.submitList(cardItems)
//                adapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}
