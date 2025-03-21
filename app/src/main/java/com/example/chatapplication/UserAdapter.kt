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

class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.textName.text = currentUser.name

        val profileImage = currentUser.profileImage
        if (!profileImage.isNullOrEmpty()) {
            ImageUtils.decodeBase64(profileImage)?.let { bitmap ->
                Glide.with(context).load(bitmap).into(holder.imgProfile)
            } ?: holder.imgProfile.setImageResource(R.drawable.default_profile)
        } else {
            holder.imgProfile.setImageResource(R.drawable.default_profile)
        }
//hjhhjhj
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra("name", currentUser.name)
                putExtra("uid", currentUser.uid)
            }
            context.startActivity(intent)
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.txt_name)
        val imgProfile: ImageView = itemView.findViewById(R.id.txt_userprofile)
    }
}