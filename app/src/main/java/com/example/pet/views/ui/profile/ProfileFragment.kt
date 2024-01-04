package com.example.pet.views.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pet.databinding.FragmentProfileBinding
import com.example.pet.viewmodel.MyViewModel
import com.example.pet.views.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    // credentials
    private var currentUserEmail: String = ""
    private var currentUserName: String = ""
    private var petName: String = ""
    private var pet: String = ""
    private var petBreed: String = ""
    // firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    // fireStore
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var collectionReference: CollectionReference = db.collection("User")

    private lateinit var myViewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        currentUser?.let {
            currentUserEmail = it.email.toString()
            // Passwords cannot be retrieved from Firebase Authentication.
            // Display other user info.
            displayUserInfo(it.uid)
        }

        binding.saveButton.setOnClickListener {
            saveProfileChanges()
        }

        binding.signOutButton.setOnClickListener {
            myViewModel.signOut()
        }

        myViewModel.userLiveData.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser == null) {
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun displayUserInfo(userId: String) {
        collectionReference.document(userId).get().addOnSuccessListener { document ->
            if (document != null) {
                currentUserEmail = currentUser.email.toString()
                currentUserName = document.getString("userName") ?: ""
                petName = document.getString("petName") ?: ""
                pet = document.getString("pet") ?: ""
                petBreed = document.getString("petBreed") ?: ""

                // Set the fetched data to EditText fields
                binding.emailViewText.text = currentUserEmail
                binding.userNameEditText.setText(currentUserName)
                binding.petNameEditText.setText(petName)
                binding.petEditText.setText(pet)
                binding.petBreedEditText.setText(petBreed)
                // Set the image to ImageView if needed
            }
        }
    }

    private lateinit var updatedEmail: String
    private lateinit var updatedUserName: String
    private lateinit var updatedPetName: String
    private lateinit var updatedPet : String
    private lateinit var updatedPetBreed: String

    private fun saveProfileChanges() {
        updatedEmail = binding.emailViewText.text.toString()
        updatedUserName = binding.userNameEditText.text.toString()
        updatedPetName = binding.petNameEditText.text.toString()
        updatedPet = binding.petEditText.text.toString()
        updatedPetBreed = binding.petBreedEditText.text.toString()

        currentUser?.uid?.let { userId ->
            val userDocumentRef = collectionReference.document(userId)
            userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Document exists, proceed with update
                    updateUserDocument(userDocumentRef, userId)
                } else {
                    // Document does not exist, create a new one
                    createUserDocument(userDocumentRef, userId)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, "Error checking for user document: ${exception.message}", Toast.LENGTH_LONG).show()
                Log.e("ProfileFragment", "Error checking for user document", exception)
            }
        } ?: Toast.makeText(context, "User not logged in", Toast.LENGTH_LONG).show()
    }

    private fun createUserDocument(userDocumentRef: DocumentReference, userId: String) {
        // Same as your existing userUpdates map
        val userUpdates = hashMapOf(
            "userName" to updatedUserName,
            "petName" to updatedPetName,
            "pet" to updatedPet,
            "petBreed" to updatedPetBreed,
        )

        userDocumentRef.set(userUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Profile created successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to create profile: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUserDocument(userDocumentRef: DocumentReference, userId: String) {
        // Same as your existing userUpdates map
        val userUpdates = hashMapOf(
            "userName" to updatedUserName,
            "petName" to updatedPetName,
            "pet" to updatedPet,
            "petBreed" to updatedPetBreed,
        )

        userDocumentRef.update(userUpdates as Map<String, Any>).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to update profile: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}