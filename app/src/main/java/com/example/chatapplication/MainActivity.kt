package com.example.chatapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var currentUsername: TextView
    private lateinit var currentUserProfile: ImageView
    private lateinit var btnLogout: Button

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

        btnLogout.setOnClickListener {
            FirebaseHelper.auth.signOut()
            startActivity(Intent(this@MainActivity, LogIn::class.java))
            finish()
        }

        val currentUserId = FirebaseHelper.auth.currentUser?.uid

        currentUserId?.let { uid ->
            FirebaseHelper.database.child("user").child(uid).get().addOnSuccessListener {
                currentUsername.text = it.child("name").value as? String ?: "User"
                val profileImage = it.child("profileImage").value as? String
                ImageUtils.decodeBase64(profileImage)?.let { bitmap ->
                    Glide.with(this).load(bitmap).into(currentUserProfile)
                } ?: currentUserProfile.setImageResource(R.drawable.default_profile)
            }
        }

        FirebaseHelper.database.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (FirebaseHelper.auth.currentUser?.uid != currentUser?.uid) {
                        currentUser?.let { userList.add(it) }
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreatePanelMenu(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            FirebaseHelper.auth.signOut()
            startActivity(Intent(this@MainActivity, LogIn::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
