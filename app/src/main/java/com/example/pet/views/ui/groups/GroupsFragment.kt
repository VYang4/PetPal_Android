package com.example.pet.views.ui.groups

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pet.R
import com.example.pet.databinding.FragmentGroupsBinding
import com.example.pet.model.ChatGroup
import com.example.pet.viewmodel.MyViewModel
import com.example.pet.views.LoginActivity
import com.example.pet.views.adapters.GroupAdapter

class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        val chatGroupArrayList = arrayListOf<ChatGroup>()
        val groupAdapter = GroupAdapter(chatGroupArrayList)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }

        myViewModel.getGroupList().observe(viewLifecycleOwner) { chatGroups ->
            chatGroupArrayList.clear()
            chatGroupArrayList.addAll(chatGroups)
            groupAdapter.notifyDataSetChanged()
        }

        binding.fab.setOnClickListener { showDialog() }
        binding.returnButton.setOnClickListener {
            myViewModel.signOut()
        }

        myViewModel.userLiveData.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser == null) {
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun showDialog() {
        val chatGroupDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_layout)
            show()
        }

        chatGroupDialog.findViewById<Button>(R.id.submit_btn).setOnClickListener {
            val groupName = chatGroupDialog.findViewById<EditText>(R.id.chat_group_edt).text.toString()
            Toast.makeText(context, "Your Chat Group: $groupName", Toast.LENGTH_SHORT).show()
            myViewModel.createNewGroup(groupName)
            chatGroupDialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}