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

// GroupViewHolder: holds references to the views within the item layout,
// which improves performance by avoiding unnecessary findViewById calls.
// Adapter: connects UI component to its data source, managing the creation and binding of view items
class GroupAdapter(private val groupArrayList: ArrayList<ChatGroup>) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        // Inflate the item layout using DataBindingUtil
        val binding: ItemCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_card,
            parent,
            false
        )
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        // Bind data to the ViewHolder
        val currentUser: ChatGroup = groupArrayList[position]
        holder.itemCardBinding.chatGroup = currentUser
    }

    override fun getItemCount(): Int {
        // Return the size of the dataset
        return groupArrayList.size
    }

    inner class GroupViewHolder(val itemCardBinding: ItemCardBinding) : RecyclerView.ViewHolder(itemCardBinding.root) {
        // Cache references to the views within an item layout

        init {
            // Set an OnClickListener on the root view
            itemCardBinding.root.setOnClickListener {
                val position = adapterPosition
                val clickedChatGroup = groupArrayList[position]

                // Start the ChatActivity with the group name as an extra
                val intent = Intent(it.context, ChatActivity::class.java)
                intent.putExtra("GROUP_NAME", clickedChatGroup.groupName)
                it.context.startActivity(intent)
            }
        }
    }
}
