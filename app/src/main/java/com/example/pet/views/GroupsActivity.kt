package com.example.pet.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pet.R
import com.example.pet.databinding.ActivityGroupsBinding
import com.example.pet.model.ChatGroup
import com.example.pet.viewmodel.MyViewModel
import com.example.pet.views.adapters.GroupAdapter

class GroupsActivity : AppCompatActivity() {
    private lateinit var chatGroupArrayList: ArrayList<ChatGroup>
    private lateinit var recyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var binding: ActivityGroupsBinding
    private lateinit var myViewModel: MyViewModel

    // Dialog
    private lateinit var chatGroupDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_groups)

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        recyclerView = binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GroupsActivity)
        }

        myViewModel.getGroupList().observe(this) { chatGroups ->
            chatGroupArrayList = ArrayList(chatGroups)
            groupAdapter = GroupAdapter(chatGroupArrayList)
            recyclerView.adapter = groupAdapter
            groupAdapter.notifyDataSetChanged()
        }

        binding.fab.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        chatGroupDialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(LayoutInflater.from(this@GroupsActivity)
                .inflate(R.layout.dialog_layout, null))
            show()
        }

        val submit: Button = chatGroupDialog.findViewById(R.id.submit_btn)
        val edt: EditText = chatGroupDialog.findViewById(R.id.chat_group_edt)

        submit.setOnClickListener {
            val groupName = edt.text.toString()
            Toast.makeText(this, "Your Chat Group: $groupName", Toast.LENGTH_SHORT).show()
            myViewModel.createNewGroup(groupName)
            chatGroupDialog.dismiss()
        }
    }
}
