package com.example.chatapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent


class UserAdapter(val context: Context, val userList: ArrayList<User>):
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

        // Decode Base64 image
        if (!currentUser.profileImage.isNullOrEmpty()) {
            val decodedImage = decodeBase64(currentUser.profileImage!!)
            holder.imgProfile.setImageBitmap(decodedImage)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.txt_name)
        val imgProfile = itemView.findViewById<ImageView>(R.id.txt_userprofile)
    }

    private fun decodeBase64(encodedImage: String): Bitmap {
        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}