package com.example.chatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
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

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView // Displays the list of messages
    private lateinit var messageBox: EditText // Input field for typing messages
    private lateinit var sendButton: ImageView // Button to send messages
    private lateinit var messageAdapter: MessageAdapter // Adapter for handling messages
    private lateinit var messageList: ArrayList<Message> // Stores messages in the chat
    private lateinit var senderProfile: ImageView // Displays the receiver's profile picture
    private lateinit var senderUsername: TextView // Displays the receiver's username
    private lateinit var btnGoBack: ImageView // Button to return to MainActivity

    private var receiverRoom: String? = null // Room identifier for the receiver
    private var senderRoom: String? = null // Room identifier for the sender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()

        val receiverUid = intent.getStringExtra("uid") // Get the receiver's user ID
        val senderUid = FirebaseHelper.auth.currentUser?.uid // Get the sender's user ID

        if (receiverUid != null && senderUid != null) {
            senderRoom = receiverUid + senderUid // Unique chat room ID for sender
            receiverRoom = senderUid + receiverUid // Unique chat room ID for receiver
        }

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        senderProfile = findViewById(R.id.senderuserprofile)
        senderUsername = findViewById(R.id.senderusername)
        btnGoBack = findViewById(R.id.btn_goback)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this) // Display messages in order
        chatRecyclerView.adapter = messageAdapter

        // Go back to MainActivity when the back button is clicked
        btnGoBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Load receiver's profile and username from Firebase
        receiverUid?.let { uid ->
            FirebaseHelper.database.child("user").child(uid).get().addOnSuccessListener {
                senderUsername.text = it.child("name").value as? String ?: "Unknown"
                val profileImage = it.child("profileImage").value as? String
                ImageUtils.decodeBase64(profileImage)?.let { bitmap ->
                    Glide.with(this).load(bitmap).into(senderProfile)
                } ?: senderProfile.setImageResource(R.drawable.default_profile)
            }
        }

        // Listen for incoming messages in the chat room
        senderRoom?.let { room ->
            FirebaseHelper.database.child("chats").child(room).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        snapshot.children.mapNotNullTo(messageList) { it.getValue(Message::class.java) }
                        messageAdapter.notifyDataSetChanged() // Refresh messages
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        // Send message when sendButton is clicked
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            if (message.isNotEmpty() && senderUid != null) {
                FirebaseHelper.database.child("user").child(senderUid).get().addOnSuccessListener {
                    val senderProfile = it.child("profileImage").value as? String ?: ""
                    val messageObject = Message(message, senderUid, senderProfile)

                    // Store message in both sender and receiver chat rooms
                    senderRoom?.let { sRoom ->
                        receiverRoom?.let { rRoom ->
                            FirebaseHelper.database.child("chats").child(sRoom).child("messages").push()
                                .setValue(messageObject).addOnSuccessListener {
                                    FirebaseHelper.database.child("chats").child(rRoom).child("messages").push()
                                        .setValue(messageObject)
                                }
                        }
                    }
                }
                messageBox.setText("")
            }
        }
    }
}