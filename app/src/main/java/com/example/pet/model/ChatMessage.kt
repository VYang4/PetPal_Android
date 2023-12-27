package com.example.pet.model

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    var senderId: String = "",
    var text: String = "",
    var time: Long = 0
) {
    // In Kotlin, isMine doesn't need to be a field with a getter
    // It can simply be a property with a custom getter
    val isMine: Boolean
        get() = senderId == FirebaseAuth.getInstance().currentUser?.uid

    // Method to convert the timestamp into a human-readable time
    fun convertTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(time)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }
}
