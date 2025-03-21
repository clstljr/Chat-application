package com.example.chatapplication

class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var profileImage: String? = null

    constructor() {}

    constructor(name: String?, email: String?, uid: String?, profileImage: String?) {
        this.name = name
        this.email = email
        this.uid = uid
        this.profileImage = profileImage
    }
}
