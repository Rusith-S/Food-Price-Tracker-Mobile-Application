package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var rootLayout:       View
    private lateinit var emailEditText:    EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signinButton:     Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components
        rootLayout       = findViewById(R.id.rootLayout)
        emailEditText    = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signinButton     = findViewById(R.id.signinButton)

        signinButton.setOnClickListener(View.OnClickListener {
            hideKeyboard(window, rootLayout)

            // Validate user inputs
            if (emailEditText.text.isNullOrBlank()) {
                val snackBar = Snackbar.make(rootLayout, "Email cannot be empty", Snackbar.LENGTH_LONG)
                snackBar.show()
                return@OnClickListener
            }

            if (passwordEditText.text.isNullOrBlank()) {
                val snackBar = Snackbar.make(rootLayout, "Password cannot be empty", Snackbar.LENGTH_LONG)
                snackBar.show()
                return@OnClickListener
            }

            var email: String    = emailEditText.text.toString()
            var password: String = passwordEditText.text.toString()

            // Sign-in user with email and password
            Constants.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        Constants.user = Constants.auth.currentUser

                        // Open MainActivity screen
                        startActivity(Intent(this, MainActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)) // Get rid of all previous stacked screens
                        finish() // Destroy this screen
                    } else {
                        // Sign in failed
                        val snackBar = Snackbar.make(rootLayout, "User sign-in failed. " + task.exception?.message, Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }
                }
        })
    }
}