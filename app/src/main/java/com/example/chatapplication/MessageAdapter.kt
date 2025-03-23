package com.example.chatapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.utils.ImageUtils
import com.google.firebase.auth.FirebaseAuth
import com.example.chatapplication.utils.FirebaseHelper

/**
 * Adapter for displaying messages in a RecyclerView.
 * Determines whether a message was sent or received and inflates the appropriate layout.
 */

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1 // View type for received messages
    private val ITEM_SENT = 2 // View type for sent messages

    //Determines the view type for a message (sent or received).
    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().currentUser?.uid == messageList[position].senderId)
            ITEM_SENT else ITEM_RECEIVE
    }

    //Creates the appropriate ViewHolder based on message type.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            if (viewType == ITEM_RECEIVE) R.layout.receive else R.layout.sent,
            parent, false
        )
        return if (viewType == ITEM_RECEIVE) ReceiveViewHolder(view) else SentViewHolder(view)
    }

    //Binds message data to the corresponding ViewHolder.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when (holder) {
            is SentViewHolder -> {
                holder.sentMessage.text = currentMessage.message // Display sent message text
            }
            is ReceiveViewHolder -> {
                holder.receiveMessage.text = currentMessage.message // Display received message text
                loadProfileImage(holder.receiveProfileImage, currentMessage.senderId)
            }
        }
    }

    // Displays the sender's profile image from Firebase, or sets a default if unavailable.
    private fun loadProfileImage(imageView: ImageView, senderUid: String?) {
        senderUid?.let { uid ->
            FirebaseHelper.database.child("user").child(uid).get().addOnSuccessListener {
                val profileImage = it.child("profileImage").value as? String
                ImageUtils.decodeBase64(profileImage)?.let { bitmap ->
                    Glide.with(context).load(bitmap).into(imageView)
                } ?: imageView.setImageResource(R.drawable.default_profile)
            }
        }
    }

    // Returns the number of messages in the list.
    override fun getItemCount(): Int = messageList.size

    // ViewHolder for sent messages.

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById(R.id.txt_sent_message)
    }

     // ViewHolder for received messages.
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.txt_receive_message)
        val receiveProfileImage: ImageView = itemView.findViewById(R.id.txt_userprofile)
    }
}
