package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ShoppingListActivity : AppCompatActivity() {
    private lateinit var rootLayout:                    View
    private lateinit var nameEditText:                  EditText
    private lateinit var updateShoppingListNameButton:  Button
    private lateinit var deleteThisShoppingListButton:  Button
    private lateinit var addANewShoppingListItemButton: Button
    private lateinit var listView:                      ListView

    private lateinit var shoppingListId: String
    private lateinit var shoppingListDatabaseReference: DatabaseReference
    private lateinit var shoppingListValueListener: ValueEventListener
    private var shoppingList: ShoppingList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        shoppingListId = intent.extras!!.getString("shoppingListId")!!

        shoppingListDatabaseReference = Constants.database.child("users").child(Constants.user!!.uid)
            .child("shopping-lists").child(shoppingListId)

        // Initialize UI components
        rootLayout                    = findViewById(R.id.rootLayout)
        nameEditText                  = findViewById(R.id.nameEditText)
        updateShoppingListNameButton  = findViewById(R.id.updateShoppingListNameButton)
        deleteThisShoppingListButton  = findViewById(R.id.deleteThisShoppingListButton)
        addANewShoppingListItemButton = findViewById(R.id.addANewShoppingListItemButton)
        listView                      = findViewById(R.id.listView)

        updateShoppingListNameButton.setOnClickListener(View.OnClickListener {
            if (shoppingList != null) {
                hideKeyboard(window, rootLayout)

                // Validate user inputs
                if (nameEditText.text.isNullOrBlank()) {
                    popup("Shopping List Name cannot be empty", rootLayout)
                    return@OnClickListener
                }

                var name = nameEditText.text.toString()

                // Update shopping list name
                shoppingList!!.name = name
                shoppingList!!.timestamp = System.currentTimeMillis()
                shoppingListDatabaseReference.setValue(shoppingList)

                popup("Updated", rootLayout)
            }
        })

        deleteThisShoppingListButton.setOnClickListener(View.OnClickListener {
            if (shoppingList != null) {
                hideKeyboard(window, rootLayout)

                // Delete this shopping list
                shoppingListDatabaseReference.removeValue()

                finish() // Destroy this screen
            }
        })

        addANewShoppingListItemButton.setOnClickListener {
            if (shoppingList != null) {
                startActivity(Intent(baseContext, NewShoppingListItemActivity::class.java)
                    .putExtra("shoppingListId", shoppingListId)
                    .putExtra("shoppingListItemId", shoppingList!!.items?.size.toString() ?: "0"))
            }
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val shoppingListItem = parent.getItemAtPosition(position) as ShoppingListItem

            // Open ShoppingListItemActivity screen
            startActivity(Intent(this, ShoppingListItemActivity::class.java)
                .putExtra("shoppingListId", shoppingListId)
                .putExtra("shoppingListItemId", shoppingListItem!!.id))
        }

        shoppingListValueListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                shoppingList = dataSnapshot.getValue<ShoppingList>()

                if (shoppingList != null) {
                    // Set shopping list name to EditText
                    nameEditText.setText(shoppingList!!.name)
                    if (shoppingList!!.items != null) {
                        shoppingList!!.items = shoppingList!!.items!!.sortedByDescending { it.timestamp } // Latest modified shopping list item comes first
                        // List shopping list items in ListView
                        listView.adapter = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, shoppingList!!.items!!)
                    } else {
                        listView.adapter = null
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
    }

    override fun onStart() {
        super.onStart()
        shoppingListDatabaseReference.addValueEventListener(shoppingListValueListener)
    }

    override fun onPause() {
        shoppingListDatabaseReference.removeEventListener(shoppingListValueListener);
        super.onPause()
    }
}