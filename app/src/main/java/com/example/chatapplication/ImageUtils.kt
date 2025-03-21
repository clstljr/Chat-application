package com.example.chatapplication.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object ImageUtils {
    fun decodeBase64(encodedImage: String?): Bitmap? {
        return if (!encodedImage.isNullOrEmpty()) {
            val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } else {
            null
        }
    }
}
