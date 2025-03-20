package com.example.chatapplication

class Message {
    var message: String? = null
    var senderId: String? = null
    var senderProfile: String? = null // New field for profile picture

    constructor() {}

    constructor(message: String?, senderId: String?, senderProfile: String?) {
        this.message = message
        this.senderId = senderId
        this.senderProfile = senderProfile
    }
}
