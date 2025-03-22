package com.example.chatapplication

/**
 * Data class representing a chat message.
 *
 * @property message The text content of the message.
 * @property senderId The unique identifier of the sender.
 * @property senderProfile The Base64-encoded profile image of the sender.
 */
class Message {
    var message: String? = null // Stores the message text
    var senderId: String? = null // Stores the sender's user ID
    var senderProfile: String? = null // Stores the sender's profile image in Base64 format

    // Empty constructor required for Firebase data retrieval
    constructor() {}

    // Constructor to initialize a Message object with content
    constructor(message: String?, senderId: String?, senderProfile: String?) {
        this.message = message
        this.senderId = senderId
        this.senderProfile = senderProfile
    }
}
