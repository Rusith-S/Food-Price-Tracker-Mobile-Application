package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.model.commentModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//difine the activity class
class addComment : AppCompatActivity() {

    //declair UI Elements
    private lateinit var comment: EditText
    private lateinit var btnSave: Button
    private lateinit var btnManage: Button

    //declair firebase database reference
    private lateinit var dbRef: DatabaseReference


    //define the onCreate reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        //initialize UI element
        comment = findViewById(R.id.comment)
        btnSave = findViewById(R.id.sumbitbutton)
        btnManage = findViewById(R.id.btnreviweComment)

        //initialize firebase database reference
        dbRef = FirebaseDatabase.getInstance().getReference("comments")

        //set a click listener for the "save button"
        btnSave.setOnClickListener {
            saveComments()
        }


        //set a click listner for the "Manage" button
        btnManage.setOnClickListener {
            val intent = Intent(this, manageReview::class.java)
            startActivity(intent)
        }


    }

    //define the saveComment method
    private fun saveComments() {

        //getting comment
        val newComment = comment.text.toString()

        //check if comment is empty
        if (newComment.isEmpty()) {
            //display error message
            comment.error = "Please Enter Comment"
            Toast.makeText(this, "Please Enter Comment", Toast.LENGTH_LONG).show()
            return
        }

        //generate unique key for comment
        val commentId = dbRef.push().key!!

        //create commentModel with id and comment
        val shopComment = commentModel(commentId, newComment)

        //save the CommentModel object to the Firebase RealTime database
        dbRef.child(commentId).setValue(shopComment).addOnCompleteListener {
            Toast.makeText(this, "Comment Added Successfully", Toast.LENGTH_LONG).show()
            comment.text.clear()
            val intent = Intent(this, manageReview::class.java)
            startActivity(intent)

            //display error message if the save option faild
        }.addOnFailureListener { err ->
            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
        }
    }
}