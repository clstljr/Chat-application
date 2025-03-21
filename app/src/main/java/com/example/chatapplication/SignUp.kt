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

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var btnUploadImage: Button
    private lateinit var imgProfile: ImageView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    private var encodedImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edt_Username)
        edtEmail = findViewById(R.id.edt_Email)
        edtPassword = findViewById(R.id.edt_Password)
        btnSignup = findViewById(R.id.btn_signup)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        imgProfile = findViewById(R.id.app_logo)

        btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        btnSignup.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            signup(name, email, password, encodedImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            val inputStream = contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            imgProfile.setImageBitmap(bitmap)
            encodedImage = encodeImage(bitmap)
        }
    }

    private fun encodeImage(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun signup(name: String, email: String, password: String, profileImage: String?) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = mAuth.currentUser?.uid!!
                    addUserToDatabase(name, email, uid, profileImage)
                    startActivity(Intent(this@SignUp, MainActivity::class.java))
                } else {
                    Toast.makeText(this@SignUp, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String, profileImage: String?) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val user = User(name, email, uid, profileImage)
        mDbRef.child("user").child(uid).setValue(user)
    }
}