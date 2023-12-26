package com.example.pet.model

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


class ChatMessage {
    var senderId: String? = null
    var text: String? = null
    var time: Long = 0
    var isMine = false

    constructor(senderId: String?, text: String?, time: Long) {
        this.senderId = senderId
        this.text = text
        this.time = time
    }

    constructor()

    fun isMine(): Boolean {
        return if (senderId == FirebaseAuth.getInstance().currentUser!!.uid) {
            true
        } else false
    }

    fun setMine(mine: Boolean) {
        isMine = mine
    }

    fun convertTime(): String {
        val sdf = SimpleDateFormat("HH:mm")
        val date = Date(time)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }
}

