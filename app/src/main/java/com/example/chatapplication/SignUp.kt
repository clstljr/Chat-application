package com.example.chatapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var edtName: EditText // Input field for name
    private lateinit var edtEmail: EditText // Input field for email
    private lateinit var edtPassword: EditText // Input field for password
    private lateinit var btnSignup: Button // Button to register
    private lateinit var btnUploadImage: Button // Button to upload profile image
    private lateinit var imgProfile: ImageView // Displays selected profile image
    private lateinit var mAuth: FirebaseAuth // Firebase authentication instance
    private lateinit var mDbRef: DatabaseReference // Firebase database reference
    private lateinit var btnGoback: ImageView // Button to navigate back to login

    private var encodedImage: String? = null // Stores base64-encoded profile image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        // Initialize Firebase authentication
        mAuth = FirebaseAuth.getInstance()

        // Initialize UI elements
        edtName = findViewById(R.id.edt_Username)
        edtEmail = findViewById(R.id.edt_Email)
        edtPassword = findViewById(R.id.edt_Password)
        btnSignup = findViewById(R.id.btn_signup)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        imgProfile = findViewById(R.id.app_logo)
        btnGoback = findViewById(R.id.btn_goback)

        imgProfile.setImageResource(R.drawable.default_profile) // Set default profile image

        // Open image picker when upload button is clicked
        btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        // Handle signup button click
        btnSignup.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            signup(name, email, password, encodedImage) // Call signup function
        }

        // Navigate back to login screen
        btnGoback.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
        }
    }

    // Handle image selection result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            val inputStream = contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            imgProfile.setImageBitmap(bitmap) // Display selected image
            encodedImage = encodeImage(bitmap) // Convert image to base64 string
        }
    }

    // Convert bitmap image to base64 string for storage
    private fun encodeImage(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Store default profile image as base64 if user does not upload one
    private fun encodeDrawableToBase64(drawableId: Int): String {
        val drawable = resources.getDrawable(drawableId, null)
        val bitmap = (drawable as android.graphics.drawable.BitmapDrawable).bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Create a new user account
    private fun signup(name: String, email: String, password: String, profileImage: String?) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = mAuth.currentUser?.uid!!
                    val finalProfileImage = profileImage ?: encodeDrawableToBase64(R.drawable.default_profile)

                    addUserToDatabase(name, email, uid, finalProfileImage) // Store user details in database
                    startActivity(Intent(this@SignUp, LogIn::class.java)) // Redirect to login
                    Toast.makeText(this@SignUp, "Kindly log in to your new account", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SignUp, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Store user details in Firebase database
    private fun addUserToDatabase(name: String, email: String, uid: String, profileImage: String?) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val user = User(name, email, uid, profileImage)
        mDbRef.child("user").child(uid).setValue(user)
    }
}