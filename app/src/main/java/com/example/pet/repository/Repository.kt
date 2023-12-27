package com.example.pet.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.pet.model.ChatGroup
import com.example.pet.model.ChatMessage
import com.example.pet.views.GroupsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Repository {
    // LiveData properties for chat groups and messages
    val chatGroupMutableLiveData = MutableLiveData<List<ChatGroup>>()
    val messagesLiveData = MutableLiveData<List<ChatMessage>>()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference // The Root Reference

    // Auth
    fun firebaseAnonymousAuth(context: Context) {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(context, GroupsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Getting Current User ID
    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    // SignOut Functionality
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    // Getting Chat Groups available from Firebase Realtime DB
    init {
        val groupsList = mutableListOf<ChatGroup>()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupsList.clear()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.key?.let { key ->
                        groupsList.add(ChatGroup(key))
                    }
                }
                chatGroupMutableLiveData.postValue(groupsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Creating a new group
    fun createNewChatGroup(groupName: String) {
        reference.child(groupName).setValue(groupName)
    }

    // Getting Messages LiveData
    fun getMessagesLiveData(groupName: String) {
        val groupReference = database.reference.child(groupName)
        val messagesList = mutableListOf<ChatMessage>()

        groupReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(ChatMessage::class.java)?.let { message ->
                        messagesList.add(message)
                    }
                }
                messagesLiveData.postValue(messagesList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Sending Messages
    fun sendMessage(messageText: String, chatGroup: String) {
        val ref = database.reference.child(chatGroup)

        if (messageText.trim().isNotEmpty()) {
            val msg = ChatMessage(
                FirebaseAuth.getInstance().currentUser?.uid ?: "",
                messageText,
                System.currentTimeMillis()
            )

            val randomKey = ref.push().key
            randomKey?.let {
                ref.child(it).setValue(msg)
            }
        }
    }
}
