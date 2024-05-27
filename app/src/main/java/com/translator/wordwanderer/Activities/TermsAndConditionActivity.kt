package com.translator.wordwanderer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.translator.wordwanderer.R

class TermsAndConditionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_condition)
        val btn: Button = findViewById(R.id.gotit)
        btn.setOnClickListener{
            val intent = Intent(this@TermsAndConditionActivity, UpgradeActivity::class.java)
            startActivity(intent)
        }
    }
}