package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.text.isDigitsOnly
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference

class NewShoppingListItemActivity : AppCompatActivity() {
    private lateinit var rootLayout: View
    private lateinit var productEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var addButton: Button

    private lateinit var shoppingListId: String
    private lateinit var shoppingListItemId: String
    private lateinit var shoppingListDatabaseReference: DatabaseReference
    private lateinit var shoppingListItemDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_shopping_list_item)

        shoppingListId     = intent.extras!!.getString("shoppingListId")!!
        shoppingListItemId = intent.extras!!.getString("shoppingListItemId")!!

        shoppingListDatabaseReference = Constants.database.child("users").child(Constants.user!!.uid)
            .child("shopping-lists").child(shoppingListId)

        shoppingListItemDatabaseReference = Constants.database.child("users").child(Constants.user!!.uid)
            .child("shopping-lists").child(shoppingListId)
            .child("items").child(shoppingListItemId)

        // Initialize UI components
        rootLayout       = findViewById(R.id.rootLayout)
        productEditText  = findViewById(R.id.productEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        addButton        = findViewById(R.id.addButton)

        addButton.setOnClickListener(View.OnClickListener {
            hideKeyboard(window, rootLayout)

            // Validate user inputs
            // Validate user inputs
            if (productEditText.text.isNullOrBlank()) {
                popup("Product Name cannot be empty", rootLayout)
                return@OnClickListener
            }

            if (quantityEditText.text.isNullOrBlank()) {
                popup("Quantity cannot be empty", rootLayout)
                return@OnClickListener
            }

            if (!quantityEditText.text.isDigitsOnly()) {
                popup("Quantity should be a number", rootLayout)
                return@OnClickListener
            }

            val product  = productEditText.text.toString()
            val quantity = quantityEditText.text.toString().toInt()

            // Create new shopping list item
            val shoppingListItem = ShoppingListItem(shoppingListItemId, product, quantity)
            shoppingListItemDatabaseReference.setValue(shoppingListItem)

            // Update shopping list timestamp because it was updated with a new item
            shoppingListDatabaseReference.child("timestamp").setValue(System.currentTimeMillis())

            finish() // Destroy this screen
        })
    }
}