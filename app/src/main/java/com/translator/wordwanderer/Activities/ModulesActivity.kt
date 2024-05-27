package com.translator.wordwanderer.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.translator.wordwanderer.R

class ModulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modules)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("Username")
        val homeImageView: ImageView = findViewById(R.id.homeButton)
        val accountImageView: ImageView = findViewById(R.id.accountButton)

        //UnderConstruction
        val upgradeVipImageView: ImageView = findViewById(R.id.upgradeVipImageView)
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
            val profileIntent = Intent(this@ModulesActivity, ProfileActivity::class.java)
            profileIntent.putExtra("Username", username)
            startActivity(profileIntent)
            finish()
        }
        //UnderConstruction
        upgradeVipImageView.setOnClickListener {
            val intent = Intent(this@ModulesActivity, UnderConstruction::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
            finish()
        }

        imageModule1Button.setOnClickListener {
            val intent = Intent(this@ModulesActivity, ModuleOneActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageModule2Button.setOnClickListener {
            val intent = Intent(this@ModulesActivity, ModuleTwoActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageModule3Button.setOnClickListener {
            val intent = Intent(this@ModulesActivity, UpgradeActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageModule4Button.setOnClickListener {
            val intent = Intent(this@ModulesActivity, UpgradeActivity::class.java)
            startActivity(intent)
            finish()
        }

        allButton.setOnClickListener {
            val intent = Intent(this@ModulesActivity, ModulesActivity::class.java)
            startActivity(intent)
            finish()
        }

        academicButton.setOnClickListener {
            val intent = Intent(this@ModulesActivity, ModulesActivity::class.java)
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
        val intent = Intent(this@ModulesActivity, DashboardActivity::class.java)
        intent.putExtra("Username", username) // Pass username to DashboardActivity
        startActivity(intent)
        finish()
    }
}
