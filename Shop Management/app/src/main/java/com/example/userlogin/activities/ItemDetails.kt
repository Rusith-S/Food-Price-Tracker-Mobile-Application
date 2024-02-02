package com.example.userlogin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.userlogin.R
import com.example.userlogin.models.ItemModel
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class ItemDetails : AppCompatActivity() {

    private lateinit var tvItemId: TextView
    private lateinit var tvItemName: TextView
    private lateinit var tvItemPrice: TextView
    private lateinit var tvStockStatus: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        initView()
        setValuesToViews()
        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("empId").toString()
            )
        }

    }

    private fun initView() {
        tvItemId = findViewById(R.id.tvItemId)
        tvItemName = findViewById(R.id.tvItemName)
        tvItemPrice = findViewById(R.id.tvItemPrice)
        tvStockStatus = findViewById(R.id.tvStockStatus)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvItemId.text = intent.getStringExtra("itemId")
        tvItemName.text = intent.getStringExtra("itemName")
        tvItemPrice.text = intent.getStringExtra("itemPrice")
        tvStockStatus.text = intent.getStringExtra("outOrIn")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Items").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Item deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, view_items::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        itemId: String,
        itemName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_update, null)

        mDialog.setView(mDialogView)

        val etItemName = mDialogView.findViewById<EditText>(R.id.etItemName)
        val etItemPrice = mDialogView.findViewById<EditText>(R.id.etItemPrice)
        val etStockStatus = mDialogView.findViewById<EditText>(R.id.etStockStatus)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etItemName.setText(intent.getStringExtra("itemName").toString())
        etItemPrice.setText(intent.getStringExtra("itemPrice").toString())
        etStockStatus.setText(intent.getStringExtra("outOrIn").toString())


        mDialog.setTitle("Updating $itemName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                itemId,
                etItemName.text.toString(),
                etItemPrice.text.toString(),
                etStockStatus.text.toString()
            )

            Toast.makeText(applicationContext, "Item Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvItemName.text = etItemName.text.toString()
            tvItemPrice.text = etItemPrice.text.toString()
            tvStockStatus.text = etStockStatus.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Items").child(id)
        val empInfo = ItemModel(id, name, age, salary)
        dbRef.setValue(empInfo)
    }

}






//    private fun initView() {}
//    private fun setValuesToViews(){
//        tvItemId.text = intent.getStringExtra("itemId")
//        tvItemName.text = intent.getStringExtra("itemName")
//        tvItemPrice.text = intent.getStringExtra("itemPrice")
//        tvStockStatus.text = intent.getStringExtra("outOrIn")
//    }
//
//
//
//}