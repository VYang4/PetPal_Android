//package com.example.pet.views
//
//import android.app.Dialog
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.Window
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.pet.R
//import com.example.pet.databinding.ActivityGroupsBinding
//import com.example.pet.model.ChatGroup
//import com.example.pet.viewmodel.MyViewModel
//import com.example.pet.views.adapters.GroupAdapter
//
//class GroupsActivity : AppCompatActivity() {
//    private lateinit var chatGroupArrayList: ArrayList<ChatGroup>
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var groupAdapter: GroupAdapter
//    private lateinit var binding: ActivityGroupsBinding
//    private lateinit var myViewModel: MyViewModel
//
//    // Dialog
//    private lateinit var chatGroupDialog: Dialog
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.fragment_groups)
//
//        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
//
//        recyclerView = binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(this@GroupsActivity)
//        }
//
//        chatGroupArrayList = arrayListOf() // Initialize with empty list
//        groupAdapter = GroupAdapter(chatGroupArrayList)
//        recyclerView.adapter = groupAdapter
//
//        myViewModel.getGroupList().observe(this) { chatGroups ->
//            chatGroupArrayList.clear()
//            chatGroupArrayList.addAll(chatGroups)
//            groupAdapter.notifyDataSetChanged()
//        }
//
//        binding.fab.setOnClickListener { showDialog() }
//
//        // This observation is set up once, typically in the onCreate method of your activity.
//        // After setup, the LiveData will automatically notify the observer whenever its data changes.
//        // When userLiveData changes (for instance, when a user signs out and userLiveData is set to null),
//        // the observer's code block is executed. In your snippet, this means that if firebaseUser is null,
//        // the app will navigate back to LoginActivity.
//        myViewModel.userLiveData.observe(this) { firebaseUser ->
//            if (firebaseUser == null) {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            }
//        }
//
//        binding.returnButton.setOnClickListener {
//            myViewModel.signOut()
//        }
//    }
//
//    private fun showDialog() {
//        chatGroupDialog = Dialog(this).apply {
//            requestWindowFeature(Window.FEATURE_NO_TITLE)
//            setContentView(LayoutInflater.from(this@GroupsActivity)
//                .inflate(R.layout.dialog_layout, null))
//            show()
//        }
//
//        val submit: Button = chatGroupDialog.findViewById(R.id.submit_btn)
//        val edt: EditText = chatGroupDialog.findViewById(R.id.chat_group_edt)
//
//        submit.setOnClickListener {
//            val groupName = edt.text.toString()
//            Toast.makeText(this, "Your Chat Group: $groupName", Toast.LENGTH_SHORT).show()
//            myViewModel.createNewGroup(groupName)
//            chatGroupDialog.dismiss()
//        }
//    }
//}
