package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.AccessControlProfileId
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.model.commentModel
import com.google.firebase.database.FirebaseDatabase


class Edit_Review : AppCompatActivity() {


    private lateinit var commentId: TextView
    private lateinit var tvcomment: TextView
    private lateinit var btndelete: Button
    private lateinit var btnupdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_review)

        //initilize views
        initView()

        //set Values to the views
        setValuesToView()


        //set click listener to the update btn
        btnupdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("commentId").toString(),
                intent.getStringExtra("newComment").toString()

            )
        }

        //set click listener to the delete button
        btndelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("commentId").toString()
            )
        }
    }


    private fun initView() {

        //initilize views
        commentId = findViewById(R.id.commentId)
        tvcomment = findViewById(R.id.updateComment)
        btnupdate = findViewById(R.id.savebtn)
        btndelete = findViewById(R.id.deletebtn)


    }

    //set valus to the views
    private fun setValuesToView() {
        commentId.text = intent.getStringExtra("commentId")
        tvcomment.text = intent.getStringExtra("newComment")


    }

    //delete record from db
    private fun deleteRecord(
        id: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("comments").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Comment deleted successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this, manageReview::class.java)
            startActivity(intent) // start the new activity
            finish() // close the current activity
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Failed to delete comment: ${error.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    //open update dialog

    private fun openUpdateDialog(
        cmtId: String,
        comment: String
    ) {
        val uDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val uDialogView = inflater.inflate(R.layout.activity_update_dialog, null)

        //intiallize  the button and edittext

        val savebtn = uDialogView.findViewById<Button>(R.id.btnUpdate)

        uDialog.setView(uDialogView)

        val etCmt = uDialogView.findViewById<EditText>(R.id.editComment)

        //set data for update dialogbox

        etCmt.setText(intent.getStringExtra("newComment").toString())

        uDialog.setTitle("Updating comment")

        val alertDialog = uDialog.create()
        alertDialog.show()

        //set click listener to the save button
        savebtn.setOnClickListener {
            updateCmt(
                cmtId,
                etCmt.text.toString()
            )
            //make toast to inform update is success

            Toast.makeText(applicationContext, "Comment updated Succefully", Toast.LENGTH_LONG)
                .show()

            //update to data to textview
            tvcomment.text = etCmt.text.toString()

            alertDialog.dismiss()
        }

    }

    private fun updateCmt(
        id: String,
        comment: String
    ) {
        //update the database
        val dbRef = FirebaseDatabase.getInstance().getReference("comments").child(id)
        val cmtinfo = commentModel(id, comment)
        dbRef.setValue(cmtinfo)
    }
}

