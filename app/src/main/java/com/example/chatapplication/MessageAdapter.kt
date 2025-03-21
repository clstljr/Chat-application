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

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1
    private val ITEM_SENT = 2
    private val defaultProfile = R.drawable.profile_icon

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            if (viewType == ITEM_RECEIVE) R.layout.receive else R.layout.sent,
            parent, false
        )
        return if (viewType == ITEM_RECEIVE) ReceiveViewHolder(view) else SentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder is SentViewHolder) {
            holder.sentMessage.text = currentMessage.message
        } else if (holder is ReceiveViewHolder) {
            holder.receiveMessage.text = currentMessage.message
            loadProfileImage(holder.receiveProfileImage, currentMessage.senderProfile)
        }
    }

    private fun loadProfileImage(imageView: ImageView, profileImage: String?) {
        ImageUtils.decodeBase64(profileImage)?.let {
            Glide.with(context).asBitmap().load(it).into(imageView)
        } ?: imageView.setImageResource(defaultProfile)
    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().currentUser?.uid == messageList[position].senderId)
            ITEM_SENT else ITEM_RECEIVE
    }

    override fun getItemCount(): Int = messageList.size

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById(R.id.txt_sent_message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.txt_receive_message)
        val receiveProfileImage: ImageView = itemView.findViewById(R.id.txt_userprofile)
    }
}
