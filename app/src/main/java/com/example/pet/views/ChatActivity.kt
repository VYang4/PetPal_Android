package com.example.pet.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pet.R
import com.example.pet.databinding.ActivityChatBinding
import com.example.pet.model.ChatMessage
import com.example.pet.viewmodel.MyViewModel
import com.example.pet.views.adapters.ChatAdapter
import androidx.lifecycle.Observer

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var myViewModel: MyViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: ChatAdapter
    private lateinit var messagesList: List<ChatMessage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        recyclerView = binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            setHasFixedSize(true)
        }

        val groupName = intent.getStringExtra("GROUP_NAME")

        myViewModel.getMessagesLiveData(groupName ?: "").observe(this) { chatMessages ->
            messagesList = ArrayList(chatMessages)
            myAdapter = ChatAdapter(messagesList, applicationContext)
            recyclerView.adapter = myAdapter
            myAdapter.notifyDataSetChanged()

            val latestPosition = myAdapter.itemCount - 1
            if (latestPosition > 0) {
                recyclerView.smoothScrollToPosition(latestPosition)
            }
        }

        binding.vModel = myViewModel

        binding.sendBTN.setOnClickListener {
            val msg = binding.edittextChatMessage.text.toString()
            myViewModel.sendMessage(msg, groupName ?: "")
            binding.edittextChatMessage.text.clear()
        }

        binding.backButton.setOnClickListener {
            finish() // Closes the ChatActivity and returns to the previous activity
        }
    }
}
