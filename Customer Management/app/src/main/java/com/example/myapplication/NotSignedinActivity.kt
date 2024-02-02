package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class NotSignedinActivity : AppCompatActivity() {
    private lateinit var signupButton: Button
    private lateinit var signinButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_signedin)

        signupButton = findViewById(R.id.signupButton)
        signinButton = findViewById(R.id.signinButton)

        signupButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        })

        signinButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        })
    }
}