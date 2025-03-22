package com.example.chatapplication.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

/**
 * ImageUtils is a utility object that provides methods for handling image encoding and decoding.
 * It helps convert images between Bitmap format and Base64 string representation.
 */
object ImageUtils {

    /**
     * Decodes a Base64-encoded string into a Bitmap.
     *
     * @param encodedImage The Base64 string representation of an image.
     * @return A Bitmap object if decoding is successful, otherwise null.
     */
    fun decodeBase64(encodedImage: String?): Bitmap? {
        return if (!encodedImage.isNullOrEmpty()) {
            val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size) // Convert byte array to Bitmap
        } else {
            null
        }
    }
}