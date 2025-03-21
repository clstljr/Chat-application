package com.example.chatapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var btnGoback: ImageView
    private lateinit var senderProfile: ImageView
    private lateinit var senderUsername: TextView

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.hide()

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        btnGoback = findViewById(R.id.btn_goback)
        senderProfile = findViewById(R.id.senderuserprofile)
        senderUsername = findViewById(R.id.senderusername)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        btnGoback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Fetch sender profile and username
        if (receiverUid != null) {
            mDbRef.child("user").child(receiverUid).get().addOnSuccessListener {
                val name = it.child("name").value as? String
                val profileImage = it.child("profileImage").value as? String

                senderUsername.text = name ?: "Unknown"

                if (!profileImage.isNullOrEmpty()) {
                    Glide.with(this)
                        .asBitmap()
                        .load(decodeBase64(profileImage))
                        .into(senderProfile)
                } else {
                    senderProfile.setImageResource(R.drawable.default_profile) // Default profile
                }
            }
        }

        // Logic for adding data to recycler view
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        // Adding the message to database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val senderUid = FirebaseAuth.getInstance().currentUser?.uid

            if (message.isNotEmpty() && senderUid != null) {
                // Fetch sender's profile from Firebase
                mDbRef.child("user").child(senderUid).get().addOnSuccessListener {
                    val senderProfile = it.child("profileImage").value as? String ?: ""

                    val messageObject = Message(message, senderUid, senderProfile) // Include profile

                    // Save to senderRoom
                    mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject).addOnSuccessListener {
                            // Save to receiverRoom
                            mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                                .setValue(messageObject)
                        }
                }
                messageBox.setText("")
            }
        }
    }

    private fun decodeBase64(encodedImage: String): Bitmap {
        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}
