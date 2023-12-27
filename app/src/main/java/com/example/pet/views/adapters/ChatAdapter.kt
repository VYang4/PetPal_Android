package com.example.pet.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pet.R
import com.example.pet.databinding.RowChatBinding
import com.example.pet.model.ChatMessage

class ChatAdapter(
    private val chatMessageList: List<ChatMessage>,
    private val context: Context
) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowChatBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_chat,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chatMessage = chatMessageList[position]
        holder.binding.chatMessage = chatMessage
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = chatMessageList.size

    class MyViewHolder(val binding: RowChatBinding) : RecyclerView.ViewHolder(binding.root)
}
