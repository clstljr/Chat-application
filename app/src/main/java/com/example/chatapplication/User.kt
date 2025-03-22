package com.example.chatapplication

/**
 * Data class representing a user in the chat application.
 *
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property uid The unique identifier of the user.
 * @property profileImage The Base64-encoded profile image of the user.
 */
class User {
    var name: String? = null // Stores the user's name
    var email: String? = null // Stores the user's email address
    var uid: String? = null // Stores the unique user ID
    var profileImage: String? = null // Stores the user's profile image in Base64 format

    // Empty constructor required for Firebase data retrieval
    constructor() {}

    // Constructor to initialize a User object with details
    constructor(name: String?, email: String?, uid: String?, profileImage: String?) {
        this.name = name
        this.email = email
        this.uid = uid
        this.profileImage = profileImage
    }
}
