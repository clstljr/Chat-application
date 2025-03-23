package com.example.chatapplication

class User {
    var name: String? = null // Stores the user's name
    var email: String? = null // Stores the user's email address
    var uid: String? = null // Stores the unique user ID
    var profileImage: String? = null // Stores the user's profile image in Base64 format

    constructor() {}

    // Constructor to initialize a User object with details
    constructor(name: String?, email: String?, uid: String?, profileImage: String?) {
        this.name = name
        this.email = email
        this.uid = uid
        this.profileImage = profileImage
    }
}