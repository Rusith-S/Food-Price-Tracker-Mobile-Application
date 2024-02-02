package com.example.myapplication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class Constants {
    companion object {
        lateinit var auth:     FirebaseAuth
        var          user:     FirebaseUser? = null
        lateinit var database: DatabaseReference
    }
}