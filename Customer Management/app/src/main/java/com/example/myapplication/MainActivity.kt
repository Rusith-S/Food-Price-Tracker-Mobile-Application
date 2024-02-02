package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var createANewShoppingListButton: Button
    private lateinit var listView: ListView

    private lateinit var shoppingListsDatabaseReference: DatabaseReference
    private lateinit var shoppingListsValueListener: ValueEventListener

    private var shoppingLists: List<ShoppingList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        Constants.auth     = Firebase.auth
        // Get signed-in user
        Constants.user     = Constants.auth.currentUser
        // Get Firebase Realtime Database reference
        Constants.database = Firebase.database.reference

        // Initialize UI components
        createANewShoppingListButton = findViewById(R.id.createANewShoppingListButton)
        listView                     = findViewById(R.id.listView)

        createANewShoppingListButton.setOnClickListener {
            if (shoppingLists != null) {
                // Open NewShoppingListActivity screen
                startActivity(Intent(this, NewShoppingListActivity::class.java)
                    .putExtra("shoppingListId", shoppingLists!!.size.toString()))
            }
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val shoppingList = parent.getItemAtPosition(position) as ShoppingList

            // Open NewShoppingListActivity screen
            startActivity(Intent(this, ShoppingListActivity::class.java)
                .putExtra("shoppingListId", shoppingList!!.id))
        }

        // Firebase value listener
        shoppingListsValueListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get user's shopping lists
                shoppingLists = dataSnapshot.getValue<List<ShoppingList>>()?.sortedByDescending { it.timestamp } // Latest modified shopping list comes first
                // User's shopping lists are available?
                if (shoppingLists != null) {
                    // List user's shopping lists in the ListView
                    listView.adapter = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, shoppingLists!!)
                }
                // User has not created any shopping lists
                else {
                    shoppingLists = listOf()
                    listView.adapter = null
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
    }

    override fun onStart() {
        super.onStart()

        // Is a user signed-in?
        if (Constants.user != null) {
            // Add listener to signed-in user's shopping lists
            shoppingListsDatabaseReference = Constants.database.child("users").child(Constants.user!!.uid).child("shopping-lists")
            shoppingListsDatabaseReference.addValueEventListener(shoppingListsValueListener)
        }
        // User has not signed-in?
        else {
            // Open NotSignedinActivity screen
            startActivity(Intent(this, NotSignedinActivity::class.java))
            finish() // Destroy this screen
        }
    }

    override fun onPause() {
        // Is a user signed-in?
        if (Constants.user != null) {
            // Remove the listener to signed-in user's shopping lists
            shoppingListsDatabaseReference.removeEventListener(shoppingListsValueListener)
        }
        super.onPause()
    }
}
