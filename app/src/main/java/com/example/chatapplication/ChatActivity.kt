package com.example.chatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.utils.FirebaseHelper
import com.example.chatapplication.utils.ImageUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var btnGoback: ImageView
    private lateinit var senderProfile: ImageView
    private lateinit var senderUsername: TextView
//o
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseHelper.auth.currentUser?.uid

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
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Fetch sender profile and username
        receiverUid?.let { uid ->
            FirebaseHelper.database.child("user").child(uid).get().addOnSuccessListener {
                senderUsername.text = it.child("name").value as? String ?: "Unknown"
                val profileImage = it.child("profileImage").value as? String
                ImageUtils.decodeBase64(profileImage)?.let { bitmap ->
                    Glide.with(this).load(bitmap).into(senderProfile)
                } ?: senderProfile.setImageResource(R.drawable.default_profile)
            }
        }

        // Load messages
        FirebaseHelper.database.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    snapshot.children.mapNotNullTo(messageList) { it.getValue(Message::class.java) }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        // Send message
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            if (message.isNotEmpty() && senderUid != null) {
                FirebaseHelper.database.child("user").child(senderUid).get().addOnSuccessListener {
                    val senderProfile = it.child("profileImage").value as? String ?: ""
                    val messageObject = Message(message, senderUid, senderProfile)
                    FirebaseHelper.database.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject).addOnSuccessListener {
                            FirebaseHelper.database.child("chats").child(receiverRoom!!).child("messages").push()
                                .setValue(messageObject)
                        }
                }
                messageBox.setText("")
            }
        }
    }
}