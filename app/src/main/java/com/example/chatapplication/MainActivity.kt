package com.example.chatapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.utils.FirebaseHelper
import com.example.chatapplication.utils.ImageUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView // List of users
    private lateinit var userList: ArrayList<User> // Stores user data
    private lateinit var adapter: UserAdapter // Adapter to display users
    private lateinit var currentUsername: TextView // Displays current user's name
    private lateinit var currentUserProfile: ImageView // Displays current user's profile picture
    private lateinit var btnLogout: Button // Logout button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        currentUsername = findViewById(R.id.currentusername)
        currentUserProfile = findViewById(R.id.currentuserprofile)
        btnLogout = findViewById(R.id.btn_logout)

        // Fetch current user's profile and name from Firebase
        val currentUserId = FirebaseHelper.auth.currentUser?.uid
        currentUserId?.let { uid ->
            FirebaseHelper.database.child("user").child(uid).get().addOnSuccessListener {
                currentUsername.text = it.child("name").value as? String ?: "User"
                val profileImage = it.child("profileImage").value as? String
                ImageUtils.decodeBase64(profileImage)?.let { bitmap ->
                    Glide.with(this).load(bitmap).into(currentUserProfile)
                }
            }
        }

        // Load user list from Firebase
        FirebaseHelper.database.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (FirebaseHelper.auth.currentUser?.uid != currentUser?.uid) {
                        currentUser?.let { userList.add(it) } // Add all users except current user
                    }
                }
                adapter.notifyDataSetChanged() // Refresh user list
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        // Logout functionality
        btnLogout.setOnClickListener {
            val alertMessage = AlertDialog.Builder(this)
            alertMessage.setTitle("Logout")
            alertMessage.setMessage("Are you sure you want to log out?")
            alertMessage.setCancelable(false)

            alertMessage.setPositiveButton("Yes") { _, _ ->
                FirebaseHelper.auth.signOut()
                startActivity(Intent(this, LogIn::class.java))
                finish()
            }
            alertMessage.setNeutralButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            alertMessage.create().show()
        }
    }
}