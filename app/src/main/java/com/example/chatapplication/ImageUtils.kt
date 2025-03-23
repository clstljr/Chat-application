package com.example.chatapplication.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

//For handling image decoding.

object ImageUtils {


    // Decodes a Base64-encoded string into a Bitmap.
    fun decodeBase64(encodedImage: String?): Bitmap? {
        return if (!encodedImage.isNullOrEmpty()) {
            val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size) // Convert byte array to Bitmap
        } else {
            null
        }
    }
}