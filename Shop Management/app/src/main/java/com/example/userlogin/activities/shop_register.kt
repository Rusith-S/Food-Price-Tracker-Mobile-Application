package com.example.userlogin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.userlogin.R
import com.example.userlogin.databinding.ActivityShopRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class shop_register : AppCompatActivity() {


    private lateinit var binding:ActivityShopRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signin.setOnClickListener {
            val intent =  Intent(this, shop_login::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {

            val email = binding.addemail.text.toString()
            val Password = binding.addpassword.text.toString()
            val Repassword = binding.rePassword.text.toString()
            if(email.isNotEmpty() && Password.isNotEmpty() && Repassword.isNotEmpty()){
                if(Password == Repassword){

                    firebaseAuth.createUserWithEmailAndPassword(email,Password).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this , shop_login::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this,it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this , "Password is not matching",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this , "Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
            }

        }
    }
}