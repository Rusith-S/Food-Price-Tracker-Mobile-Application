package com.example.userlogin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.userlogin.R
import com.example.userlogin.databinding.ActivityShopLoginBinding
import com.google.firebase.auth.FirebaseAuth

class shop_login : AppCompatActivity() {

    private lateinit var binding:ActivityShopLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.redirectSignup.setOnClickListener{
            val intent =  Intent(this, shop_register::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {

            val email = binding.addemail.text.toString()
            val Password = binding.addpassword.text.toString()
            if(email.isNotEmpty() && Password.isNotEmpty() ){
                    firebaseAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this , home_page::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this,it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }

            }else{
                Toast.makeText(this , "Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}