package com.example.chatapplication

class Message {
    var message: String? = null // Stores the message text
    var senderId: String? = null // Stores the sender's user ID
    var senderProfile: String? = null // Stores the sender's profile image in Base64 format

    constructor() {}

    // Constructor to initialize a Message object with content
    constructor(message: String?, senderId: String?, senderProfile: String?) {
        this.message = message
        this.senderId = senderId
        this.senderProfile = senderProfile
    }
}