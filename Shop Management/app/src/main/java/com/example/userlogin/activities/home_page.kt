package com.example.userlogin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.userlogin.R

class home_page : AppCompatActivity() {
    private lateinit var btnEditProf: Button
    private lateinit var btnAddItems: Button
    private lateinit var btnViewItems: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        btnEditProf = findViewById(R.id.button)
        btnAddItems = findViewById(R.id.btnAddItems)
        btnViewItems = findViewById(R.id.btnViewItems)

        btnEditProf.setOnClickListener{
            val intent = Intent(this, shop_profile::class.java)
            startActivity(intent)
        }
        btnAddItems.setOnClickListener {
            val intent = Intent(this, add_items::class.java)
            startActivity(intent)
        }
        btnViewItems.setOnClickListener {
            val intent = Intent(this, view_items::class.java)
            startActivity(intent)
        }
    }
}