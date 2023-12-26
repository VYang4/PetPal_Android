package com.example.pet.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pet.R
import com.example.pet.databinding.ActivityGroupsBinding
import com.example.pet.model.ChatGroup
import com.example.pet.viewmodel.MyViewModel
import com.example.pet.views.adapters.GroupAdapter


class GroupsActivity : AppCompatActivity() {
    private var chatGroupArrayList: ArrayList<ChatGroup>? = null
    private var recyclerView: RecyclerView? = null
    private var groupAdapter: GroupAdapter? = null
    private var binding: ActivityGroupsBinding? = null
    private var myViewModel: MyViewModel? = null

    // Dialog
    private var chatGroupDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_groups
        )

        // Define the ViewModel
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)


        // RecycleView With Data Binding
        recyclerView = binding.recyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)


        // Setup an observer to listen for changes in a "Live Data" object
        myViewModel.getGroupList().observe(this, object : Observer<List<ChatGroup?>?> {
            override fun onChanged(chatGroups: List<ChatGroup>) {
                // the updated data is received as "chatGroups" parameter in onChanged()
                chatGroupArrayList = ArrayList()
                chatGroupArrayList!!.addAll(chatGroups)
                groupAdapter = GroupAdapter(chatGroupArrayList!!)
                recyclerView!!.adapter = groupAdapter
                groupAdapter!!.notifyDataSetChanged()
            }
        })
        binding.fab.setOnClickListener { showDialog() }
    }

    fun showDialog() {
        chatGroupDialog = Dialog(this)
        chatGroupDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = LayoutInflater.from(this)
            .inflate(
                R.layout.dialog_layout,
                null
            )
        chatGroupDialog!!.setContentView(view)
        chatGroupDialog!!.show()
        val submit = view.findViewById<Button>(R.id.submit_btn)
        val edt = view.findViewById<EditText>(R.id.chat_group_edt)
        submit.setOnClickListener {
            val groupName = edt.text.toString()
            Toast.makeText(
                this@GroupsActivity,
                "Your Chat Group: $groupName", Toast.LENGTH_SHORT
            ).show()
            myViewModel.createNewGroup(groupName)
            chatGroupDialog!!.dismiss()
        }
    }
}