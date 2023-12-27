package com.example.pet.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pet.repository.Repository
import com.example.pet.model.ChatGroup
import com.example.pet.model.ChatMessage

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository()

    // Auth
    fun signUpAnonymousUser() {
        val context: Context = getApplication()
        repository.firebaseAnonymousAuth(context)
    }

    fun getCurrentUserId(): String? {
        return repository.getCurrentUserId()
    }

    fun signOut() {
        repository.signOut()
    }

    // Getting Chat Groups
    fun getGroupList(): MutableLiveData<List<ChatGroup>> {
        return repository.chatGroupMutableLiveData
    }

    fun createNewGroup(groupName: String) {
        repository.createNewChatGroup(groupName)
    }

    // Messages
    fun getMessagesLiveData(groupName: String) {
        return repository.getMessagesLiveData(groupName)
    }

    fun sendMessage(msg: String, chatGroup: String) {
        repository.sendMessage(msg, chatGroup)
    }
}
