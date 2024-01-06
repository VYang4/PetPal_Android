package com.example.pet.model

//class ChatGroup(val groupName: String, val latitude: Double, val longitude: Double)
//getters and setters are automatically generated in Kotlin

class ChatGroup() {
    var name: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var creatorId: String = ""

    constructor(name: String, latitude: Double, longitude: Double, creatorId: String?) : this() {
        this.name = name
        this.latitude = latitude
        this.longitude = longitude
        if (creatorId != null) {
            this.creatorId = creatorId
        }
    }
}
