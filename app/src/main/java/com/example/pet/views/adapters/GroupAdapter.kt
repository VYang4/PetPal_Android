package com.example.pet.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pet.R
import com.example.pet.databinding.ItemCardBinding
import com.example.pet.model.ChatGroup
import com.example.pet.views.ChatActivity

class GroupAdapter(private val groupArrayList: ArrayList<ChatGroup>) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding: ItemCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_card,
            parent,
            false
        )
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val chatGroup = groupArrayList[position]
        holder.itemCardBinding.chatGroup = chatGroup
        holder.itemCardBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return groupArrayList.size
    }

    inner class GroupViewHolder(val itemCardBinding: ItemCardBinding) : RecyclerView.ViewHolder(itemCardBinding.root) {

        init {
            itemCardBinding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedChatGroup = groupArrayList[position]
                    val intent = Intent(it.context, ChatActivity::class.java)
                    intent.putExtra("GROUP_NAME", clickedChatGroup.name)
                    it.context.startActivity(intent)
                }
            }
        }
    }

}
