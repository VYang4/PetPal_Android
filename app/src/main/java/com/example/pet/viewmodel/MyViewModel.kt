package com.example.pet.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pet.repository.Repository
import com.example.pet.model.ChatGroup
import com.example.pet.model.ChatMessage
import com.google.firebase.auth.FirebaseUser

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository()

    // Auth
    val userLiveData: LiveData<FirebaseUser?> = repository.userLiveData
    val authErrorLiveData: LiveData<String?> = repository.authErrorLiveData

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun signInWithEmail() {
        val emailStr = email.value ?: return
        val passwordStr = password.value ?: return
        repository.signInWithEmail(emailStr, passwordStr)
    }

    fun signUpWithEmail(email: String, password: String) {
        repository.signUpWithEmail(email, password)
    }

    fun getCurrentUserId(): String? {
        return repository.getCurrentUserId()
    }

    fun signOut() {
        repository.signOut()
    }

    // Getting Chat Groups
    fun getGroupList(): MutableLiveData<List<ChatGroup>> {
        repository.getChatGroups()
        return repository.chatGroupMutableLiveData
    }

    fun createNewGroup(groupName: String) {
        repository.createNewChatGroup(groupName)
    }

    // Messages
    fun getMessagesLiveData(groupName: String): MutableLiveData<List<ChatMessage>> {
        repository.getMessagesLiveData(groupName)
        return repository.messagesLiveData
    }

    fun sendMessage(msg: String, chatGroup: String) {
        repository.sendMessage(msg, chatGroup)
    }
}
