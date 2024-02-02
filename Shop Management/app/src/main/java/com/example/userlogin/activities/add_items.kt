package com.example.userlogin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.userlogin.models.ItemModel
import com.example.userlogin.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class add_items : AppCompatActivity() {


    private lateinit var addItemName: EditText
    private lateinit var addUnitPrice: EditText
    private lateinit var addstockStatus: EditText

    private lateinit var btnItemsAdding: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_items)

        addItemName = findViewById(R.id.addemail)
        addUnitPrice= findViewById(R.id.addpassword)
        addstockStatus= findViewById(R.id.addstockStatus)
        btnItemsAdding = findViewById(R.id.button)

        dbRef = FirebaseDatabase.getInstance().getReference("Items")

        btnItemsAdding.setOnClickListener {
            saveItemsData()
        }
    }

    private fun saveItemsData(){
        //getting items
        val itemName = addItemName.text.toString()
        val itemPrice = addUnitPrice.text.toString()
        val outOrIn = addstockStatus.text.toString()

        if (itemName.isEmpty()){
            addItemName.error = "Please Enter Item Name"
        }
        if (itemPrice.isEmpty()){
            addUnitPrice.error = "Please Enter Unit Price"
        }
        if (outOrIn.isEmpty()){
            addstockStatus.error = "Please Stock Status"
        }
        val itemId = dbRef.push().key!!

        val item = ItemModel(itemId,itemName,itemPrice,outOrIn)

        dbRef.child(itemId).setValue(item)
            .addOnCompleteListener {
                Toast.makeText(this,"Item Added Successfully", Toast.LENGTH_LONG).show()

                addItemName.text.clear()
                addUnitPrice.text.clear()
                addstockStatus.text.clear()

            }.addOnFailureListener{ err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}