package com.translator.wordwanderer.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import com.translator.wordwanderer.R
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userEmail: TextView
    private lateinit var userFullName: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeFirebase()

        userEmail = findViewById(R.id.userEmail)
        userFullName = findViewById(R.id.userName)
        profileImageView = findViewById(R.id.profile)

        val editProfileImageButton: ImageButton = findViewById(R.id.editProfile)

        val user = auth.currentUser
        if (user != null) {
            loadUserProfile(user)
        } else {
            promptUserToLogin()
        }

        editProfileImageButton.setOnClickListener {
            openImageChooser()
        }


        val backButton: TextView = findViewById(R.id.backButton)
        val signOut: TextView = findViewById(R.id.topSignOut)
        val changePassword: TextView = findViewById(R.id.changePassword)
        val changeUserName: TextView = findViewById(R.id.changeUserName)

        setupClickListeners(backButton, signOut, changePassword, changeUserName)
    }

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
    }

    private fun loadUserProfile(user: FirebaseUser) {
        db.collection("users").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fullName = document.getString("fullName")
                    val email = document.getString("email")
                    val profileImageUrl = document.getString("profileImageUrl")

                    userFullName.text = fullName ?: "No name found"
                    userEmail.text = email ?: "No email found"

                    if (profileImageUrl != null) {
                        loadProfileImage(profileImageUrl)
                    }
                } else {
                    showToast("Profile data does not exist.")
                    userFullName.text = "No name found"
                    userEmail.text = "No email found"
                }
            }
            .addOnFailureListener { exception ->
                showToast("Failed to load profile: ${exception.message}")
                userFullName.text = "Error loading name"
                userEmail.text = "Error loading email"
            }
    }

    private fun promptUserToLogin() {
        showToast("Session expired. Please log in again.")
        val intent = Intent(this@ProfileActivity, LoginGuestActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupClickListeners(backButton: TextView, signOut: TextView, changePassword: TextView, changeUserName: TextView) {
        backButton.setOnClickListener {
            navigateToDashboard()
        }

        signOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@ProfileActivity, LoginGuestActivity::class.java)
            startActivity(intent)
            finish()
        }

        changePassword.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        changeUserName.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ChangeUserNameActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val intent = Intent(this@ProfileActivity, DashboardActivity::class.java)
        intent.putExtra("Username", userFullName.text.toString())
        startActivity(intent)
        finish()
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun loadProfileImage(profileImageUrl: String) {
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(profileImageUrl)
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .transform(CropCircleTransformation())
                .into(profileImageView)
        }.addOnFailureListener {
            showToast("Failed to load profile image")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                profileImageView.setImageBitmap(bitmap)
                uploadImageToFirestore(imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImageToFirestore(imageUri: Uri) {
        val user = auth.currentUser
        user?.let {
            val profileImagesRef: StorageReference = storageRef.child("profile_images/${user.uid}.jpg")
            val uploadTask: UploadTask = profileImagesRef.putFile(imageUri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    db.collection("users").document(user.uid)
                        .update("profileImageUrl", downloadUrl)
                        .addOnSuccessListener {
                            showToast("Profile image updated")
                        }
                        .addOnFailureListener { e ->
                            Log.e("ProfileUpdate", "Error updating profile image", e)
                        }
                }
            }.addOnFailureListener { e ->
                Log.e("ProfileUpdate", "Error uploading profile image", e)
            }
        }
    }
}