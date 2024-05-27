package com.translator.wordwanderer.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.R

class PremiumModulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium_modules)

        val username = intent.getStringExtra("Username")
        val homeImageView: ImageView = findViewById(R.id.homeButton)
        val accountImageView: ImageView = findViewById(R.id.accountButton)

        val imageModule1Button: ImageView = findViewById(R.id.imageModule1Button)
        val imageModule2Button: ImageView = findViewById(R.id.imageModule2Button)
        val imageModule3Button: ImageView = findViewById(R.id.imageModule3Button)
        val imageModule4Button: ImageView = findViewById(R.id.imageModule4Button)
        val allButton : Button = findViewById(R.id.allButton)
        val academicButton : Button = findViewById(R.id.academicButton)


        homeImageView.setOnClickListener {
            navigateToDashboard()
        }

        accountImageView.setOnClickListener {

            // Start ProfileActivity and pass the username as an extra
            val profileIntent = Intent(this@PremiumModulesActivity, ProfileActivity::class.java)
            profileIntent.putExtra("Username", username)
            startActivity(profileIntent)
            finish()
        }

        imageModule1Button.setOnClickListener {
            val intent = Intent(this@PremiumModulesActivity, ModuleOneActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageModule2Button.setOnClickListener {
            val intent = Intent(this@PremiumModulesActivity, ModuleTwoActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageModule3Button.setOnClickListener {
            val intent = Intent(this@PremiumModulesActivity, ModuleThreeActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageModule4Button.setOnClickListener {
            val intent = Intent(this@PremiumModulesActivity, ModuleFourActivity::class.java)
            startActivity(intent)
            finish()
        }

        allButton.setOnClickListener {
            val intent = Intent(this@PremiumModulesActivity, ModulesActivity::class.java)
            startActivity(intent)
            finish()
        }

        academicButton.setOnClickListener {
            val intent = Intent(this@PremiumModulesActivity, ModulesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val username = intent.getStringExtra("Username") // Retrieve username
        val intent = Intent(this@PremiumModulesActivity, PremiumDashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}