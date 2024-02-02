package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.core.text.isDigitsOnly
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ShoppingListItemActivity : AppCompatActivity() {
    private lateinit var rootLayout:                       View
    private lateinit var productEditText:                  EditText
    private lateinit var quantityEditText:                 EditText
    private lateinit var updateShoppingListItemButton:     Button
    private lateinit var deleteThisShoppingListItemButton: Button

    private lateinit var shoppingListId: String
    private lateinit var shoppingListItemId: String
    private lateinit var shoppingListDatabaseReference: DatabaseReference
    private lateinit var shoppingListItemDatabaseReference: DatabaseReference
    private lateinit var shoppingListItemValueListener: ValueEventListener
    private var shoppingListItem: ShoppingListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list_item)

        shoppingListId     = intent.extras!!.getString("shoppingListId")!!
        shoppingListItemId = intent.extras!!.getString("shoppingListItemId")!!

        shoppingListDatabaseReference = Constants.database.child("users").child(Constants.user!!.uid)
            .child("shopping-lists").child(shoppingListId)

        shoppingListItemDatabaseReference = Constants.database.child("users").child(Constants.user!!.uid)
            .child("shopping-lists").child(shoppingListId)
            .child("items").child(shoppingListItemId)

        rootLayout                       = findViewById(R.id.rootLayout)
        productEditText                  = findViewById(R.id.productEditText)
        quantityEditText                 = findViewById(R.id.quantityEditText)
        updateShoppingListItemButton     = findViewById(R.id.updateShoppingListItemButton)
        deleteThisShoppingListItemButton = findViewById(R.id.deleteThisShoppingListItemButton)

        updateShoppingListItemButton.setOnClickListener(View.OnClickListener {
            if (shoppingListItem != null) {
                hideKeyboard(window, rootLayout)

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

                var product  = productEditText.text.toString()
                var quantity = quantityEditText.text.toString().toInt()

                // Update shopping list item
                shoppingListItem!!.product   = product
                shoppingListItem!!.quantity  = quantity
                shoppingListItem!!.timestamp = System.currentTimeMillis()
                shoppingListItemDatabaseReference.setValue(shoppingListItem)

                // Update shopping list timestamp because it was updated with an item modification
                shoppingListDatabaseReference.child("timestamp").setValue(System.currentTimeMillis())

                popup("Updated", rootLayout)
            }
        })

        deleteThisShoppingListItemButton.setOnClickListener {
            if (shoppingListItem != null) {
                hideKeyboard(window, rootLayout)

                // Delete this shopping list item
                shoppingListItemDatabaseReference.removeValue()

                finish() // Destroy this screen
            }
        }

        shoppingListItemValueListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                shoppingListItem = dataSnapshot.getValue<ShoppingListItem>()

                if (shoppingListItem != null) {
                    productEditText.setText(shoppingListItem!!.product)
                    quantityEditText.setText(shoppingListItem!!.quantity.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
    }

    override fun onStart() {
        super.onStart()
        shoppingListItemDatabaseReference.addValueEventListener(shoppingListItemValueListener)
    }

    override fun onPause() {
        shoppingListItemDatabaseReference.removeEventListener(shoppingListItemValueListener)
        super.onPause()
    }
}