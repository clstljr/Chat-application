package com.example.chatapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.utils.ImageUtils

/**
 * Adapter for displaying a list of users in a RecyclerView.
 * Handles user profile images and name display, and allows navigation to the chat screen.
 */
class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    /**
     * Creates a ViewHolder for each user item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    /**
     * Returns the total number of users in the list.
     */
    override fun getItemCount(): Int {
        return userList.size
    }

    /**
     * Binds user data to the ViewHolder, including name and profile image.
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.name // Set the user's name

        // Load profile image if available, otherwise use default
        val profileImage = currentUser.profileImage
        if (!profileImage.isNullOrEmpty()) {
            ImageUtils.decodeBase64(profileImage)?.let { bitmap ->
                Glide.with(context).load(bitmap).into(holder.imgProfile)
            } ?: holder.imgProfile.setImageResource(R.drawable.default_profile)
        } else {
            holder.imgProfile.setImageResource(R.drawable.default_profile)
        }

        // Open ChatActivity when a user is clicked
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra("name", currentUser.name)
                putExtra("uid", currentUser.uid)
            }
            context.startActivity(intent)
        }
    }

    /**
     * ViewHolder class to hold UI components for each user item.
     */
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.txt_name)
        val imgProfile: ImageView = itemView.findViewById(R.id.txt_userprofile)
    }
}