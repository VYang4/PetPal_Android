package com.example.pet.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.pet.model.ChatGroup
import com.example.pet.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import androidx.fragment.app.Fragment

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class Repository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val userLiveData = MutableLiveData<FirebaseUser?>()
    val authErrorLiveData = MutableLiveData<String?>()
    // LiveData properties for chat groups and messages
    val chatGroupMutableLiveData = MutableLiveData<List<ChatGroup>>()
    val messagesLiveData = MutableLiveData<List<ChatMessage>>()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference // The Root Reference

    // Auth
    fun signInWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userLiveData.postValue(firebaseAuth.currentUser)
            } else {
                authErrorLiveData.postValue(task.exception?.message)
            }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userLiveData.postValue(firebaseAuth.currentUser)
            } else {
                authErrorLiveData.postValue(task.exception?.message)
            }
        }
    }

    // SignOut Functionality
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        userLiveData.postValue(null)
    }

    // Getting Chat Groups available from Firebase Realtime DB
    fun getChatGroups(): MutableLiveData<List<ChatGroup>> {
        val groupsList = mutableListOf<ChatGroup>()
        reference.child("groups").addValueEventListener(object : ValueEventListener {  // Assuming the child name is "chatGroups"
            override fun onDataChange(snapshot: DataSnapshot) {
                groupsList.clear()
                for (dataSnapshot in snapshot.children) {
                    val group = dataSnapshot.getValue(ChatGroup::class.java)
                    group?.let {
                        groupsList.add(it)
                    }
                }
                chatGroupMutableLiveData.postValue(groupsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.d("Repository", "Error fetching groups: ${error.message}")
            }
        })
        return chatGroupMutableLiveData
    }



    // Creating a new group
    fun createNewChatGroup(group: ChatGroup) {
        val groupMap = mapOf(
            "name" to group.name,
            "latitude" to group.latitude,
            "longitude" to group.longitude,
            "creatorId" to group.creatorId
            // ... other properties if needed
        )
        reference.child("groups").child(group.name).setValue(groupMap)
    }

    fun deleteGroup(group: ChatGroup, currentUserId: String): Result<Unit> {
        return if (group.creatorId == currentUserId) {
            try {
                reference.child("groups").child(group.name).removeValue()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("Unauthorized to delete this group"))
        }
    }

    // Getting Messages LiveData
    fun getMessagesLiveData(groupName: String): MutableLiveData<List<ChatMessage>> {
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
        return messagesLiveData
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
