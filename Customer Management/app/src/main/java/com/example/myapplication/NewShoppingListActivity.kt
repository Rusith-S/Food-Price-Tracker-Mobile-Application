package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference

class NewShoppingListActivity : AppCompatActivity() {
    private lateinit var rootLayout:   View
    private lateinit var nameEditText: EditText
    private lateinit var createButton: Button

    private lateinit var shoppingListId: String
    private lateinit var shoppingListDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_shopping_list)

        shoppingListId = intent.extras!!.getString("shoppingListId")!!

        shoppingListDatabaseReference = Constants.database.child("users").child(Constants.user!!.uid)
            .child("shopping-lists").child(shoppingListId)

        // Initialize UI components
        rootLayout   = findViewById(R.id.rootLayout)
        nameEditText = findViewById(R.id.nameEditText)
        createButton = findViewById(R.id.createButton)

        createButton.setOnClickListener(View.OnClickListener {
            hideKeyboard(window, rootLayout)

            // Validate user inputs
            if (nameEditText.text.isNullOrBlank()) {
                val snackBar = Snackbar.make(rootLayout, "Shopping List Name cannot be empty", Snackbar.LENGTH_LONG)
                snackBar.show()
                return@OnClickListener
            }

            val name = nameEditText.text.toString()

            val shoppingList = ShoppingList(shoppingListId, name)
            shoppingListDatabaseReference.setValue(shoppingList)

            finish() // Destroy this screen
        })
    }
}