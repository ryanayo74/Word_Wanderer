package com.translator.wordwanderer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val displayText: TextView = findViewById(R.id.textViewDisplay)
        val name = intent.getStringExtra("Username")
        val modulesImageView : ImageView = findViewById(R.id.modules)
        val gamesImageView : ImageView = findViewById(R.id.games)
        val profileImageView: ImageView  = findViewById(R.id.profile)
        val logoutButton: ImageView = findViewById(R.id.buttonLogout)

        //For UnderConstruction Page
        val languageImageView: ImageView = findViewById(R.id.languagesImageView)
        val translationImageView: ImageView = findViewById(R.id.translationImageView)
        val upgradeVipImageView: ImageView = findViewById(R.id.upgradeVipImageView)

        displayText.text = name

        logoutButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LoginGuestActivity::class.java)
            startActivity(intent)
            finish()
        }

        profileImageView.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
            intent.putExtra("Username", name)
            startActivity(intent)
            finish()
        }

        modulesImageView.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ModulesActivity::class.java)
            intent.putExtra("Username", name)
            startActivity(intent)
            finish()
        }

        gamesImageView.setOnClickListener {
            val intent = Intent(this@DashboardActivity, GamesActivity::class.java)
            intent.putExtra("Username", name)
            startActivity(intent)
            finish()
        }

        translationImageView.setOnClickListener {
            val intent = Intent(this@DashboardActivity, TextTranslationActivity::class.java)
            intent.putExtra("Username", name)
            startActivity(intent)
            finish()
        }


        //For UnderConstruction Page
        languageImageView.setOnClickListener {
            val intent = Intent(this@DashboardActivity, UnderConstruction::class.java)
            intent.putExtra("Username", name)
            startActivity(intent)
            finish()
        }

        upgradeVipImageView.setOnClickListener {
            val intent = Intent(this@DashboardActivity, UnderConstruction::class.java)
            intent.putExtra("Username", name)
            startActivity(intent)
            finish()
        }




    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { dialog, which ->
                super.onBackPressed()}
            .setNegativeButton("No", null)
            .show()
    }
}