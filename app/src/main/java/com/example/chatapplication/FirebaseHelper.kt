package com.example.chatapplication.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// Provides access to Firebase Authentication and Firebase Realtime Database.

object FirebaseHelper {

    // Firebase Authentication instance, used for user authentication (login, signup, logout)
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Firebase Database reference, used to read and write data in Realtime Database
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
}