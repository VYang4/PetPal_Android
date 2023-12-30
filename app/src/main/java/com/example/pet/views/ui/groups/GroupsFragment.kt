package com.example.pet.views.ui.groups

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pet.R
import com.example.pet.databinding.FragmentGroupsBinding
import com.example.pet.model.ChatGroup
import com.example.pet.viewmodel.MyViewModel
import com.example.pet.views.LoginActivity
import com.example.pet.views.adapters.GroupAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var chatGroupArrayList: ArrayList<ChatGroup>
    private lateinit var recyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        // chatGroupArrayList = arrayListOf<ChatGroup>()
        // groupAdapter = GroupAdapter(chatGroupArrayList)

        recyclerView = binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            // adapter = groupAdapter
        }

        chatGroupArrayList = arrayListOf() // Initialize with empty list
        groupAdapter = GroupAdapter(chatGroupArrayList)
        recyclerView.adapter = groupAdapter

        myViewModel.getGroupList().observe(viewLifecycleOwner) { chatGroups ->
            chatGroupArrayList.clear()
            chatGroupArrayList.addAll(chatGroups)
            groupAdapter.notifyDataSetChanged() // This will refresh the RecyclerView with new data
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
//        myViewModel.getGroupList().observe(viewLifecycleOwner) { chatGroups ->
//            Log.d("GroupsFragment", "Number of groups: ${chatGroups.size}")
//            chatGroupArrayList.clear()
//            chatGroupArrayList.addAll(chatGroups)
//            groupAdapter.notifyDataSetChanged()
//        }
    }

    private fun showDialog() {
        val chatGroupDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_layout)
            show()
        }

        chatGroupDialog.findViewById<Button>(R.id.submit_btn).setOnClickListener {
            val groupName = chatGroupDialog.findViewById<EditText>(R.id.chat_group_edt).text.toString()

            getCurrentLocation { location ->
                // Create group with location data
                val group = ChatGroup(groupName, location.latitude, location.longitude)
                myViewModel.createNewGroup(group)
                Toast.makeText(context, "Group '$groupName' created at your current location", Toast.LENGTH_SHORT).show()
            }
            chatGroupDialog.dismiss()
        }
    }

    private fun getCurrentLocation(callback: (Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                callback(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}